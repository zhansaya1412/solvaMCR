package kz.zhansaya.solvaMCR.repositories;

import jakarta.transaction.Transactional;
import kz.zhansaya.solvaMCR.entities.Limits;
import kz.zhansaya.solvaMCR.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategoryAndDateTimeBetween(Limits.Category category, LocalDateTime startDate,
                                                       LocalDateTime endDate);

    List<Transaction> findByCategoryAndDateTimeAfter(Limits.Category category, LocalDateTime startDate);

    @Query("SELECT t " +
            "FROM Transaction t " +
            "JOIN FETCH t.limit l " + // Присоединяем связанный объект Limits
            "WHERE t.limitExceeded = true")
    List<Transaction> findTransactionsWithLimitExceededAndLimits();

}
