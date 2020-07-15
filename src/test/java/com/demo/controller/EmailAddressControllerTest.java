package com.demo.controller;

import com.demo.model.EmailAddressStatus;
import com.demo.service.EmailAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailAddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailAddressService emailAddressService;

    @BeforeEach
    public void setup() {
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
