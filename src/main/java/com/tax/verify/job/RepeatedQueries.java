package com.tax.verify.job;

import com.tax.verify.dao.DataRepositoryImp;
import com.tax.verify.service.QueueService;
import com.tax.verify.dao.RepeatedSqlRepo;
import com.tax.verify.model.Vd_Tc_Queried;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RepeatedQueries {
    private Vd_Tc_Queried vd_tc_queried = new Vd_Tc_Queried();
    private static Logger LOGGER = LoggerFactory.getLogger(RepeatedQueries.class);

    @Autowired
    QueueService queueService;

    @Autowired
    RepeatedSqlRepo repeatedSqlRepo;

    @Autowired
    DataRepositoryImp dataRepositoryImp;

    public Vd_Tc_Queried findByQuery() {

        return repeatedSqlRepo.findByState();
    }

    public void addRepeatedTcSql(){
        queueService.addRepoQueriedSqlTc();
    }

    public void addRepeatedVdSql(){
        queueService.addRepoQueriedSql();
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduledVd(){
        vd_tc_queried = findByQuery();

        //addRepeatedVdSql();
        //addRepeatedTcSql();

        if (vd_tc_queried == null) {
            //addRepeatedVdSql();
            return;
        }
        try {
            if(vd_tc_queried != null && (vd_tc_queried.getQuery_type().equals("vd") || vd_tc_queried.getQuery_type().equals("VD"))){
                repeatedSqlRepo.updateState(Vd_Tc_Queried.QueriedState.PROCESSING, "Process is starting...", vd_tc_queried.getJob_oid());
                dataRepositoryImp.updateVknTable(vd_tc_queried.getSql_string());

                repeatedSqlRepo.updateStateProcessed(Vd_Tc_Queried.QueriedState.PROCESSED, "Process is completed", vd_tc_queried.getEnd_date(), vd_tc_queried.getJob_oid());

            }else if(vd_tc_queried != null && (vd_tc_queried.getQuery_type().equals("tc") || vd_tc_queried.getQuery_type().equals("TC"))) {

                repeatedSqlRepo.updateState(Vd_Tc_Queried.QueriedState.PROCESSING, "Process is starting...", vd_tc_queried.getJob_oid());
                dataRepositoryImp.updateTable(vd_tc_queried.getSql_string());

                repeatedSqlRepo.updateStateProcessed(Vd_Tc_Queried.QueriedState.PROCESSED, "Process is completed", vd_tc_queried.getEnd_date(), vd_tc_queried.getJob_oid());

            }
            return;

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

}
