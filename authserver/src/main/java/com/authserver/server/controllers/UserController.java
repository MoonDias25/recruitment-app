package com.authserver.server.controllers;

import com.authserver.server.dto.UserEditDto;
import com.authserver.server.entity.User;
import com.authserver.server.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/employees")
@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public String showUsersList(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "lastName-asc") String sortOption,
            Model model, Authentication authentication) {

        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"));

        Page<User> userPage = userService.getUsersPaginatedAndFiltered(isSuperAdmin, email, page, size, sortOption);

        model.addAttribute("employees", userPage.getContent());
        model.addAttribute("isSuperAdmin", isSuperAdmin);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        model.addAttribute("currentEmail", email);
        model.addAttribute("currentSortOption", sortOption);

        return "admin/users-list";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, Authentication authentication) {
        User user = userService.getUserById(id);
        UserEditDto userDto = new UserEditDto(user);

        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"));

        List<String> allowedRoles;
        if (isSuperAdmin) {
            allowedRoles = Arrays.asList("USER", "HR", "ADMIN", "SUPER_ADMIN");
        } else {
            allowedRoles = Arrays.asList("USER", "HR");
        }

        model.addAttribute("user", userDto);
        model.addAttribute("allRoles", allowedRoles);
        return "admin/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable String id, @ModelAttribute("user") UserEditDto dto) {
        userService.updateEmployee(id, dto);

        return "redirect:/admin/employees";
    }

}
