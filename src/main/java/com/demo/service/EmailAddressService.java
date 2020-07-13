package com.demo.service;

import com.demo.model.EmailAddressStatus;
import com.demo.model.UserAccount;
import com.demo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailAddressService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    public EmailAddressStatus getEmailAddressStatus(String emailAddress) {
        Optional<UserAccount> userAccount = userAccountRepository.getUserAccountByEmailAddress(emailAddress);
        if (userAccount.isPresent()) {
            return userAccount.get().getStatus();
        } else {
            return EmailAddressStatus.NOT_REGISTERED;
        }
    }
}
