package kz.zhansaya.solvaMCR.mappers;


import kz.zhansaya.solvaMCR.dtos.LimitDto;
import kz.zhansaya.solvaMCR.entities.Limits;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitMapper {
    LimitDto toDto(Limits limit);
    Limits toEntity(LimitDto limitDto);
}
