package com.tax.verify.dao;

import com.tax.verify.model.Data;

import java.util.List;
import java.util.Optional;

public interface DataDao {
    boolean insertNewData(Data data);
    boolean insertNewDataByGovernmentNumber(Data data);
    List<Data> selectDataByTaxNumber(String taxNumber, String plaka);
    List<Data> selectDataByGovernmentNumber(String governmentNumber, String plaka);
    List<Data> selectAllDatas();
    boolean updateDataByForTaxNumber(Data newData);
    boolean updateDataByForGovernmentNumber(Data newData);
    int deleteDataById(String oid);
    Optional<Data> selectDataById(String oid);
}
