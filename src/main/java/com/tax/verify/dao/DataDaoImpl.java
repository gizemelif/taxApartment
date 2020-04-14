package com.tax.verify.dao;

import com.tax.verify.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DataDaoImpl implements DataDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertNewData(UUID uuid, Data data) {
        return 0;
    }

    @Override
    public Data selectDataByTaxNumber(String taxNumber) {
        return null;
    }

    @Override
    public Data selectDataByGovernmentNumber(String governmentNumber) {
        return null;
    }

    @Override
    public List<Data> selectAllDatas(String taxNumber, String plaka) {
        final String sql = "SELECT * FROM vd_tc_index WHERE vd_sorulan = \'"+ taxNumber
                + "\' AND plaka = \'"+ plaka +"\'";
        List<Data> dataList =jdbcTemplate.query(sql, (resultSet, i) ->{
            return new Data();
        });
        return dataList;
    }

    @Override
    public int updateDataById(UUID id, Data newData) {
        return 0;
    }

    @Override
    public int deleteDataById(UUID id) {
        return 0;
    }

    @Override
    public Optional<Data> selectDataById(UUID id) {
        return Optional.empty();
    }
}
