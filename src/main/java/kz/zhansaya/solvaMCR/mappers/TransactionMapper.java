package kz.zhansaya.solvaMCR.mappers;

import kz.zhansaya.solvaMCR.dtos.TransactionDto;
import kz.zhansaya.solvaMCR.entities.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction transaction);
    Transaction toEntity(TransactionDto transactionDto);
}
