package com.demo.orchestrator;

import com.demo.exception.EmailAddressIsAlreadyTakenException;
import com.demo.exception.EmailAddressIsDueForActivationException;
import com.demo.external.email.EmailService;
import com.demo.external.email.Mail;
import com.demo.external.hash.HashService;
import com.demo.external.hash.SaltGenerationService;
import com.demo.model.Activation;
import com.demo.model.EmailAddressStatus;
import com.demo.model.UserAccount;
import com.demo.repository.UserAccountRepository;
import com.demo.service.ActivationService;
import com.demo.service.EmailAddressService;
import com.demo.service.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RegistrationOrchestrator {
    @Autowired
    private EmailAddressService emailAddressService;

    @Autowired
    private SaltGenerationService saltGenerationService;

    @Autowired
    private HashService hashService;

    @Autowired
    private ActivationService activationService;

    @Autowired
    private UuidGenerator uuidGenerator;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailService emailService;

    @Value("${activation.email.sender}")
    private String activationEmailSender;

    @Value("${activation.email.subject}")
    private String activationEmailSubject;

    @Value("${activation.email.body}")
    private String activationEmailBody;

    public void orchestrate(UserAccount userAccount) {
        EmailAddressStatus status = emailAddressService.getEmailAddressStatus(userAccount.getEmailAddress());
        if (EmailAddressStatus.REGISTERED.equals(status)) {
            throw new EmailAddressIsDueForActivationException();
        } else if (!EmailAddressStatus.NOT_REGISTERED.equals(status)) {
            throw new EmailAddressIsAlreadyTakenException();
        }

        String salt = saltGenerationService.generateRandomSalt();
        String hash = hashService.hash(userAccount.getPassword(), salt);
        userAccount.setPassword(hash);
        userAccount.setSalt(salt);

        Activation activation = activationService.generateActivation();
        userAccount.setActivationCode(activation.getCode());
        userAccount.setActivationExpiration(activation.getExpiration());

        userAccount.setId(uuidGenerator.generateRandomUuid());
        userAccount.setStatus(EmailAddressStatus.REGISTERED);
        userAccountRepository.save(userAccount);

        Mail mail = new Mail();
        mail.setFrom(activationEmailSender);
        mail.setTo(userAccount.getEmailAddress());
        mail.setSubject(activationEmailSubject);
        mail.setBody(String.format(activationEmailBody, userAccount.getActivationCode()));
        emailService.send(mail);
    }
}
