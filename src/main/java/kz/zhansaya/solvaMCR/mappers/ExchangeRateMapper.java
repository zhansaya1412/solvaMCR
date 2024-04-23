package kz.zhansaya.solvaMCR.mappers;


import kz.zhansaya.solvaMCR.dtos.ExchangeRateDto;
import kz.zhansaya.solvaMCR.entities.ExchangeRate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
    ExchangeRateDto toDto(ExchangeRate exchangeRate);
    ExchangeRate toEntity(ExchangeRateDto exchangeRateDto);
}
