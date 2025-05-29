package com.eshope.admin.PDFExporter;


import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eshope.admin.Exporter.AbstractExporter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BrandPdfExporter  extends AbstractExporter {

    public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {

        super.setResponseHeader("Brand_",response,"application/pdf",".pdf");

        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();

        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph=new Paragraph("List of  Brands",font);
        paragraph.setAlignment(paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table= new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1.2f,3.5f,4.0f});

        writeTableHeader(table);
        writeTableData(table,listBrands);

        document.add(table);

        document.close();
    }



    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font= FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Brand Id",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category",font));
        table.addCell(cell);



    }

    private void writeTableData(PdfPTable table, List<Brand> listBrands) {
        for(Brand brand:listBrands){

            String categoriesList="";
            Set<Category> categories=brand.getCategories();
            for(Category category: categories){
                categoriesList+=category.getName()+" ,";
            }

            table.addCell(String.valueOf(brand.getId()));
            table.addCell(brand.getName());
            table.addCell(categoriesList);
        }

    }
}
