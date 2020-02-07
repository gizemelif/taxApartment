package com.tax.verify.api;
import com.tax.verify.jpa.*;
import com.tax.verify.jpa.pojo.Queue;
import com.tax.verify.jpa.QueueService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


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

    private String fileLocation;

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

    @RequestMapping(method = RequestMethod.POST, path = "/uploadExcelFile")
    public String uploadFile(Model model, MultipartFile file){
        try {
            InputStream in = file.getInputStream();
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            FileOutputStream f = new FileOutputStream(fileLocation);
            int ch = 0;
            while((ch = in.read()) != -1){
                f.write(ch);
            }
            f.flush();
            f.close();
            model.addAttribute("message", "File: " + file.getOriginalFilename()
            + " has been uploaded successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            return "Upload process failed!!";
        }
        return "excel";
    }


}
