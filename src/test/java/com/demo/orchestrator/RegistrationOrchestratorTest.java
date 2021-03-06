package com.demo.orchestrator;

import com.demo.controller.exception.UserRegistrationException;
import com.demo.controller.exception.UserRegistrationExceptionType;
import com.demo.external.email.EmailService;
import com.demo.external.email.Mail;
import com.demo.external.hash.HashService;
import com.demo.external.hash.SaltGenerationService;
import com.demo.model.Activation;
import com.demo.model.EmailAddressStatus;
import com.demo.model.UserAccount;
import com.demo.repository.UserAccountRepository;
import com.demo.service.ActivationGenerator;
import com.demo.service.EmailAddressService;
import com.demo.service.UuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationOrchestratorTest {
    @InjectMocks
    private RegistrationOrchestrator target;

    @Mock
    private EmailAddressService emailAddressService;

    @Mock
    private SaltGenerationService saltGenerationService;

    @Mock
    private HashService hashService;

    @Mock
    private ActivationGenerator activationGenerator;

    @Mock
    private UuidGenerator uuidGenerator;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<UserAccount> userAccountArgumentCaptor;

    @Captor
    private ArgumentCaptor<Mail> mailArgumentCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(target, "activationEmailSender", "someTest@sender.com");
        ReflectionTestUtils.setField(target, "activationEmailSubject", "Some Test Subject");
        ReflectionTestUtils.setField(target, "activationEmailBody", "Some Test Body");

        Activation activation = new Activation();
        activation.setCode("someActivationCode");
        activation.setExpiration(LocalDateTime.of(2020, 7, 18, 2, 27));

        when(saltGenerationService.generateRandomSalt()).thenReturn("someSalt");
        when(hashService.hash(anyString(), anyString())).thenReturn("someHash");
        when(activationGenerator.generateActivation()).thenReturn(activation);
        when(uuidGenerator.generateRandomUuid()).thenReturn("sommeUuid");
    }

    @Test
    public void orchestrate_whenEmailAddressIsAlreadyRegistered() {
        when(emailAddressService.getEmailAddressStatus(anyString())).thenReturn(EmailAddressStatus.REGISTERED);

        UserAccount userAccount = new UserAccount();
        userAccount.setEmailAddress("someEmail@address.com");

        UserRegistrationException e = assertThrows(UserRegistrationException.class, () -> target.orchestrate(userAccount));
        assertThat(e.getType()).isEqualTo(UserRegistrationExceptionType.EMAIL_ADDRESS_IS_DUE_FOR_ACTIVATION_EXCEPTION);

        verify(emailAddressService, times(1)).getEmailAddressStatus("someEmail@address.com");
    }

    @Test
    public void orchestrate_whenEmailAddressIsAlreadyActivated() {
        when(emailAddressService.getEmailAddressStatus(anyString())).thenReturn(EmailAddressStatus.ACTIVATED);

        UserAccount userAccount = new UserAccount();
        userAccount.setEmailAddress("someEmail@address.com");

        UserRegistrationException e = assertThrows(UserRegistrationException.class, () -> target.orchestrate(userAccount));
        assertThat(e.getType()).isEqualTo(UserRegistrationExceptionType.EMAIL_ADDRESS_IS_ALREADY_TAKEN_EXCEPTION);

        verify(emailAddressService, times(1)).getEmailAddressStatus("someEmail@address.com");
    }

    @Test
    public void orchestrate_whenEmailAddressIsAvailable() {
        when(emailAddressService.getEmailAddressStatus(anyString())).thenReturn(EmailAddressStatus.NOT_REGISTERED);

        UserAccount userAccount = new UserAccount();
        userAccount.setEmailAddress("someEmail@address.com");
        userAccount.setPassword("userInputPassword");
        userAccount.setFirstName("Nikki Nicholas");
        userAccount.setLastName("Romero");

        target.orchestrate(userAccount);

        verify(saltGenerationService, times(1)).generateRandomSalt();
        verify(hashService, times(1)).hash("userInputPassword", "someSalt");
        verify(activationGenerator, times(1)).generateActivation();
        verify(uuidGenerator, times(1)).generateRandomUuid();
        verify(userAccountRepository, times(1)).save(userAccountArgumentCaptor.capture());

        UserAccount actualUserAccount = userAccountArgumentCaptor.getValue();
        assertThat(actualUserAccount).isNotNull();
        assertThat(actualUserAccount.getId()).isEqualTo("sommeUuid");
        assertThat(actualUserAccount.getEmailAddress()).isEqualTo("someEmail@address.com");
        assertThat(actualUserAccount.getPassword()).isEqualTo("someHash");
        assertThat(actualUserAccount.getSalt()).isEqualTo("someSalt");
        assertThat(actualUserAccount.getActivationCode()).isEqualTo("someActivationCode");
        assertThat(actualUserAccount.getActivationExpiration()).isEqualTo(LocalDateTime.of(2020, 7, 18, 2, 27));
        assertThat(actualUserAccount.getStatus()).isEqualTo(EmailAddressStatus.REGISTERED);
        assertThat(actualUserAccount.getFirstName()).isEqualTo("Nikki Nicholas");
        assertThat(actualUserAccount.getLastName()).isEqualTo("Romero");

        verify(emailService, times(1)).send(mailArgumentCaptor.capture());

        Mail actualMail = mailArgumentCaptor.getValue();
        assertThat(actualMail).isNotNull();
        assertThat(actualMail.getFrom()).isEqualTo("someTest@sender.com");
        assertThat(actualMail.getTo()).isEqualTo("someEmail@address.com");
        assertThat(actualMail.getSubject()).isEqualTo("Some Test Subject");
        assertThat(actualMail.getBody()).isEqualTo("Some Test Body");
    }
}
