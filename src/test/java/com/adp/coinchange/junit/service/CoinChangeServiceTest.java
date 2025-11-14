package com.adp.coinchange.junit.service;

import com.adp.coinchange.dto.CoinType;
import com.adp.coinchange.exception.InSufficientCoinsException;
import com.adp.coinchange.exception.InValidBillException;
import com.adp.coinchange.exception.ResourceNotFoundException;
import com.adp.coinchange.repository.CoinInventoryRepository;
import com.adp.coinchange.service.CoinChangeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.adp.coinchange.mockdata.MockData.getCoinInventoryEntity;
import static com.adp.coinchange.mockdata.MockData.getCoinInventoryEntityList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoinChangeServiceTest {

    @InjectMocks
    private CoinChangeServiceImpl coinChangeService;

    @Mock
    private CoinInventoryRepository coinInventoryRepository;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(coinChangeService, "validBills", Arrays.asList(1, 2, 5, 10, 20, 50, 100));
    }

    @Test
    public void coinChangeServiceTest() {
        when(coinInventoryRepository.findAll()).thenReturn(getCoinInventoryEntityList());
        when(coinInventoryRepository.findCoinInventoryEntityByCoinType(CoinType.QUARTER)).thenReturn(getCoinInventoryEntity(CoinType.QUARTER));
        when(coinInventoryRepository.save(any())).thenReturn(getCoinInventoryEntity(CoinType.QUARTER));

        Map<CoinType, Integer> response = coinChangeService.calculateChange(10);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
        Assertions.assertTrue(response.containsKey(CoinType.QUARTER));
        Assertions.assertEquals(40, response.get(CoinType.QUARTER));
    }

    @Test
    public void InValidBillExceptionTest() {
        final InValidBillException inValidBillException = Assertions.assertThrows(InValidBillException.class,
                () -> coinChangeService.calculateChange(15));

        Assertions.assertNotNull(inValidBillException);
        Assertions.assertNotNull(inValidBillException.getMessage());
        Assertions.assertEquals("The provided bill amount is invalid for the amount: 15", inValidBillException.getMessage());
    }

    @Test
    public void InValidBillExceptionNullTest() {
        final InValidBillException inValidBillException = Assertions.assertThrows(InValidBillException.class,
                () -> coinChangeService.calculateChange(null));

        Assertions.assertNotNull(inValidBillException);
        Assertions.assertNotNull(inValidBillException.getMessage());
        Assertions.assertEquals("The provided bill amount is invalid for the amount: null", inValidBillException.getMessage());
    }

    @Test
    public void ResourceNotFoundExceptionTest() {
        when(coinInventoryRepository.findAll()).thenReturn(getCoinInventoryEntityList());
        when(coinInventoryRepository.findCoinInventoryEntityByCoinType(CoinType.QUARTER)).thenReturn(null);

        final ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> coinChangeService.calculateChange(10));

        Assertions.assertNotNull(resourceNotFoundException);
        Assertions.assertNotNull(resourceNotFoundException.getMessage());
        Assertions.assertEquals("Coin type: QUARTER with amount: 0.25 not found", resourceNotFoundException.getMessage());
    }

    @Test
    public void InSufficientCoinsExceptionTest() {
        when(coinInventoryRepository.findAll()).thenReturn(new ArrayList<>());

        final InSufficientCoinsException inSufficientCoinsException = Assertions.assertThrows(InSufficientCoinsException.class,
                () -> coinChangeService.calculateChange(10));

        Assertions.assertNotNull(inSufficientCoinsException);
        Assertions.assertNotNull(inSufficientCoinsException.getMessage());
        Assertions.assertEquals("Insufficient coins in the inventory to process the request.", inSufficientCoinsException.getMessage());
    }
}
