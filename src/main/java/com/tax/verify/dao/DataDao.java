package com.tax.verify.dao;

import com.tax.verify.model.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataDao {
    int insertNewData(UUID uuid, Data data);
    Data selectDataByTaxNumber(String taxNumber);
    Data selectDataByGovernmentNumber(String governmentNumber);
    List<Data> selectAllDatas(String taxNumber, String plaka);
    int updateDataById(UUID id, Data newData);
    int deleteDataById(UUID id);
    Optional<Data> selectDataById(UUID id);
}
