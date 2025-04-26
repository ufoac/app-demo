package com.demo.app.interfaces.controller.account;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.common.entity.CommonResult;
import com.demo.app.interfaces.common.entity.CommonResultCode;
import com.demo.app.interfaces.controller.account.request.AccountPageQuery;
import com.demo.app.interfaces.controller.account.request.CardPageQuery;
import com.demo.app.interfaces.controller.account.request.ChangeAccountStatusRequest;
import com.demo.app.interfaces.controller.account.request.CreateAccountRequest;
import com.demo.app.interfaces.controller.account.response.AccountVO;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.dto.request.CreateAccountCommand;
import com.demo.app.service.dto.request.UpdateAccountCommand;
import com.demo.app.service.service.AccountCardBizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Account Controller handles account-related operations
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountCardBizService accountCardBizService;
    private final WebEntityConvertor webEntityConvertor;


    /**
     * Create new account
     *
     * @param request Account creation data
     * @return Created account details
     */
    @PostMapping("/create")
    CommonResult<AccountVO> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return Optional.ofNullable(accountCardBizService.createAccount(
                        CreateAccountCommand.builder()
                                .email(request.getEmail())
                                .info(request.getInfo())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Update account status
     *
     * @param request Status change request
     * @return Updated account details
     */
    @PostMapping("/status")
    CommonResult<AccountVO> updateAccountStatus(@Valid @RequestBody ChangeAccountStatusRequest request) {
        return Optional.ofNullable(accountCardBizService.updateAccountStatus(
                        UpdateAccountCommand.builder()
                                .id(request.getId())
                                .status(request.getStatus())
                                .build()))
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.failed(CommonResultCode.ERROR));
    }

    /**
     * Paginated account search with card association
     *
     * @param query Search criteria and pagination
     * @return Paginated account results
     */
    @GetMapping("/pages")
    public CommonResult<CommonPage<AccountVO>> pageAccountWithCards(@Valid @ModelAttribute AccountPageQuery query) {
        var pageInfo = new CommonPageInfo(query.getPage(), query.getSize());
        var pageData = accountCardBizService.getAccountPage(pageInfo, query.getWithCards());
        return CommonResult.success(pageData.convert(webEntityConvertor::toVO));
    }

    /**
     * Get paginated cards for specific account
     *
     * @param accountId Target account ID
     * @param query     Pagination parameters
     * @return Paginated card results
     */
    @GetMapping("/{accountId}/cards")
    public CommonResult<CommonPage<CardVO>> pageAccountCards(@PathVariable("accountId") @Positive Long accountId,
                                                             @Valid @ModelAttribute CardPageQuery query) {
        var pageInfo = new CommonPageInfo(query.getPage(), query.getSize());
        var pageData = accountCardBizService.getCardByAccountId(accountId, pageInfo);
        return CommonResult.success(pageData.convert(webEntityConvertor::toVO));
    }

    /**
     * Get account details by ID
     *
     * @param id Account ID
     * @return Account details
     */
    @GetMapping("/detail/{id}")
    CommonResult<AccountVO> account(@PathVariable("id") @Positive Long id) {
        return accountCardBizService.getAccountById(id)
                .map(webEntityConvertor::toVO)
                .map(CommonResult::success)
                .orElse(CommonResult.success(null));
    }
}