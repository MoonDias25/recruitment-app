package com.authserver.server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @RequestMapping("/oauth2/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object errorCode = request.getAttribute("oauth2_error_code");
        Object errorDesc = request.getAttribute("oauth2_error_description");

        model.addAttribute("oauth2_error_code", errorCode);
        model.addAttribute("oauth2_error_description", errorDesc);

        return "error";
    }
}
