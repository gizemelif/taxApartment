package com.tax.verify.dao;

import com.tax.verify.model.Data;
import com.tax.verify.job.RepeatedQueries;
import com.tax.verify.job.Scheduler;
import com.tax.verify.model.Queue;
import com.tax.verify.service.JsonObjectMapper;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class DataRepositoryImp {
    private final DataDaoImpl dataDaoImpl;
    private final GetHttpResponse getHttpResponse;
    private final JsonObjectMapper jsonObjectMapper;

    @Autowired
    public DataRepositoryImp(DataDaoImpl dataDaoImpl, GetHttpResponse getHttpResponse, JsonObjectMapper jsonObjectMapper){
        this.dataDaoImpl = dataDaoImpl;
        this.getHttpResponse = getHttpResponse;
        this.jsonObjectMapper = jsonObjectMapper;
    }

    @PersistenceContext
    EntityManager em;

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Scheduler scheduler;

    @Autowired
    RepeatedQueries queries;

    @Autowired
    Queue queue;

   public List<Data> getSqlQuery(String sql) {
        Session session = em.unwrap(Session.class);
        SQLQuery s = session.createSQLQuery(sql);
        s.addEntity(Data.class);
        return s.list();
    }

    public void updateVknTable(String sql) {
        try{

            List<Data> newList = getSqlQuery(sql);

            newList.parallelStream().forEach( d ->{
                try {
                    List<Data> list_for_parallel = new ArrayList<>();
                    list_for_parallel.add(d);
                    try{
                        Data respData = new Data();
                        for(int i=0; i < list_for_parallel.size(); i++){
                            if(d.getPlaka() == null || d.getPlaka().length()==0){
                                respData = getHttpResponse.getResponseVknNullPlate(list_for_parallel).get(i);
                            }else{
                                respData = getHttpResponse.getResponseVkn(list_for_parallel).get(i);
                            }
                        }
                        /*ındexRepository.updateVkn(respData.getVd_vkn(),respData.getVd_unvan_donen(),
                                respData.getVd_vdkodu(), respData.getVd_tc_donen(), respData.getVd_fiili_durum_donen(),
                                respData.getPlaka(),respData.getOid(), respData.getVd_tum_il_na(), respData.getVd_adres_donen(),
                                respData.getNacekoduaciklama_vd(), respData.getIsebaslamatarihi_vd(), respData.getMatrah_vd(),
                                respData.getTahakkukeden_vd(), respData.getYil_vd());*/
                        dataDaoImpl.updateDataByForTaxNumber(respData);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateTable(String sql) {
        try{

            List<Data> newList = getSqlQuery(sql);

            newList.parallelStream().forEach( d ->{
                try {
                    List<Data> list_for_parallel = new ArrayList<>();
                    list_for_parallel.add(d);
                    try{
                        Data respData = new Data();
                        for(int i=0; i < list_for_parallel.size(); i++){

                            if( d.getPlaka() == null || d.getPlaka().length()==0){
                                respData = getHttpResponse.getResponseNullPlate(list_for_parallel).get(i);
                            }else{
                                respData = getHttpResponse.getResponse(list_for_parallel).get(i);
                            }
                        }
                        /*ındexRepository.update(respData.getTckn(),respData.getUnvan(),respData.getVdkodu(),
                                respData.getVkn(),respData.getDurum_text(), respData.getPlaka(),respData.getOid(),
                                respData.getTc_tum_il_na(), respData.getTc_adres_donen(), respData.getNacekoduaciklama(),
                                respData.getIsebaslamatarihi(), respData.getMatrah(),respData.getTahakkukeden(),
                                respData.getYil());*/
                        dataDaoImpl.updateDataByForGovernmentNumber(respData);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void printQueues(){
        List<Queue> queues = em.createQuery("SELECT q FROM Queue q").getResultList();
        queues.stream().forEach(q -> System.out.println(q.getSql_string()));
    }

    public void updateWithGovernmentFromRita(Data data, String responseString, String plate){
      Data tempData = new Data();
       try{
           if(data != null){
               try{
                   Data respData = new Data();
                   respData = JsonObjectMapper.jsonMapperTc(data, responseString, plate);
                   /*ındexRepository.update(respData.getTckn(),respData.getUnvan(),respData.getVdkodu(),
                           respData.getVkn(),respData.getDurum_text(), respData.getPlaka(),respData.getOid(),
                           respData.getTc_tum_il_na(), respData.getTc_adres_donen(), respData.getNacekoduaciklama(),
                           respData.getIsebaslamatarihi(), respData.getMatrah(),respData.getTahakkukeden(),
                           respData.getYil());*/
                   dataDaoImpl.updateDataByForGovernmentNumber(respData);
               }catch (Exception e){
                   e.printStackTrace();
               }
           }else {
               tempData = JsonObjectMapper.jsonMapperTc(data, responseString, plate);

               dataDaoImpl.insertNewDataByGovernmentNumber(tempData);
           }

       }catch (Exception e){e.printStackTrace();}

    }
    public void updateWithTaxNumberFromRita(Data data, String responseString, String plate) {

       try {
           if (data != null) {
               try {
                   Data respData = new Data();
                   respData = JsonObjectMapper.jsonMapperVD(data, responseString, plate);

                   dataDaoImpl.updateDataByForTaxNumber(respData);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           } else {
               Data tempData = new Data();
               tempData = JsonObjectMapper.jsonMapperVD(data, responseString, plate);

               dataDaoImpl.insertNewData(tempData);
           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }

}
