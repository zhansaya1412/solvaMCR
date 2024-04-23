package kz.zhansaya.solvaMCR.services;

import kz.zhansaya.solvaMCR.dtos.LimitDto;
import kz.zhansaya.solvaMCR.entities.Limits;
import kz.zhansaya.solvaMCR.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface LimitService {

    void setNewLimit(Limits.Category category, BigDecimal newLimit);
    List<LimitDto> getAllLimits();
}
