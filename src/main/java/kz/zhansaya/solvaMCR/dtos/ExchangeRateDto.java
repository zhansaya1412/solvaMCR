package kz.zhansaya.solvaMCR.dtos;

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
public class ExchangeRateDto {
    private Long id;
    private String baseCurrency;
    private String targetCurrency;
    private LocalDate date;
    private BigDecimal rate;

    public ExchangeRateDto(String baseCurrency, String targetCurrency, LocalDate date, BigDecimal rate){
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.date = date;
        this.rate = rate;
    }
}
