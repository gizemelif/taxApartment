package com.tax.verify.dao;

import com.tax.verify.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public int insertNewData(Data data) {
        final String sql = "INSERT INTO vd_tc_index ("+
                " oid, "+
                " vd_sorulan, "+
                " vd_unvan_donen, "+
                " vd_adres_donen, "+
                " vd_vergi_dairesi_kodu, "+
                " vd_tc_donen, "+
                " vd_fiili_durum_donen, "+
                " vd_tum_il_na, "+
                " lastupdated_vd, "+
                " plaka, "+
                " ise_baslama_tarihi_vd, "+
                " matrah_vd, "+
                " faaliyet_aciklama_vd, "+
                " tahakkuk_eden_vd, "+
                " yil_vd) "+
                "VALUES (sys_guid(), ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                data.getVd_vkn(),
                data.getVd_unvan_donen(),
                data.getVd_adres_donen(),
                data.getVd_vdkodu(),
                data.getVd_tc_donen(),
                data.getVd_fiili_durum_donen(),
                data.getVd_tum_il_na(),
                data.getPlaka(),
                data.getIsebaslamatarihi_vd(),
                data.getMatrah_vd(),
                data.getNacekoduaciklama_vd(),
                data.getTahakkukeden_vd(),
                data.getYil_vd()
        );
    }

    @Override
    public int insertNewDataByGovernmentNumber(Data data) {
        final String sql = "INSERT INTO vd_tc_index ("+
                " oid, "+
                " tc_sorulan, "+
                " tc_unvan_donen, "+
                " tc_adres_donen, "+
                " tc_vergi_dairesi_kodu, "+
                " tc_vd_donen, "+
                " tc_fiili_durum_donen, "+
                " tc_tum_il_na, "+
                " lastupdated, "+
                " plaka, "+
                " ise_baslama_tarihi, "+
                " matrah, "+
                " faaliyet_aciklama, "+
                " tahakkuk_eden, "+
                " yil) "+
                "VALUES (sys_guid(), ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                data.getTckn(),
                data.getUnvan(),
                data.getTc_adres_donen(),
                data.getVdkodu(),
                data.getVkn(),
                data.getDurum_text(),
                data.getTc_tum_il_na(),
                data.getPlaka(),
                data.getIsebaslamatarihi(),
                data.getMatrah(),
                data.getNacekoduaciklama(),
                data.getTahakkukeden(),
                data.getYil()
        );
    }

    @Override
    public Data selectDataByTaxNumber(String taxNumber, String plaka) {
        final String sql = "SELECT * FROM vd_tc_index WHERE vd_sorulan = ? AND plaka = ?";
        try{
            Data data = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{taxNumber, plaka}
                    , (resultSet, i) -> {
                    return new Data();
            });
            return data;
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Data selectDataByGovernmentNumber(String governmentNumber, String plaka) {
        final String sql = "SELECT * FROM vd_tc_index WHERE tc_sorulan = ? AND plaka = ?";
        try{
            Data data = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{governmentNumber, plaka}
                    , (resultSet, i) -> {
                        return new Data();
                    });
            return data;
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Data> selectAllDatas() {
        final String sql = "SELECT * FROM vd_tc_index";
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
