package com.authserver.server.dto;

import com.authserver.server.entity.User;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class UserEditDto {

    private String id;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-ZĂÂÎȘȚ][a-zA-Zăâîșț\\s-]{1,39}",
            message = "First name must begin with a capital letter and must not contain symbols or numbers!")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-ZĂÂÎȘȚ][a-zA-Zăâîșț\\s-]{1,39}",
            message = "Last name must begin with a capital letter and must not contain symbols or numbers!")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^0[0-9]{9}$",
            message = "Phone number must begin with 0 and must be exactly 10 figures")
    private String phoneNumber;

    @NotNull(message = "This field can't be blank!")
    @Past(message = "Birth date must be in the past!")
    private LocalDate birthDate;

    private boolean active;

    @NotBlank
    private String roleName;

    public UserEditDto() {}

    public UserEditDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.birthDate = user.getBirthDate();
        this.active = user.isActive();
        this.roleName = user.getAuthority().getAuthorityName().replace("ROLE_", "");
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
