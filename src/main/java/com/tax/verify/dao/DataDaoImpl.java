package com.tax.verify.dao;

import com.tax.verify.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class DataDaoImpl implements DataDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean insertNewData(Data data) {
        boolean isInserted = false;
        try{
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

            jdbcTemplate.update(
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
            isInserted = true;
            return isInserted;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertNewDataByGovernmentNumber(Data data) {
        boolean isInserted = false;
        try{
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

            jdbcTemplate.update(
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
            isInserted = true;
            return isInserted;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Data> selectDataByTaxNumber(String taxNumber, String plaka) {
        //final String sql = "SELECT * FROM vd_tc_index WHERE vd_sorulan = ? AND plaka = ?";
        final String sql = "SELECT * FROM vd_tc_index WHERE vd_sorulan = ? AND (plaka = ? or plaka is null)";
        try{
            return jdbcTemplate.query(
                    sql,
                    new Object[]{taxNumber, plaka}
                    , (resultSet, i) ->{
                        String oid = resultSet.getString("oid");
                        return new Data(oid);
                    });
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Data> selectDataByGovernmentNumber(String governmentNumber, String plaka) {
        //final String sql = "SELECT * FROM vd_tc_index WHERE tc_sorulan = ? AND plaka = ?";
        final String sql = "SELECT * FROM vd_tc_index WHERE tc_sorulan = ? AND (plaka = ? or plaka is null)";
        try{
            return jdbcTemplate.query(
                    sql,
                    new Object[]{governmentNumber, plaka}
                    , (resultSet, i) -> {
                        String oid = resultSet.getString("oid");
                        String plate = resultSet.getString("plaka");
                        return new Data(oid, plate);
                    });
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
    public boolean updateDataByForTaxNumber(Data newData) {
        boolean isUpdated = false;
        try {
            final String sql = "UPDATE vd_tc_index SET" +
                    " vd_sorulan = ?," +
                    " vd_unvan_donen = ?," +
                    " vd_vergi_dairesi_kodu = ?," +
                    " vd_tc_donen = ?," +
                    " vd_fiili_durum_donen = ?," +
                    " plaka = ?," +
                    " lastupdated_vd = CURRENT_TIMESTAMP," +
                    " vd_tum_il_na = ?," +
                    " vd_adres_donen = ?," +
                    " faaliyet_aciklama_vd = ?," +
                    " ise_baslama_tarihi_vd = ?," +
                    " matrah_vd = ?," +
                    " tahakkuk_eden_vd = ?," +
                    " yil_vd = ?" +
                    " WHERE oid = ?";
            jdbcTemplate.update(
                    sql,
                    newData.getVd_vkn(),
                    newData.getVd_unvan_donen(),
                    newData.getVd_vdkodu(),
                    newData.getVd_tc_donen(),
                    newData.getVd_fiili_durum_donen(),
                    newData.getPlaka(),
                    newData.getVd_tum_il_na(),
                    newData.getVd_adres_donen(),
                    newData.getNacekoduaciklama_vd(),
                    newData.getIsebaslamatarihi_vd(),
                    newData.getMatrah_vd(),
                    newData.getTahakkukeden_vd(),
                    newData.getYil_vd(),
                    newData.getOid()
            );
            isUpdated = true;
            return isUpdated;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateDataByForGovernmentNumber(Data newData) {
        boolean isUpdated = false;
        try {
            final String sql = "UPDATE vd_tc_index SET" +
                    " tc_sorulan = ?," +
                    " tc_unvan_donen = ?," +
                    " tc_vergi_dairesi_kodu = ?," +
                    " tc_vd_donen = ?," +
                    " tc_fiili_durum_donen = ?," +
                    " plaka = ?," +
                    " lastupdated = CURRENT_TIMESTAMP," +
                    " tc_tum_il_na = ?," +
                    " tc_adres_donen = ?," +
                    " faaliyet_aciklama = ?," +
                    " ise_baslama_tarihi = ?," +
                    " matrah = ?," +
                    " tahakkuk_eden = ?," +
                    " yil = ?" +
                    " WHERE oid = ?";
            jdbcTemplate.update(
                    sql,
                    newData.getTckn(),
                    newData.getUnvan(),
                    newData.getVdkodu(),
                    newData.getVkn(),
                    newData.getDurum_text(),
                    newData.getPlaka(),
                    newData.getTc_tum_il_na(),
                    newData.getTc_adres_donen(),
                    newData.getNacekoduaciklama(),
                    newData.getIsebaslamatarihi(),
                    newData.getMatrah(),
                    newData.getTahakkukeden(),
                    newData.getYil(),
                    newData.getOid()
            );
            isUpdated = true;
            return isUpdated;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteDataById(String oid) {
        return 0;
    }

    @Override
    public Optional<Data> selectDataById(String oid) {
        return Optional.empty();
    }
}
