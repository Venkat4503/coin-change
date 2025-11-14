package com.adp.coinchange.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CoinChangeControllerIntegrationTest extends BaseTest {

    @Test
    public void calculateChangeTest() throws Exception {
        mockMvc.perform(get("/api/change/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"0.25\":40}"));
    }

    @Test
    public void calculateChangeInValidBillException() throws Exception {
        mockMvc.perform(get("/api/change/15").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"uri\":\"/api/change/15\",\"method\":\"GET\",\"exceptionMessage\":\"The provided bill amount is invalid for the amount: 15\"}"));
    }

    @Test
    public void calculateChangeException() throws Exception {
        mockMvc.perform(get("/api/change/null").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"uri\":\"/api/change/null\",\"method\":\"GET\",\"exceptionMessage\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \\\"null\\\"\"}"));
    }
}
