package com.authserver.server.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class RegisterRequest {

    @NotBlank(message = "This field can't be blank!")
    @Pattern(regexp = "^[A-ZĂÂÎȘȚ][a-zA-Zăâîșț\\s-]{1,39}",
            message = "First name must begin with a capital letter and must not contain symbols or numbers!")
    private String firstName;

    @NotBlank(message = "This field can't be blank!")
    @Pattern(regexp = "^[A-ZĂÂÎȘȚ][a-zA-Zăâîșț\\s-]{1,39}",
            message = "Last name must begin with a capital letter and must not contain symbols or numbers!")
    private String lastName;

    @NotBlank(message = "This field can't be blank!")
    @Email(message = "Invalid format!")
    private String email;

    @NotBlank(message = "This field can't be blank!")
    @Size(min = 6, max = 50, message = "Password should have at least 6 characters")
    private String password;

    @NotBlank(message = "This field can't be blank!")
    @Pattern(regexp = "^0[0-9]{9}$",
             message = "Phone number must begin with 0 and must be exactly 10 figures")
    private String phoneNumber;

    @NotNull(message = "This field can't be blank!")
    @Past(message = "Birth date must be in the past!")
    private LocalDate birthDate;

    public RegisterRequest(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
