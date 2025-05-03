package com.demo.app.interfaces.controller.account.request;

import com.demo.app.domain.model.account.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * The type Change account status request.
 */
@Data
public class ChangeAccountStatusRequest {
    @NotNull
    private AccountStatus status;
}
