package com.adp.coinchange.service;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.entities.CoinInventoryEntity;
import com.adp.coinchange.exception.InSufficientCoinsException;
import com.adp.coinchange.exception.InValidBillException;
import com.adp.coinchange.exception.ResourceNotFoundException;
import com.adp.coinchange.repository.CoinInventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoinChangeServiceImpl implements CoinChangeService {

    @Value("#{'${coin.change.valid.bills}'.split(',')}")
    protected List<Integer> validBills;

    private final CoinInventoryRepository coinInventoryRepository;

    @Autowired
    public CoinChangeServiceImpl(final CoinInventoryRepository coinInventoryRepository) {
        this.coinInventoryRepository = coinInventoryRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Map<CoinType, Integer> calculateChange(final Integer bill) {
        if (!validBills.contains(bill)) {
            log.error("The provided bill amount is invalid for the amount: {}", bill);
            throw new InValidBillException("The provided bill amount is invalid for the amount: " + bill);
        }
        return processCalculateChange(bill, getCoinInventoryEntityList());
    }

    private Map<CoinType, Integer> processCalculateChange(final Integer bill, final Map<CoinType, Integer> coinInventoryMap) {
        final Map<CoinType, Integer> coinChangeMap = new HashMap<>();
        final AtomicReference<Double> amount = new AtomicReference<>(Double.valueOf(bill));
        coinInventoryMap.keySet().stream().filter(Objects::nonNull)
                .sorted(Comparator.reverseOrder()).forEach(coinType -> {
                    final double amountRemaining = amount.get();
                    if (amountRemaining > 0) {
                        final double coinAmount = coinType.getAmount().doubleValue();
                        final int neededCoins = (int) (amountRemaining / coinAmount);
                        final int availableCoins = coinInventoryMap.get(coinType);

                        if (neededCoins > 0 && availableCoins > 0) {
                            final int coinsToUse = Math.min(neededCoins, availableCoins);
                            coinChangeMap.put(coinType, coinsToUse);
                            updateCoinInventory(coinType, coinsToUse);
                            amount.getAndUpdate(value -> value - coinsToUse * coinAmount);
                        }
                    }
                });
        if (amount.get() > 0) {
            throw new InSufficientCoinsException("Insufficient coins in the inventory to process the request.");
        }
        return coinChangeMap;
    }

    @Override
    public void saveInitialData(final Map<CoinType, Integer> initialCoins) {
        if (!CollectionUtils.isEmpty(initialCoins)) {
            initialCoins.entrySet().stream()
                    .map(entry -> {
                        CoinInventoryEntity coinInventoryEntity = new CoinInventoryEntity();
                        coinInventoryEntity.setCoinType(entry.getKey());
                        coinInventoryEntity.setAvailableCoinsQuantity(entry.getValue());
                        return coinInventoryEntity;
                    })
                    .forEach(coinInventoryRepository::save);
        }
    }

    private void updateCoinInventory(final CoinType coinType, final int coinsToUse) {
        Map<CoinType, Integer> coinTypeInventory = coinInventory(coinType, coinsToUse);
        coinTypeInventory.entrySet().stream()
                .map(entry -> {
                    final CoinInventoryEntity coinInventoryEntity = Optional.ofNullable(
                                    coinInventoryRepository.findCoinInventoryEntityByCoinType(entry.getKey()))
                            .orElseThrow(() -> new ResourceNotFoundException(String.format("Coin type: %s with amount: %s not found",
                                    entry.getKey().name(), entry.getKey().getAmount())));
                    coinInventoryEntity.setAvailableCoinsQuantity(entry.getValue());
                    return coinInventoryEntity;
                })
                .forEach(coinInventoryRepository::save);
    }

    private Map<CoinType, Integer> coinInventory(final CoinType coinType, final int coinsToUse) {
        Map<CoinType, Integer> coinInventoryMap = getCoinInventoryEntityList();
        return Map.of(coinType, coinInventoryMap.get(coinType) - coinsToUse);
    }

    private Map<CoinType, Integer> getCoinInventoryEntityList() {
        final List<CoinInventoryEntity> coinInventoryEntityList = coinInventoryRepository.findAll();
        if (CollectionUtils.isEmpty(coinInventoryEntityList)) {
            log.error("Insufficient coins in the inventory to process the request.");
            throw new InSufficientCoinsException("Insufficient coins in the inventory to process the request.");
        }
        return coinInventoryEntityList.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(CoinInventoryEntity::getCoinType, CoinInventoryEntity::getAvailableCoinsQuantity));
    }
}
