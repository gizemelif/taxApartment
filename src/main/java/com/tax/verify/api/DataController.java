package com.tax.verify.api;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import com.tax.verify.model.Data;
import com.tax.verify.dao.*;
import com.tax.verify.model.Queue;
import com.tax.verify.service.QueueService;
import com.tax.verify.thirdparty.excel.ExcelPOIHelper;
import com.tax.verify.thirdparty.excel.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.ws.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("api")
public class DataController {

    private final DataDaoImpl dataDaoImpl;

    @Autowired
    public DataController(DataDaoImpl dataDaoImpl){
        this.dataDaoImpl = dataDaoImpl;
    }

    @Autowired
    private DataRepositoryImp dataRepositoryImp;

    @Autowired
    private RepeatedSqlRepo repo;

    @Autowired
    private Queue queue;

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    private ReadExcel readExcel;

    @Autowired
    QueueService service;

    private String fileLocation;

    //Bu servis gelen SQL string leri alır ve Queue tablosuna set eder.
    @GetMapping("/datas")
    @ResponseBody
    public void all(@RequestParam String sql, @RequestParam String queryType) {

        service.setQueueRepo(sql, queryType);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addQuery")
    public void addRepeatedQuery(@RequestParam String sqlString, @RequestParam String queryType) {
        service.addRepoQueriedSqlVdTc(sqlString,queryType);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/uploadExcelFile")
    public String uploadFile(Model model, MultipartFile file) throws IOException {
        InputStream in = file.getInputStream();
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch = in.read()) != -1) {
            f.write(ch);
        }
        f.flush();
        f.close();
        Model message = model.addAttribute("message", "File: " + file.getOriginalFilename()
                + " has been uploaded successfully!");

        return "excel";
    }

    @Resource(name = "excelPOIHelper")
    private ExcelPOIHelper excelPOIHelper;

    @RequestMapping(method = RequestMethod.GET, value = "/readPOI")
    public String readPOI(Model model) throws IOException {
        if(fileLocation != null){
            if(fileLocation.endsWith(".xlsx") || fileLocation.endsWith(".xls")){
                Map<Integer, List<Data>> data = excelPOIHelper.readExcel(fileLocation);
                model.addAttribute("data",data);
            }else{
                model.addAttribute("message","Not a valid excel file");
            }
        }else{
            model.addAttribute("message","File missing! Please upload an excel file.");
        }
        return "excel";
    }

    @PostMapping("/sendToVerify")
    @ResponseBody
    public void createSqlString(@RequestParam("jsonResponseString") String jsonResponseString,
                                    @RequestParam("text") String text, @RequestParam("type") String type,
                                    @RequestParam("plate") String plate){

        Data data = new Data();
        if(type.equals("tc")) {

            data = dataDaoImpl.selectDataByGovernmentNumber(text, plate);

            //eğer vd_tc_index tablosunda var olan bir kayıt ise bilgileri update edilir.
            dataRepositoryImp.updateWithGovernmentFromRita(data, jsonResponseString, plate);


        }else if(type.equals("vkn")){

            //data = dataDaoImpl.selectDataByTaxNumber(text, plate);
            data = dataDaoImpl.selectDataByTaxNumber(text, plate);

            dataRepositoryImp.updateWithTaxNumberFromRita(data, jsonResponseString, plate);

        }
    }


}
