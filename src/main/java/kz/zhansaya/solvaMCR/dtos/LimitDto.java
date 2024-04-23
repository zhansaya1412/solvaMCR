package kz.zhansaya.solvaMCR.dtos;

import kz.zhansaya.solvaMCR.entities.Limits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LimitDto {
    private Long id;
    private BigDecimal monthlyLimitUSD;
    private LocalDate setDateTime;
    private Limits.Category category;
}
