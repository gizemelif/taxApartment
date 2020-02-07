package com.tax.verify.thirdparty.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ExcelPOIHelper {
    public Map<Integer, List<MyCell>> readExcel(String fileLocation){
        Map<Integer, List<MyCell>> data = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(new File(fileLocation));
            if(fileLocation.endsWith(".xls")){
                data = readHSSFWorkbook(fis);
            }else if(fileLocation.endsWith(".xlsx")){
                data = readXSSFWorkbook(fis);
            }
            int maxNrCols = data.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

            data.values().stream()
                 .filter(ls -> ls.size() < maxNrCols)
                 .forEach(ls -> {
                     IntStream.range(ls.size(), maxNrCols)
                             .forEach(i -> ls.add(new MyCell("")));
                 });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<Integer, List<MyCell>> readXSSFWorkbook(FileInputStream fis) {
        Map<Integer, List<MyCell>> data = new HashMap<>();

        return data;
    }

    private String readCellContent(Cell cell){
        String content;
        switch (cell.getCellTypeEnum()){
            case STRING:
                content = cell.getStringCellValue();
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    content = cell.getDateCellValue() + "";
                }else {
                    content = cell.getNumericCellValue() + "";
                }
                break;
            case BOOLEAN:
                content = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                content = cell.getCellFormula();
                break;
            default:
                content = "";
        }
        return content;
    }
    private Map<Integer, List<MyCell>> readHSSFWorkbook(FileInputStream fis){
        Map<Integer, List<MyCell>> data = new HashMap<>();
        HSSFWorkbook workbook = null;
        try{
            workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
                HSSFRow row = sheet.getRow(i);
                data.put(i, new ArrayList<>());
                if(row != null){
                    for(int j = 0; j < row.getLastCellNum(); j++){
                        HSSFCell cell1 = row.getCell(j);
                        if(cell1 != null && cell1.equals("vd_sorulan")){
                            MyCell myCell = new MyCell();

                            myCell.setVd_sorulan(cell1.getStringCellValue().trim());

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

}
