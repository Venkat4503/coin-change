package com.adp.coinchange.junit.controller;

import com.adp.coinchange.controller.CoinChangeController;
import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.service.CoinChangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoinChangeControllerTest {

    @Mock
    private CoinChangeService coinChangeService;

    @InjectMocks
    private CoinChangeController coinChangeController;

    @Test
    public void coinChangeTest() {
        when(coinChangeService.calculateChange(10)).thenReturn(Map.of(CoinType.QUARTER, 40));
        final Map<CoinType, Integer> response = coinChangeController.calculateChange(10);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
        Assertions.assertTrue(response.containsKey(CoinType.QUARTER));
        Assertions.assertEquals(40, response.get(CoinType.QUARTER));
    }
}
