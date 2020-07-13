package com.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailAddressController.class)
public class EmailAddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getEmailAddress_validEmailAddress() throws Exception {
        this.mockMvc.perform(get("/emailAddress/valid@email.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getEmailAddress_invalidEmailAddress() throws Exception {
        this.mockMvc.perform(get("/emailAddress/invalid@@email.com"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is("E1")))
                .andExpect(jsonPath("message", is("Invalid email address")));
    }
}
