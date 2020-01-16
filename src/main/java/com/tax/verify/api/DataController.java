package com.tax.verify.api;
import com.tax.verify.dto.Data;
import com.tax.verify.job.Scheduler;
import com.tax.verify.jpa.*;
import com.tax.verify.jpa.pojo.Queue;
import com.tax.verify.jpa.QueueService;
import com.tax.verify.jpa.pojo.Vd_Tc_Queried;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tax.verify.jpa.pojo.Vd_Tc_Queried.state.WAITING;

@RestController
@RequestMapping("api")
public class DataController {
    @Autowired
    private DataRepositoryImp dataRepositoryImp;
    @Autowired
    private IndexRepository dataRepository;
    @Autowired
    private RepeatedSqlRepo repo;

    @Autowired
    private Queue queue;

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    QueueService service;

    //Bu servis gelen SQL string leri alır ve Queue tablosuna set eder.
    @GetMapping("/datas")
    @ResponseBody
    public void all( @RequestParam String sql, @RequestParam String queryType ) {

        service.setQueueRepo(sql, queryType);
    }

    @PostMapping("/addQuery")
    public void addRepeatedQuery(@RequestParam String sqlString,
                                 @RequestParam String queryType,
                                 @RequestParam String notification){
        Vd_Tc_Queried vdTcQueried = new Vd_Tc_Queried();
        Enum status = WAITING;
        repo.insertSql(sqlString,status,queryType,notification);
    }


}
