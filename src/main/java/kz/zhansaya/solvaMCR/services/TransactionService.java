package kz.zhansaya.solvaMCR.services;

import kz.zhansaya.solvaMCR.dtos.TransactionDto;
import java.util.List;

public interface TransactionService {

    boolean saveTransaction(TransactionDto transactionDto);
    List<TransactionDto> getTransactionsExceedingLimit();
}
