package com.demo.app.interfaces.controller.account;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.common.entity.CommonResultCode;
import com.demo.app.interfaces.controller.account.request.ChangeAccountStatusRequest;
import com.demo.app.interfaces.controller.account.request.CreateAccountRequest;
import com.demo.app.service.common.entity.request.CreateAccountCommand;
import com.demo.app.service.common.entity.request.UpdateAccountCommand;
import com.demo.app.service.common.entity.response.AccountDTO;
import com.demo.app.service.common.entity.response.CardDTO;
import com.demo.app.service.service.AccountCardBizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Account controller test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountController Unit Tests")
class AccountControllerTest {
    private static final Long ACCOUNT_ID = 1L;
    private static final String VALID_EMAIL = "test@test.com";
    private static final String ACCOUNT_INFO = "Test Account";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Account testAccount;
    private AccountDTO testAccountDTO;
    @Mock
    private AccountCardBizService bizService;
    @Spy
    private WebEntityConvertor webEntityConvertor = Mappers.getMapper(WebEntityConvertor.class);
    @InjectMocks
    private AccountController controller;

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        testAccount = new Account();
        testAccount.setEmail(VALID_EMAIL);
        testAccount.setId(ACCOUNT_ID);
        testAccountDTO = new AccountDTO();
        testAccountDTO.setEmail(VALID_EMAIL);
        testAccountDTO.setId(ACCOUNT_ID);
    }

    /**
     * The type Account operations.
     */
    @Nested
    @DisplayName("Account Operations")
    class AccountOperations {

        /**
         * The type Account creation.
         */
        @Nested
        @DisplayName("Account Creation")
        class AccountCreation {
            private CreateAccountRequest validRequest() {
                return new CreateAccountRequest(VALID_EMAIL, ACCOUNT_INFO);
            }

            /**
             * Create account success.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("create account successfully")
            void createAccountSuccess() throws Exception {
                var command = CreateAccountCommand.builder()
                        .email(VALID_EMAIL)
                        .info(ACCOUNT_INFO)
                        .build();
                when(bizService.createAccount(command)).thenReturn(testAccount);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest())))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.ok").value(true))
                        .andExpect(jsonPath("$.code").value(CommonResultCode.SUCCESS.getCode()))
                        .andExpect(jsonPath("$.data.id").value(ACCOUNT_ID));
                verify(bizService, times(1)).createAccount(command);
            }

            /**
             * Create with invalid email.
             *
             * @param invalidEmail the invalid email
             * @throws Exception the exception
             */
            @ParameterizedTest
            @ValueSource(strings = {"invalid-email",
                    "",
                    "bad@",
                    "bad@.com",})
            @DisplayName("reject invalid email format")
            void createWithInvalidEmail(String invalidEmail) throws Exception {
                var request = new CreateAccountRequest(invalidEmail, ACCOUNT_INFO);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }

        /**
         * Gets account success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("get existing account details")
        void getAccountSuccess() throws Exception {
            when(bizService.getAccountById(ACCOUNT_ID)).thenReturn(Optional.of(testAccount));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{id}", ACCOUNT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.code").value(CommonResultCode.SUCCESS.getCode()))
                    .andExpect(jsonPath("$.message").value(CommonResultCode.SUCCESS.getMessage()))
                    .andExpect(jsonPath("$.data.id").value(ACCOUNT_ID))
                    .andExpect(jsonPath("$.data.email").value(VALID_EMAIL));
        }


        /**
         * Gets account not found.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("get non-existing account returns empty data")
        void getAccountNotFound() throws Exception {
            when(bizService.getAccountById(ACCOUNT_ID)).thenReturn(Optional.empty());

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{id}", ACCOUNT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }


    /**
     * The type Status management.
     */
    @Nested
    @DisplayName("Status Management")
    class StatusManagement {
        private ChangeAccountStatusRequest validRequest(AccountStatus status) {
            var request = new ChangeAccountStatusRequest();
            request.setStatus(status);
            return request;
        }

        /**
         * Update status success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("update account status successfully")
        void updateStatusSuccess() throws Exception {
            var command = UpdateAccountCommand.builder()
                    .id(ACCOUNT_ID)
                    .status(AccountStatus.ACTIVATED)
                    .build();

            when(bizService.updateAccountStatus(command)).thenReturn(AccountStatus.ACTIVATED);

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/accounts/{id}/status", ACCOUNT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest(AccountStatus.ACTIVATED))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.status").value("ACTIVATED"));
        }

        /**
         * Update status without body.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("reject status update without status field")
        void updateStatusWithoutBody() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/accounts/{id}/status", ACCOUNT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * The type Pagination tests.
     */
    @Nested
    @DisplayName("Pagination Queries")
    class PaginationTests {


        /**
         * Gets account page.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("get account page with cards")
        void getAccountPage() throws Exception {
            final Long pageNum = 1L;
            final Long pageSize = 10L;
            var page = CommonPage.<AccountDTO>builder()
                    .list(Collections.singletonList(testAccountDTO))
                    .info(new CommonPageInfo(pageNum, pageSize))
                    .build();

            when(bizService.getAccountPage(any(), anyBoolean())).thenReturn(page);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                            .param("page", pageNum.toString())
                            .param("size", pageSize.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.info.pageNum").value(pageNum))
                    .andExpect(jsonPath("$.data.info.pageSize").value(pageSize))
                    .andExpect(jsonPath("$.data.list[0].id").value(ACCOUNT_ID));


            var pageCaptor = ArgumentCaptor.forClass(CommonPageInfo.class);
            verify(bizService).getAccountPage(pageCaptor.capture(), eq(true));
            assertThat(pageCaptor.getValue()).usingRecursiveComparison()
                    .isEqualTo(new CommonPageInfo(pageNum, pageSize));
        }

        /**
         * Gets account cards.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("get cards for account")
        void getAccountCards() throws Exception {
            final Long pageNum = 1L;
            final Long pageSize = 10L;
            var page = CommonPage.<CardDTO>builder()
                    .info(new CommonPageInfo(pageNum, pageSize))
                    .list(Collections.emptyList())
                    .build();

            when(bizService.getCardByAccountId(ACCOUNT_ID, new CommonPageInfo(pageNum, pageSize))).thenReturn(page);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{id}/cards", ACCOUNT_ID)
                            .param("page", pageNum.toString())
                            .param("size", pageSize.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.info.pageNum").value(pageNum))
                    .andExpect(jsonPath("$.data.info.pageSize").value(pageSize))
                    .andExpect(jsonPath("$.data.list").isEmpty());
        }
    }
}