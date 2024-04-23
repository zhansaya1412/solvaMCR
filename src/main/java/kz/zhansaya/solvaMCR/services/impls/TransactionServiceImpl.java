package kz.zhansaya.solvaMCR.services.impls;

import kz.zhansaya.solvaMCR.dtos.TransactionDto;
import kz.zhansaya.solvaMCR.entities.ExchangeRate;
import kz.zhansaya.solvaMCR.entities.Limits;
import kz.zhansaya.solvaMCR.entities.Transaction;
import kz.zhansaya.solvaMCR.mappers.TransactionMapper;
import kz.zhansaya.solvaMCR.repositories.ExchangeRateRepository;
import kz.zhansaya.solvaMCR.repositories.LimitRepository;
import kz.zhansaya.solvaMCR.repositories.TransactionRepository;
import kz.zhansaya.solvaMCR.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final LimitRepository limitsRepository;
    private final TransactionMapper transactionMapper;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public boolean saveTransaction(TransactionDto transactionDto) {
        try {
            Limits.Category category = transactionDto.getCategory();
            Limits monthlyLimit = getMonthlyLimit(category);
            LocalDateTime dateTimeLimit = monthlyLimit.getSetDateTime();

            BigDecimal transactionAmount = transactionDto.getAmount();
            BigDecimal convertedAmount = transactionAmount;

            LocalDateTime transactionDateTime = transactionDto.getDateTime();
            LocalDate transactionDate = transactionDateTime.toLocalDate();

            if (!transactionDto.getCurrencyShortName().equalsIgnoreCase("USD")) {
                ExchangeRate exchangeRate = exchangeRateRepository.findByDateAndBaseCurrencyAndTargetCurrency(
                        transactionDate, transactionDto.getCurrencyShortName(), "USD");
                if (exchangeRate != null) {
                    convertedAmount = transactionAmount.multiply(exchangeRate.getRate());
                } else {
                    throw new RuntimeException("Exchange rate not found for currency: "
                            + transactionDto.getCurrencyShortName());
                }
            }

            BigDecimal totalAmountThisMonth = calculateTotalAmountThisMonth(category, dateTimeLimit);
            System.out.println("totalAmountThisMonth" + " " + totalAmountThisMonth);
            BigDecimal newTotalAmountThisMonth = totalAmountThisMonth.add(convertedAmount);
            System.out.println("newTotalAmountThisMonth" + " " + newTotalAmountThisMonth);

            boolean limitExceeded = newTotalAmountThisMonth.compareTo(monthlyLimit.getMonthlyLimitUSD()) > 0;

            if (limitExceeded) {
                transactionDto.setLimitExceeded(true);
                log.warn("Transaction limit exceeded for category {}", category);
            } else {
                transactionDto.setLimitExceeded(false);
                log.debug("Transaction saved successfully and not limit exceeded");
            }
            transactionDto.setLimit(monthlyLimit);
            Transaction transaction = transactionMapper.toEntity(transactionDto);
            transactionRepository.save(transaction);

            log.info("Transaction saved successfully");
            return limitExceeded;
        } catch (Exception e) {
            log.error("Failed to save transaction", e);
            throw new RuntimeException("Failed to save transaction: " + e.getMessage());
        }
    }
    private Limits getMonthlyLimit(Limits.Category category) {

        LocalDateTime firstDayOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        Limits limit = limitsRepository.findFirstByCategoryAndSetDateTimeBetweenOrderBySetDateTimeDesc(category, firstDayOfMonth, lastDayOfMonth);

        if (limit == null) {
            limit = new Limits();
            limit.setCategory(category);
            limit.setMonthlyLimitUSD(BigDecimal.valueOf(1000));
            limit.setSetDateTime(LocalDateTime.now());
            limitsRepository.save(limit);
        }
        return limit;
    }

    private BigDecimal calculateTotalAmountThisMonth(Limits.Category category, LocalDateTime limitSetDate) {

        LocalDateTime firstDayOfMonth = limitSetDate.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime lastDayOfMonth = limitSetDate.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        //находим все транзакции в этом месяце по заданной категории
        List<Transaction> transactions = transactionRepository.findByCategoryAndDateTimeBetween(category, firstDayOfMonth, lastDayOfMonth);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            // Проверяем, что транзакция принадлежит категории category
            if (transaction.getCategory() == category) {
                // Проверяем, что сумма транзакции необходимо конвертировать в USD
                if (!transaction.getCurrencyShortName().equalsIgnoreCase("USD")) {
                    ExchangeRate exchangeRate = exchangeRateRepository.findByDateAndBaseCurrencyAndTargetCurrency(
                            transaction.getDateTime().toLocalDate(), transaction.getCurrencyShortName(), "USD");

                    if (exchangeRate != null) {
                        BigDecimal amountInUSD = transaction.getAmount().multiply(exchangeRate.getRate());
                        totalAmount = totalAmount.add(amountInUSD);
                    } else {
                        throw new RuntimeException("Exchange rate not found for currency: " + transaction.getCurrencyShortName());
                    }
                } else {
                    totalAmount = totalAmount.add(transaction.getAmount());
                }
            }
        }
        return totalAmount;
    }

    @Override
    public List<TransactionDto> getTransactionsExceedingLimit() {
        try {
            List<Transaction> exceedingTransactions = transactionRepository.findTransactionsWithLimitExceededAndLimits();
            List<TransactionDto> exceedingTransactionDtos = new ArrayList<>();

            for (Transaction transaction : exceedingTransactions) {
                exceedingTransactionDtos.add(transactionMapper.toDto(transaction));
            }

            return exceedingTransactionDtos;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get transactions with exceeded limit: " + e.getMessage());
        }

    }
}
