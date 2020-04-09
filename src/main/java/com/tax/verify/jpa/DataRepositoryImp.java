package com.tax.verify.jpa;

import com.tax.verify.dto.Data;
import com.tax.verify.job.RepeatedQueries;
import com.tax.verify.job.Scheduler;
import com.tax.verify.jpa.pojo.Queue;
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
    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataRepositoryImp dataRepositoryImp;

    @Autowired
    private IndexRepository ındexRepository;

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

    @Autowired
    private JsonObjectMapper jsonObjectMapper;

   public List<Data> getSqlQuery(String sql) {
        Session session = em.unwrap(Session.class);
        SQLQuery s = session.createSQLQuery(sql);
        s.addEntity(Data.class);
        return s.list();
    }

    public void updateVknTable(String sql) {
        try{

            List<Data> newList = getSqlQuery(sql);
            GetHttpResponse getHttpResponse = new GetHttpResponse();

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
                        ındexRepository.updateVkn(respData.getVd_vkn(),respData.getVd_unvan_donen(),
                                respData.getVd_vdkodu(), respData.getVd_tc_donen(), respData.getVd_fiili_durum_donen(),
                                respData.getPlaka(),respData.getOid(), respData.getVd_tum_il_na(), respData.getVd_adres_donen(),
                                respData.getNacekoduaciklama_vd(), respData.getIsebaslamatarihi_vd(), respData.getMatrah_vd(),
                                respData.getTahakkukeden_vd(), respData.getYil_vd());
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
            GetHttpResponse getHttpResponse = new GetHttpResponse();

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
                        ındexRepository.update(respData.getTckn(),respData.getUnvan(),respData.getVdkodu(),
                                respData.getVkn(),respData.getDurum_text(), respData.getPlaka(),respData.getOid(),
                                respData.getTc_tum_il_na(), respData.getTc_adres_donen(), respData.getNacekoduaciklama(),
                                respData.getIsebaslamatarihi(), respData.getMatrah(),respData.getTahakkukeden(),
                                respData.getYil());
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

    public void updateWithGovernmentFromRita(List<Data> newList, String responseString){
      Data tempData = new Data();
       try{
           if(newList.size()>0){
               newList.parallelStream().forEach( d ->{
                   try {
                       List<Data> list_for_parallel = new ArrayList<>();
                       list_for_parallel.add(d);
                       try{
                           Data respData = new Data();
                           for(int i=0; i < list_for_parallel.size(); i++){
                               respData = JsonObjectMapper.jsonMapperTc(list_for_parallel, responseString);
                           }
                           ındexRepository.update(respData.getTckn(),respData.getUnvan(),respData.getVdkodu(),
                                   respData.getVkn(),respData.getDurum_text(), respData.getPlaka(),respData.getOid(),
                                   respData.getTc_tum_il_na(), respData.getTc_adres_donen(), respData.getNacekoduaciklama(),
                                   respData.getIsebaslamatarihi(), respData.getMatrah(),respData.getTahakkukeden(),
                                   respData.getYil());
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
           tempData = JsonObjectMapper.jsonMapperTc(newList, responseString);
           //insert sorgusu eklenecek.
           insertWithQuery(tempData);


       }catch (Exception e){e.printStackTrace();}

    }
    public void updateWithTaxNumberFromRita(List<Data> dataList, String responseString){

    }

    public void insertWithQuery(Data data){
        em.createNativeQuery("INSERT INTO data (tckn, unvan, vdkodu, vkn, durum_text, plaka, oid, tc_tum_il_na" +
                "tc_adres_donen, faaliyet_aciklama, ise_baslama_tarihi, matrah, tahakkuk_eden, yil)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
        .setParameter(1, data.getTckn())
        .setParameter(2, data.getUnvan())
        .setParameter(3, data.getVdkodu())
        .setParameter(4, data.getVkn())
        .setParameter(5, data.getDurum_text())
        .setParameter(6, data.getPlaka())
        .setParameter(7, data.getOid())
        .setParameter(8, data.getTc_tum_il_na())
        .setParameter(9, data.getTc_adres_donen())
        .setParameter(10, data.getNacekoduaciklama())
        .setParameter(11, data.getIsebaslamatarihi())
        .setParameter(12, data.getMatrah())
        .setParameter(13, data.getTahakkukeden())
        .setParameter(14, data.getYil())
        .executeUpdate();
    }


}
