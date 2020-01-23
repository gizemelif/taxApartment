package com.tax.verify.jpa;

import com.tax.verify.dto.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

@Repository
@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
public interface IndexRepository extends JpaRepository<Data, String> {
    //Tckn ile yapılan sorgudan donen değerleri update eder.

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Data c SET c.tckn =:tckn, c.unvan=:unvan, c.vdkodu=:vdkodu, c.vkn=:vkn, c.durum_text=:durum_text, plaka=:plaka, lastupdated=current_timestamp, tc_tum_il_na =:tc_tum_il_na, adres=:adres, faaliyet_aciklama=:faaliyet_aciklama, ise_baslama_tarihi=:ise_baslama_tarihi, matrah=:matrah, tahakkuk_eden=:tahakkuk_eden, yil=:yil where c.oid=:oid")
    void update(@Param("tckn") String tckn,@Param("unvan") String unvan,@Param("vdkodu") String vdkodu, @Param("vkn") String vkn,
                @Param("durum_text") String durum_text, @Param("plaka") String plaka, @Param("oid") String oid,
                @Param("tc_tum_il_na") Long tc_tum_il_na, @Param("adres") String adres,
                @Param("faaliyet_aciklama") String faaliyet_aciklama, @Param("ise_baslama_tarihi") String ise_baslama_tarihi,
                @Param("matrah") String matrah, @Param("tahakkuk_eden") String tahakkuk_eden, @Param("yil") String yil);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Data c SET c.vd_vkn =:vd_vkn, c.vd_unvan_donen=:vd_unvan_donen, c.vd_vdkodu=:vd_vdkodu, c.vd_tc_donen=:vd_tc_donen, c.vd_fiili_durum_donen=:vd_fiili_durum_donen, plaka=:plaka,lastupdated_vd=current_timestamp, vd_tum_il_na =:vd_tum_il_na, adres=:adres, faaliyet_aciklama=:faaliyet_aciklama, ise_baslama_tarihi=:ise_baslama_tarihi, matrah=:matrah, tahakkuk_eden=:tahakkuk_eden, yil=:yil where c.oid=:oid")
    void updateVkn(@Param("vd_vkn") String vd_vkn, @Param("vd_unvan_donen") String vd_unvan_donen, @Param("vd_vdkodu") String vd_vdkodu,
                   @Param("vd_tc_donen") String vd_tc_donen, @Param("vd_fiili_durum_donen") String vd_fiili_durum_donen,
                   @Param("plaka") String plaka, @Param("oid") String oid, @Param("vd_tum_il_na") Long vd_tum_il_na,
                   @Param("adres") String adres, @Param("faaliyet_aciklama") String faaliyet_aciklama,
                   @Param("ise_baslama_tarihi") String ise_baslama_tarihi, @Param("matrah") String matrah,
                   @Param("tahakkuk_eden") String tahakkuk_eden, @Param("yil") String yil);

}