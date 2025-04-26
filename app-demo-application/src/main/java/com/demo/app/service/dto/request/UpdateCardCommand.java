package com.demo.app.service.dto.request;

import com.demo.app.domain.model.card.CardStatus;
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
public class UpdateCardCommand {
    private Long id;
    private CardStatus status;
}
