package com.eshope.admin.ExcelExporter;

import com.eShope.common.entity.Category;

import com.eshope.admin.Exporter.AbstractExporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class CategoryExcelExporter  extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public CategoryExcelExporter(){
        workbook=new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet=workbook.createSheet("Categories");
        XSSFRow row =sheet.createRow(0);

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row,0,"Category Id",cellStyle);
        createCell(row,1,"Name",cellStyle);
        createCell(row,2,"Alias",cellStyle);
        createCell(row,3,"Parent",cellStyle);
        createCell(row,4,"Enabled",cellStyle);
    }

    private void createCell(Row row, int columnIndex, Object value, CellStyle style){


        sheet.autoSizeColumn(columnIndex);

        Cell cell=row.createCell(columnIndex);


        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        } else if(value instanceof Boolean){
            cell.setCellValue((Boolean)value);
            cell.setCellValue((Boolean)value);
        }else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    private void writeDataLines(List<Category> listCategories) {

        int rowIndex=1;

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(false);
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(Category category :listCategories){
            XSSFRow row=sheet.createRow(rowIndex++);
            int columnIndex=0;
            String parent=category.getParent()!=null?category.getParent().getName():"none";
            createCell(row,columnIndex++,category.getId(),cellStyle);
            createCell(row,columnIndex++,category.getName(),cellStyle);
            createCell(row,columnIndex++,category.getAlias(),cellStyle);
            createCell(row,columnIndex++,parent,cellStyle);
            createCell(row,columnIndex++,category.isEnabled(),cellStyle);
        }


    }

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

        super.setResponseHeader("category_",response,"application/octet-stream",".xlsx");

        writeHeaderLine();
        writeDataLines(listCategories);

        ServletOutputStream outputStream=response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


}
