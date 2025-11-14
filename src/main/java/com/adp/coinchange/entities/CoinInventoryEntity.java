package com.adp.coinchange.entities;

import com.adp.coinchange.dto.CoinType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "coin_inventory")
public class CoinInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "coinType", nullable = false, unique = true)
    private CoinType coinType;

    @Column(name = "availableCoinsQuantity")
    private Integer availableCoinsQuantity;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoinInventoryEntity that = (CoinInventoryEntity) o;
        return coinType == that.coinType && Objects.equals(availableCoinsQuantity, that.availableCoinsQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coinType, availableCoinsQuantity);
    }
}
