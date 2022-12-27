package com.eshope.admin.Brand;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eshope.admin.Main.Exporter.AbstractExporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BrandExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public BrandExcelExporter(){
        workbook=new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet=workbook.createSheet("Brands");
        XSSFRow row =sheet.createRow(0);

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row,0,"Brand Id",cellStyle);
        createCell(row,1,"Name",cellStyle);
        createCell(row,2,"Category",cellStyle);

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

    private void writeDataLines(List<Brand> listBrand) {

        int rowIndex=1;

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(false);
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(Brand brand :listBrand){
            XSSFRow row=sheet.createRow(rowIndex++);
            int columnIndex=0;
            String categoriesList="";
            Set<Category> categories=brand.getCategories();
            for(Category category: categories){
                categoriesList+=category.getName()+" ,";
            }
            createCell(row,columnIndex++,brand.getId(),cellStyle);
            createCell(row,columnIndex++,brand.getName(),cellStyle);
            createCell(row,columnIndex++,categoriesList,cellStyle);

        }


    }

    public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {

        super.setResponseHeader("Brand_",response,"application/octet-stream",".xlsx");

        writeHeaderLine();
        writeDataLines(listBrand);

        ServletOutputStream outputStream=response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


}
