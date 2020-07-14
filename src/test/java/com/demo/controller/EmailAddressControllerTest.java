package com.demo.controller;

import com.demo.model.EmailAddressStatus;
import com.demo.service.EmailAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
public class EmailAddressControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EmailAddressService emailAddressService;

    @MockBean
    private ErrorHandlerAdvice errorHandlerAdvice;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        when(emailAddressService.getEmailAddressStatus(anyString())).thenReturn(EmailAddressStatus.ACTIVATED);
    }

    @Test
    public void getEmailAddressStatus_validEmailAddress() throws Exception {
        this.mockMvc.perform(get("/emailAddress/valid@email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Activated")));

        verify(emailAddressService, times(1)).getEmailAddressStatus("valid@email.com");
    }
}
