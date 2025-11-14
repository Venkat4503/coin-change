package com.adp.coinchange.controller;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.service.CoinChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class CoinChangeController {

    private final CoinChangeService coinChangeService;

    @Autowired
    public CoinChangeController(final CoinChangeService coinChangeService) {
        this.coinChangeService = coinChangeService;
    }

    @GetMapping(value = "/api/change/{bill}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<CoinType, Integer> calculateChange(@PathVariable final Integer bill) {
        log.info("Calculating change for {}", bill);
        final Map<CoinType, Integer> response = coinChangeService.calculateChange(bill);
        log.info("Response: {} for the request bill: {}", response, bill);
        return response;
    }
}