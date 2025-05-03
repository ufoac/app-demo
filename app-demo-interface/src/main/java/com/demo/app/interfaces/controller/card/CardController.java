package com.demo.app.interfaces.controller.card;

import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.common.entity.CommonResult;
import com.demo.app.interfaces.common.entity.CommonResultCode;
import com.demo.app.interfaces.controller.card.request.AssignCardRequest;
import com.demo.app.interfaces.controller.card.request.ChangeCardStatusRequest;
import com.demo.app.interfaces.controller.card.request.CreateCardRequest;
import com.demo.app.interfaces.controller.card.response.CardAssignResult;
import com.demo.app.interfaces.controller.card.response.CardStatusResult;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.common.entity.request.AssignCardCommand;
import com.demo.app.service.common.entity.request.CreateCardCommand;
import com.demo.app.service.common.entity.request.UpdateCardCommand;
import com.demo.app.service.service.AccountCardBizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Card Controller handles card-related operations
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
@Validated
public class CardController {
    private final AccountCardBizService accountCardBizService;
    private final WebEntityConvertor webEntityConvertor;

    /**
     * Create new card
     *
     * @param request Card creation data
     * @return Created card details
     */
    @PostMapping
    public CommonResult<CardVO> createCard(@Valid @RequestBody CreateCardRequest request) {
        return Optional.ofNullable(accountCardBizService.createCard(
                        CreateCardCommand.builder()
                                .info(request.getInfo())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Update card status
     *
     * @param id      the id
     * @param request Status change request
     * @return Updated card details
     */
    @PatchMapping("/{id}/status")
    public CommonResult<CardStatusResult> updateCardStatus(@PathVariable("id") @Positive Long id,
                                                           @Valid @RequestBody ChangeCardStatusRequest request) {
        var status = accountCardBizService.updateCardStatus(
                UpdateCardCommand.builder()
                        .id(id)
                        .status(request.getStatus())
                        .build());
        return CommonResult.success(CardStatusResult.builder()
                .id(id)
                .status((status))
                .build());
    }


    /**
     * Assign card to account
     *
     * @param id      the id
     * @param request Assignment details
     * @return Updated card details
     */
    @PatchMapping("/{id}/assign")
    public CommonResult<CardAssignResult> assignCard(@PathVariable("id") @Positive Long id,
                                                     @Valid @RequestBody AssignCardRequest request) {
        accountCardBizService.assignCard(
                AssignCardCommand.builder()
                        .accountId(request.getAccountId())
                        .cardId(id)
                        .build());
        return CommonResult.success(CardAssignResult.builder()
                .id(id)
                .accountId(request.getAccountId())
                .build());
    }

    /**
     * Get card details by ID
     *
     * @param id Card ID
     * @return Card details
     */
    @GetMapping("/{id}")
    public CommonResult<CardVO> card(@PathVariable("id") @Positive Long id) {
        return accountCardBizService.getCardById(id)
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.success(null));
    }

}
