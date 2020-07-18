package com.demo.controller;

import com.demo.model.UserAccount;
import com.demo.orchestrator.RegistrationOrchestrator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationOrchestrator registrationOrchestrator;

    @Test
    public void registerUserAccount() throws Exception {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmailAddress("some@email.com");
        userAccount.setPassword("somePassword");
        userAccount.setFirstName("someFirstName");
        userAccount.setLastName("someLastName");

        this.mockMvc.perform(post("/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userAccount)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(registrationOrchestrator, times(1)).orchestrate(eq(userAccount));
    }
}