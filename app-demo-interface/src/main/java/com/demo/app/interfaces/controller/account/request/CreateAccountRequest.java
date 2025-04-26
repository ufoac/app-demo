package com.demo.app.interfaces.controller.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Create account request.
 */
@Data
public class CreateAccountRequest {
    @NotBlank
    @Email
    private String email;
    @Size(max = 255)
    private String info;
}