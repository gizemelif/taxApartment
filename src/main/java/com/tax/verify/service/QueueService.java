package com.tax.verify.service;

import com.tax.verify.dao.DataRepositoryImp;
import com.tax.verify.dao.QueueRepo;
import com.tax.verify.dao.RepeatedSqlRepo;
import com.tax.verify.model.Queue;
import com.tax.verify.model.Vd_Tc_Queried;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import static com.tax.verify.model.Queue.QueueState.WAITING;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Component
public class QueueService {
    @Autowired
    private DataRepositoryImp dataRepositoryImp;

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    private RepeatedSqlRepo repeatedSqlRepo;

    public void setQueueRepo(String sql, String queryType){
        Queue queue = new Queue();

        queue.setSql_string(sql);
        queue.setNotification_mail("Queued");
        queue.setState(WAITING);
        queue.setCreated_at( new Date());
        queue.setQueryType(queryType);
        queueRepo.save(queue);
    }
    //Queue'ya üzerinden 1 aydan fazla zaman geçmiş vd ve tc lerin yeniden sorulması için gerekli olan sorgu da kaydedildi.
    public void addRepoQueriedSql(){
        Vd_Tc_Queried queried = new Vd_Tc_Queried();
        String sql = "select * from vd_tc_index vti where vd_sorulan is not null and length(vd_sorulan)>0 and  plaka is not null and vd_adres_donen is null and \n" +
                "vd_fiili_durum_donen='FAAL' and lastupdated_vd<=date_trunc('day', current_timestamp - interval '1 month') order by plaka limit 10000";
        String queryType = "vd";
        queried.setSql_string(sql);
        queried.setNotification_mail("Queued");
        queried.setState(Vd_Tc_Queried.QueriedState.WAITING);
        queried.setCreated_at( new Date());
        queried.setQuery_type(queryType);
        repeatedSqlRepo.save(queried);
    }
    public void addRepoQueriedSqlTc(){
        Vd_Tc_Queried queried = new Vd_Tc_Queried();
        String sql = "select * from vd_tc_index vti where tc_sorulan is not null and length(tc_sorulan)>0 and plaka is not null and tc_fiili_durum_donen='FAAL' and lastupdated<=date_trunc('day', current_timestamp - interval '1 month') order by plaka limit 10000";
        String queryType = "tc";
        queried.setSql_string(sql);
        queried.setNotification_mail("Queued");
        queried.setState(Vd_Tc_Queried.QueriedState.WAITING);
        queried.setCreated_at( new Date());
        queried.setQuery_type(queryType);
        repeatedSqlRepo.save(queried);
    }

    public void addRepoQueriedSqlVdTc(String sqlString, String queryType){
        Vd_Tc_Queried queried = new Vd_Tc_Queried();
        queried.setSql_string(sqlString);
        queried.setQuery_type(queryType);
        queried.setNotification_mail("Queued");
        queried.setState(Vd_Tc_Queried.QueriedState.WAITING);
        queried.setCreated_at( new Date());
        queried.setQuery_type(queryType);
        repeatedSqlRepo.save(queried);
    }

}
