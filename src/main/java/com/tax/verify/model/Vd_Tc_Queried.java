package com.tax.verify.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Entity
@Table(name = "QUERIED_VD_TC_SQL")
@EntityListeners(AuditingEntityListener.class)
public class Vd_Tc_Queried {
    public Vd_Tc_Queried(){}

    public Vd_Tc_Queried(String query_type, String job_oid, Date created_at, String notification_mail, String sql_string) {
        this.query_type = query_type;
        this.job_oid = job_oid;
        this.created_at = created_at;
        this.notification_mail = notification_mail;
        this.sql_string = sql_string;
    }
    public enum QueriedState {
        WAITING, PROCESSING, PROCESSED
    }
    @Column(name = "QUERY_TYPE")
    private String query_type;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "JOB_OID")
    private String job_oid;

    @Column(name = "CREATED_AT")
    @CreatedDate
    private Date created_at;

    @Column(name = "END_DATE")
    private Date end_date;

    @Column(name = "NOTIFICATION_MAIL")
    private String notification_mail;

    @Column(name = "SQL_STRING")
    private String sql_string;

    @Column(name = "START_DATE")
    private Date start_date;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private QueriedState state;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public QueriedState getState() {
        return this.state;
    }

    public void setState(QueriedState state) {
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

    public String getQuery_type() {
        return query_type;
    }
    public void setQuery_type(String query_type) {
        this.query_type = query_type;
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
