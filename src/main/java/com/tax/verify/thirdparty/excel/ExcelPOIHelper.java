package com.tax.verify.thirdparty.excel;

import com.tax.verify.model.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class ExcelPOIHelper {
    public Map<Integer, List<Data>> readExcel(String fileLocation) throws IOException {
        Map<Integer, List<Data>> data = new HashMap<>();

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
                             .forEach(i -> ls.add(new Data()));
                 });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<Integer, List<Data>> readXSSFWorkbook(FileInputStream fis) throws IOException {
        XSSFWorkbook workbook = null;
        Map<Integer, List<Data>> data = new HashMap<>();
        try {

            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                data.put(i, new ArrayList<>());
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            XSSFCellStyle cellStyle = cell.getCellStyle();

                            Data data1 = new Data();
                            XSSFColor bgColor = cellStyle.getFillForegroundColorColor();

                            XSSFFont font = cellStyle.getFont();

                            XSSFColor textColor = font.getXSSFColor();
                            if (textColor != null) {
                                byte[] rgbColor = textColor.getRGB();

                            }
                            data.get(i)
                                    .add(data1);
                        } else {
                            data.get(i)
                                    .add(new Data());
                        }
                    }
                }
            }
        } catch (Exception e){}
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
    private Map<Integer, List<Data>> readHSSFWorkbook(FileInputStream fis) throws IOException {
        Map<Integer, List<Data>> data = new HashMap<>();
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(fis);

            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);
                data.put(i, new ArrayList<>());
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        HSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            HSSFCellStyle cellStyle = cell.getCellStyle();

                            Data data1 = new Data();

                            HSSFColor bgColor = cellStyle.getFillForegroundColorColor();
                            data.get(i)
                                    .add(data1);
                        } else {
                            data.get(i)
                                    .add(new Data());
                        }
                    }
                }
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return data;
    }

}
