package com.tax.verify.jpa;

import com.tax.verify.jpa.pojo.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import static com.tax.verify.jpa.pojo.Queue.QueueState.WAITING;

@Service
@Transactional
public class QueueService {
    @Autowired
    private DataRepositoryImp dataRepositoryImp;

    @Autowired
    private IndexRepository dataRepository;

    @Autowired
    private QueueRepo queueRepo;

    public void setQueueRepo(String sql, String queryType){
        Queue queue = new Queue();

        queue.setSql_string(sql);
        queue.setNotification_mail("Queued");
        queue.setState(WAITING);
        queue.setCreated_at( new Date());
        queue.setQueryType(queryType);
        //queue.setIsPlate(isPlate);
        queueRepo.save(queue);
    }
    //Queue'ya üzerinden 1 aydan fazla zaman geçmiş vd ve tc lerin yeniden sorulması için gerekli olan sorgu da kaydedildi.
    //Bir kereliğine tabloya eklendi ve her gece çalıştırılması için zamanlandı.
    public void addRepoVdSql(){
        Queue queue = new Queue();
        String sql = "select * from vd_tc_index where vd_sorulan is not null and vd_fiili_durum_donen = 'FAAL' or vd_fiili_durum_donen = 'TERK' and lastupdated < now() - INTERVAL '1 month' limit 10000";
        String queryType = "vd";
        queue.setSql_string(sql);
        queue.setNotification_mail("Queued");
        queue.setState(WAITING);
        queue.setCreated_at( new Date());
        queue.setQueryType(queryType);
        queueRepo.save(queue);
    }
    public void addRepoTcSql(){
        Queue queue = new Queue();

        String sql = "select * from vd_tc_index where tc_sorulan is not null and tc_fiili_durum_donen = 'FAAL' or tc_fiili_durum_donen = 'TERK' and lastupdated < now() - INTERVAL '1 month' limit 10000";
        String queryType = "tc";
        queue.setSql_string(sql);
        queue.setNotification_mail("Queued");
        queue.setState(WAITING);
        queue.setCreated_at( new Date());
        queue.setQueryType(queryType);
        queueRepo.save(queue);
    }
}
