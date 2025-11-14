package com.adp.coinchange.service;

import com.adp.coinchange.dto.CoinType;

import java.util.Map;

public interface CoinChangeService {

    Map<CoinType, Integer> calculateChange(final Integer bill);

    void saveInitialData(final Map<CoinType, Integer> initialCoins);
}
