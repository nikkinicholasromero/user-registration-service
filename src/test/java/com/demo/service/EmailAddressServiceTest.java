package com.demo.service;

import com.demo.model.EmailAddressStatus;
import com.demo.model.UserAccount;
import com.demo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmailAddressServiceTest {
    @InjectMocks
    private EmailAddressService target;

    @Mock
    private UserAccountRepository userAccountRepository;

    private Optional<UserAccount> optionalUserAccount;

    private UserAccount userAccount;

    @BeforeEach
    private void setup() {
        MockitoAnnotations.initMocks(this);

        userAccount = new UserAccount();
        userAccount.setStatus(EmailAddressStatus.ACTIVATED);
    }

    @Test
    public void getEmailAddressStatus_userAccountExists_thenReturnEmailAddressStatus() {
        optionalUserAccount = Optional.of(userAccount);
        when(userAccountRepository.getUserAccountByEmailAddress(anyString()))
                .thenReturn(optionalUserAccount);

        EmailAddressStatus status = target.getEmailAddressStatus("some@email.com");

        assertThat(status).isEqualTo(EmailAddressStatus.ACTIVATED);

        verify(userAccountRepository, times(1)).getUserAccountByEmailAddress("some@email.com");
    }

    @Test
    public void getEmailAddressStatus_userAccountDoesNotExist_thenReturnNotRegistered() {
        optionalUserAccount = Optional.ofNullable(null);
        when(userAccountRepository.getUserAccountByEmailAddress(anyString()))
                .thenReturn(optionalUserAccount);

        EmailAddressStatus status = target.getEmailAddressStatus("some@email.com");

        assertThat(status).isEqualTo(EmailAddressStatus.NOT_REGISTERED);

        verify(userAccountRepository, times(1)).getUserAccountByEmailAddress("some@email.com");
    }
}
