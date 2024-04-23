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
public class Transaction extends BaseEntity{
    @Column(nullable = false)
    private String accountFrom;

    @Column(nullable = false)
    private String accountTo;

    @Column(nullable = false)
    private String currencyShortName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private boolean limitExceeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Limits.Category category;

    @ManyToOne
    @JoinColumn(name = "limit_id")
    private Limits limit;
}
