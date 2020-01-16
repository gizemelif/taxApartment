package com.tax.verify.jpa;

import com.tax.verify.jpa.pojo.Vd_Tc_Queried;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RepeatedSqlRepo extends JpaRepository<Vd_Tc_Queried,String> {
    @Modifying
    @Query(value = "INSERT INTO QUERIED_VD_TC_SQL (job_oid, sql_string, state, query_type, notification_mail) VALUES (NEWID(), :sqlString, :state,  :queryType, :notification)", nativeQuery = true)
    void insertSql(@Param("sqlString") String sqlString, @Param("state") Enum state, @Param("queryType") String queryType, @Param("notification") String notification);

}
