package com.adp.coinchange.mockdata;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.entities.CoinInventoryEntity;

import java.util.ArrayList;
import java.util.List;

public class MockData {

    public static List<CoinInventoryEntity> getCoinInventoryEntityList() {
        List<CoinInventoryEntity> coinInventoryEntityList = new ArrayList<>();
        coinInventoryEntityList.add(getCoinInventoryEntity(CoinType.PENNY));
        coinInventoryEntityList.add(getCoinInventoryEntity(CoinType.NICKEL));
        coinInventoryEntityList.add(getCoinInventoryEntity(CoinType.DIME));
        coinInventoryEntityList.add(getCoinInventoryEntity(CoinType.QUARTER));
        return coinInventoryEntityList;
    }

    public static CoinInventoryEntity getCoinInventoryEntity(final CoinType coinType) {
        CoinInventoryEntity coinInventoryEntity = new CoinInventoryEntity();
        coinInventoryEntity.setCoinType(coinType);
        coinInventoryEntity.setAvailableCoinsQuantity(100);
        return coinInventoryEntity;
    }
}
