package com.adp.coinchange.config;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.service.CoinChangeService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "coin.change")
public class LoadDataConfig {

    private final CoinChangeService changeService;
    private Map<CoinType, Integer> initialCoins;

    @PostConstruct
    public void initLoadData() {
        changeService.saveInitialData(initialCoins);
    }
}
