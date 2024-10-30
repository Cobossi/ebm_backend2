package com.expert.beaute.mobile_backend.expert.register;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterERequest {

    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String nom;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String prenom;

    @Email(message = "Email is not well formatted")
    private String email;

    private String phoneNumber;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    // Au moins l'email ou le téléphone doit être fourni
    @AssertTrue(message = "Either email or phone number must be provided")
    private boolean isContactInformationValid() {
        return (email != null && !email.trim().isEmpty()) ||
                (phoneNumber != null && !phoneNumber.trim().isEmpty());
    }
}
