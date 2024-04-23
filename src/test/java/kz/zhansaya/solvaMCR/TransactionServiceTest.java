package kz.zhansaya.solvaMCR;

import kz.zhansaya.solvaMCR.dtos.TransactionDto;
import kz.zhansaya.solvaMCR.entities.ExchangeRate;
import kz.zhansaya.solvaMCR.entities.Limits;
import kz.zhansaya.solvaMCR.mappers.TransactionMapper;
import kz.zhansaya.solvaMCR.repositories.ExchangeRateRepository;
import kz.zhansaya.solvaMCR.repositories.LimitRepository;
import kz.zhansaya.solvaMCR.repositories.TransactionRepository;
import kz.zhansaya.solvaMCR.services.impls.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private LimitRepository limitRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;


    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository, limitRepository,
                transactionMapper, exchangeRateRepository);
    }

    @Test
    void testSaveTransaction_LimitExceeded() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCategory(Limits.Category.GOODS);
        transactionDto.setAmount(BigDecimal.valueOf(2000)); // больше предельной суммы
        transactionDto.setDateTime(LocalDateTime.now());
        transactionDto.setCurrencyShortName("USD");
        when(limitRepository.findFirstByCategoryAndSetDateTimeBetweenOrderBySetDateTimeDesc(any(), any(), any()))
                .thenReturn(new Limits(BigDecimal.valueOf(1000), LocalDateTime.now(), Limits.Category.GOODS));


        boolean limitExceeded = transactionService.saveTransaction(transactionDto);

        assertTrue(limitExceeded);
        assertTrue(transactionDto.isLimitExceeded());
    }

    @Test
    void testSaveTransaction_LimitNotExceeded() {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCategory(Limits.Category.GOODS);
        transactionDto.setAmount(BigDecimal.valueOf(500));
        transactionDto.setDateTime(LocalDateTime.now());
        transactionDto.setCurrencyShortName("USD");
        when(limitRepository.findFirstByCategoryAndSetDateTimeBetweenOrderBySetDateTimeDesc(any(), any(), any()))
                .thenReturn(new Limits(BigDecimal.valueOf(1000), LocalDateTime.now(), Limits.Category.GOODS));


        boolean limitExceeded = transactionService.saveTransaction(transactionDto);

        assertFalse(limitExceeded);
        assertFalse(transactionDto.isLimitExceeded());
    }

    @Test
    void testSaveTransaction_LimitExceeded_CurrencyConversion() {
        TransactionDto transactionDtoKZT = new TransactionDto();
        transactionDtoKZT.setCategory(Limits.Category.GOODS);
        transactionDtoKZT.setAmount(BigDecimal.valueOf(450000));
        transactionDtoKZT.setDateTime(LocalDateTime.now());
        transactionDtoKZT.setCurrencyShortName("KZT");

        Limits monthlyLimit = new Limits(BigDecimal.valueOf(1000), LocalDateTime.now(), Limits.Category.GOODS);
        when(limitRepository.findFirstByCategoryAndSetDateTimeBetweenOrderBySetDateTimeDesc(any(), any(), any()))
                .thenReturn(monthlyLimit);

        System.out.println( monthlyLimit + " " +monthlyLimit.getMonthlyLimitUSD());

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(BigDecimal.valueOf(0.0025));
        when(exchangeRateRepository.findByDateAndBaseCurrencyAndTargetCurrency(any(), any(), any()))
                .thenReturn(exchangeRate);

        transactionDtoKZT.setLimit(monthlyLimit);

        boolean limitExceededKZT = transactionService.saveTransaction(transactionDtoKZT);

        assertTrue(limitExceededKZT);
        assertTrue(transactionDtoKZT.isLimitExceeded());
    }


    @Test
    void testSaveTransaction_LimitNotExceeded_CurrencyConversion() {
        TransactionDto transactionDtoKZT = new TransactionDto();
        transactionDtoKZT.setCategory(Limits.Category.GOODS);
        transactionDtoKZT.setAmount(BigDecimal.valueOf(300000));
        transactionDtoKZT.setDateTime(LocalDateTime.now());
        transactionDtoKZT.setCurrencyShortName("KZT");

        Limits monthlyLimit = new Limits(BigDecimal.valueOf(1000), LocalDateTime.now(), Limits.Category.GOODS);
        when(limitRepository.findFirstByCategoryAndSetDateTimeBetweenOrderBySetDateTimeDesc(any(), any(), any()))
                .thenReturn(monthlyLimit);

        System.out.println( monthlyLimit + " " +monthlyLimit.getMonthlyLimitUSD());

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(BigDecimal.valueOf(0.0025));
        when(exchangeRateRepository.findByDateAndBaseCurrencyAndTargetCurrency(any(), any(), any()))
                .thenReturn(exchangeRate);

        transactionDtoKZT.setLimit(monthlyLimit);

        boolean limitExceededKZT = transactionService.saveTransaction(transactionDtoKZT);

        assertFalse(limitExceededKZT);
        assertFalse(transactionDtoKZT.isLimitExceeded());
    }

}

