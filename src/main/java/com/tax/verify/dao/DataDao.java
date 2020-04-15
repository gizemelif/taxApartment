package com.tax.verify.dao;

import com.tax.verify.model.Data;

import java.util.List;
import java.util.Optional;

public interface DataDao {
    int insertNewData(Data data);
    int insertNewDataByGovernmentNumber(Data data);
    Data selectDataByTaxNumber(String taxNumber, String plaka);
    Data selectDataByGovernmentNumber(String governmentNumber, String plaka);
    List<Data> selectAllDatas();
    int updateDataByForTaxNumber(Data newData);
    int updateDataByForGovernmentNumber(Data newData);
    int deleteDataById(String oid);
    Optional<Data> selectDataById(String oid);
}
