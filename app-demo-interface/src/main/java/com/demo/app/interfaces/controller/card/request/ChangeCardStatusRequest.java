package com.demo.app.interfaces.controller.card.request;

import com.demo.app.domain.model.card.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Change account status request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCardStatusRequest {
    @NotNull
    private CardStatus status;
}
