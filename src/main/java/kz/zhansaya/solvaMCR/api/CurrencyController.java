package kz.zhansaya.solvaMCR.api;

import kz.zhansaya.solvaMCR.services.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private final ExchangeRateService currencyService;

    public CurrencyController(ExchangeRateService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/update-daily-rates")
    public ResponseEntity<String> updateDailyRates() {
        try {
            currencyService.updateDailyRates();
            return ResponseEntity.ok("Daily rates update scheduled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update daily rates: " + e.getMessage());
        }
    }
}

