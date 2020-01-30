package com.tax.verify.jpa;
import com.tax.verify.jpa.pojo.Vd_Tc_Queried;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface RepeatedSqlRepo extends JpaRepository<Vd_Tc_Queried,String> {
    /*@Modifying
    @Query(value = "INSERT INTO QUERIED_VD_TC_SQL (job_oid, created_at, sql_string, state, query_type, notification_mail) VALUES (:job_oid,=current_timestamp, :sqlString, :state,  :queryType, :notification)", nativeQuery = true)
    void insertSql(@Param("sqlString") String sqlString, @Param("state") Enum state, @Param("queryType") String queryType, @Param("notification") String notification);*/

    @Query(value = "SELECT * FROM QUERIED_VD_TC_SQL u WHERE u.state = 'WAITING' ORDER BY u.created_at ASC LIMIT 1", nativeQuery = true)
    Vd_Tc_Queried findByState();

    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE Vd_Tc_Queried s SET s.state =:state, s.notification_mail =:notification_mail, s.start_date = current_timestamp where s.job_oid =:job_oid")
    void updateState(@Param("state")Vd_Tc_Queried.QueriedState state, @Param("notification_mail") String notification_mail, @Param("job_oid") String job_oid);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Vd_Tc_Queried s SET s.state =:state, s.notification_mail =:notification_mail, s.end_date =:end_date where s.job_oid =:job_oid")
    void updateStateProcessed(@Param("state")Vd_Tc_Queried.QueriedState state, @Param("notification_mail") String notification_mail, @Param("end_date") Date end_date, @Param("job_oid") String job_oid);

}
