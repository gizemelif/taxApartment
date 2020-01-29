package com.tax.verify.jpa.pojo;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Component
@Entity
@Table(name = "QUERIED_VD_TC_SQL")
@EntityListeners(AuditingEntityListener.class)
public class Vd_Tc_Queried {
    public Vd_Tc_Queried(){}

    public Vd_Tc_Queried(String queryType, String job_oid, Date start_date, Date end_date, String notification_mail, String sql_string, Vd_Tc_Queried.state state) {
        this.queryType = queryType;
        this.job_oid = job_oid;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notification_mail = notification_mail;
        this.sql_string = sql_string;
        this.state = state;
    }

    public Vd_Tc_Queried(String sql_string) {
        this.sql_string = sql_string;
    }

    public enum state {
        WAITING, PROCESSING, PROCESSED
    }
    @Column(name = "QUERY_TYPE")
    private String queryType;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Generated(GenerationTime.INSERT)
    @Column(name = "JOB_OID")
    private String job_oid;

    @Column(name = "CREATED_AT")
    @Generated(GenerationTime.INSERT)
    private Date created_at;

    @Column(name = "START_DATE")
    private Date start_date;

    @Column(name = "END_DATE")
    private Date end_date;

    @Column(name = "NOTIFICATION_MAIL")

    private String notification_mail;

    @Column(name = "SQL_STRING")
    private String sql_string;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private Vd_Tc_Queried.state state;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Vd_Tc_Queried.state getState() {
        return state;
    }

    public void setState(Vd_Tc_Queried.state state) {
        this.state = state;
    }

    public String getNotification_mail() {
        return notification_mail;
    }

    public void setNotification_mail(String notification_mail) {
        this.notification_mail = notification_mail;
    }

    public String getSql_string() {
        return sql_string;
    }

    public void setSql_string(String sql_string) {
        this.sql_string = sql_string;
    }

    public String getJob_oid() {
        return job_oid;
    }

    public void setJob_oid(String job_oid) {
        this.job_oid = job_oid;
    }

    public String getQueryType() {
        return queryType;
    }
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public Date getStart_date() {
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        this.end_date = new Date();
        return this.end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }


}
