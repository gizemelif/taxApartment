package com.tax.verify.dao;

import com.tax.verify.model.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataDao {
    //int insertNewData(UUID uuid, Data data);

    int insertNewData(Data data);
    int insertNewDataByGovernmentNumber(Data data);
    Data selectDataByTaxNumber(String taxNumber, String plaka);
    Data selectDataByGovernmentNumber(String governmentNumber, String plaka);
    List<Data> selectAllDatas();
    int updateDataById(UUID id, Data newData);
    int deleteDataById(UUID id);
    Optional<Data> selectDataById(UUID id);
}
