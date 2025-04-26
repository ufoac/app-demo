package com.demo.app.service.dto.request;

import com.demo.app.domain.model.account.AccountStatus;
import lombok.Builder;
import lombok.Data;

/**
 * CreateAccountCommand
 *
 * @author cao
 * @since 2025 /4/24
 */
@Data
@Builder
public class UpdateAccountCommand {
    private Long id;
    private AccountStatus status;
}
