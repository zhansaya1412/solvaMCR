package kz.zhansaya.solvaMCR.api;

import kz.zhansaya.solvaMCR.dtos.LimitDto;
import kz.zhansaya.solvaMCR.dtos.TransactionDto;
import kz.zhansaya.solvaMCR.services.LimitService;
import kz.zhansaya.solvaMCR.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final TransactionService transactionService;
    private final LimitService limitService;


    @GetMapping("/transactions/limit-exceeded")
    public ResponseEntity<?> getTransactionsWithLimitExceeded() {
        try {
            List<TransactionDto> transactions = transactionService.getTransactionsExceedingLimit();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get transactions with "
                    + "exceeded limit: " + e.getMessage());
        }
    }

    @PostMapping("/limit")
    public ResponseEntity<?> setNewLimit(@RequestBody LimitDto limitRequest) {
        try {
            limitService.setNewLimit(limitRequest.getCategory(), limitRequest.getMonthlyLimitUSD());
            return ResponseEntity.ok("New Limit saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Failed to set new limit: " + e.getMessage());
        }
    }

    @GetMapping("/limits")
    public ResponseEntity<?> getAllLimits() {
        try {
            List<LimitDto> limits = limitService.getAllLimits();
            return ResponseEntity.ok(limits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Failed to get all limits: " + e.getMessage());
        }
    }
}
