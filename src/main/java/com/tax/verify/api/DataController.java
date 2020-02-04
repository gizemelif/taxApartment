package com.tax.verify.api;
import com.tax.verify.dto.Data;
import com.tax.verify.job.Scheduler;
import com.tax.verify.jpa.*;
import com.tax.verify.jpa.pojo.Queue;
import com.tax.verify.jpa.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.POST,path = "/addQuery")
    public void addRepeatedQuery(){
        service.addRepoQueriedSql();
    }


}
