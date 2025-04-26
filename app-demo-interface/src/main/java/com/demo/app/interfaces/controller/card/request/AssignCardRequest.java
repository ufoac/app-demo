package com.demo.app.interfaces.controller.card.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssignCardRequest
 *
 * @author cao
 * @since 2025 /4/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignCardRequest {
    @Positive
    private Long accountId;
    @Positive
    private Long cardId;
}
