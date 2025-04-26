package com.demo.app.interfaces.controller.card.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Create account request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequest {
    @Size(max = 255)
    private String info;
}