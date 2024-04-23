package kz.zhansaya.solvaMCR.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Limits extends BaseEntity{

    @Column(nullable = false)
    private BigDecimal monthlyLimitUSD; //лимит устанавливается в долларах

    @Column(nullable = false)
    private LocalDateTime setDateTime;

    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        GOODS, // товары
        SERVICES // услуги
    }

}
