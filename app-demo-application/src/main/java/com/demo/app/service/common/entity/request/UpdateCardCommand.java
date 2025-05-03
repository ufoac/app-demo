package com.demo.app.service.common.entity.request;

import com.demo.app.domain.model.card.CardStatus;
import lombok.Builder;
import lombok.Data;


/**
 * The type Update card command.
 */
@Data
@Builder
public class UpdateCardCommand {
    private Long id;
    private CardStatus status;
}
