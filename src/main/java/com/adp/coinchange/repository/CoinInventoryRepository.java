package com.adp.coinchange.repository;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.entities.CoinInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinInventoryRepository extends JpaRepository<CoinInventoryEntity, String> {

    CoinInventoryEntity findCoinInventoryEntityByCoinType(final CoinType coinType);
}
