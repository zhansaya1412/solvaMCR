package kz.zhansaya.solvaMCR.dtos;

import kz.zhansaya.solvaMCR.entities.Limits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String accountFrom;
    private String accountTo;
    private String currencyShortName;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private boolean limitExceeded;
    private Limits.Category category;
    private Limits limit;
}
