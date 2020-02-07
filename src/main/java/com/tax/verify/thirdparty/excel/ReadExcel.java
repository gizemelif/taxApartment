package com.tax.verify.thirdparty.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ReadExcel {
    private static String vd_sorulan;
    private static String tc_sorulan;
    private static String plaka;

    public Map<String, String> readExcel(Workbook workbook){
        Map<String, String> data = new HashMap<>();
        try{
            //FileInputStream fis = new FileInputStream(in);
            //Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();

            while(itr.hasNext()){
                Row  currentRow = itr.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while(cellIterator.hasNext()){
                    Cell currentCell = cellIterator.next();
                    Cell cell2 = cellIterator.next();
                    Cell cell3 = cellIterator.next();

                    if ((currentCell.getCellTypeEnum() == CellType.NUMERIC || currentCell.getCellTypeEnum() == CellType.STRING) && currentCell.getStringCellValue().equals("vd_sorulan")) {
                        vd_sorulan = currentCell.getStringCellValue();
                        data.put(vd_sorulan,"vd_sorulan");
                        System.out.print(currentCell.getStringCellValue() + "--");
                    }else if((cell2.getCellTypeEnum() == CellType.NUMERIC || cell2.getCellTypeEnum() == CellType.STRING ) && cell2.getStringCellValue().equals("tc_sorulan")){
                        tc_sorulan = cell2.getStringCellValue();
                        data.put(tc_sorulan,"tc_sorulan");
                    }else if((currentCell.getCellTypeEnum() == CellType.NUMERIC || cell3.getCellTypeEnum() == CellType.STRING) && cell3.getStringCellValue().equals("plaka")){
                        plaka = cell3.getStringCellValue();
                        data.put(plaka, "plaka");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}
