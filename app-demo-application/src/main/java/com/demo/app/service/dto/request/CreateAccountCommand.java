package com.demo.app.service.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * CreateAccountCommand
 *
 * @author cao
 * @since 2025/4/24
 */
@Data
@Builder
public class CreateAccountCommand {
    private String email;
    private String info;
}
