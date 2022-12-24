package com.eshope.admin.Category.Exporter;

import com.eShope.common.entity.Category;
import com.eShope.common.entity.User;
import com.eshope.admin.Main.Exporter.AbstractExporter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CategoryPdfExporter  extends AbstractExporter {

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

        super.setResponseHeader("category_",response,"application/pdf",".pdf");

        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();

        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph=new Paragraph("List of  Users",font);
        paragraph.setAlignment(paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table= new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1.2f,3.5f,3.0f,3.0f,3.0f});

        writeTableHeader(table);
        writeTableData(table,listCategories);

        document.add(table);

        document.close();
    }



    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font= FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Category Id",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Alias",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Parent Name",font));
        table.addCell(cell);


        cell.setPhrase(new Phrase("Enabled",font));
        table.addCell(cell);


    }

    private void writeTableData(PdfPTable table, List<Category> listCategories) {
        for(Category category:listCategories){
//            String parent=category.getParent()!=null?category.getParent().getName():"none";
            table.addCell(String.valueOf(category.getId()));
            table.addCell(category.getName());
            table.addCell(category.getAlias());
            table.addCell(category.getParent()!=null?category.getParent().getName():"none");
            table.addCell(String.valueOf(category.isEnabled()));
        }

    }
}
