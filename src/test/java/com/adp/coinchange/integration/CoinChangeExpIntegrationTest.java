package com.adp.coinchange.integration;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.repository.CoinInventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.adp.coinchange.mockdata.MockData.getCoinInventoryEntityList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CoinChangeExpIntegrationTest extends BaseTest {

    @MockBean
    protected CoinInventoryRepository coinInventoryRepository;

    @Test
    public void calculateChangeInValidBillException() throws Exception {
        when(coinInventoryRepository.findAll()).thenReturn(getCoinInventoryEntityList());
        when(coinInventoryRepository.findCoinInventoryEntityByCoinType(CoinType.QUARTER)).thenReturn(null);

        mockMvc.perform(get("/api/change/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"uri\":\"/api/change/10\",\"method\":\"GET\",\"exceptionMessage\":\"Coin type: QUARTER with amount: 0.25 not found\"}"));
    }

    @Test
    public void calculateChangeInSufficientCoinsException() throws Exception {
        mockMvc.perform(get("/api/change/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"uri\":\"/api/change/10\",\"method\":\"GET\",\"exceptionMessage\":\"Insufficient coins in the inventory to process the request.\"}"));
    }
}
