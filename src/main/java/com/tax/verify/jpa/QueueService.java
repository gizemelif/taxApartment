package com.tax.verify.jpa;

import com.tax.verify.jpa.pojo.Queue;
import com.tax.verify.jpa.pojo.Vd_Tc_Queried;
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
    //Bir kereliğine tabloya eklendi ve her gece çalıştırılması için zamanlandı.
    public void addRepoQueriedSql(String sql, String queryType){
        Vd_Tc_Queried queried = new Vd_Tc_Queried();
        queried.setSql_string(sql);
        queried.setNotification_mail("Queued");
        queried.setState(Vd_Tc_Queried.QueriedState.WAITING);
        queried.setCreated_at( new Date());
        queried.setQuery_type(queryType);
        repeatedSqlRepo.save(queried);
    }

}
