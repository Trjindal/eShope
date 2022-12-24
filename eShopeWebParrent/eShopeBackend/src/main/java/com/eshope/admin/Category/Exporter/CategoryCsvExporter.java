package com.eshope.admin.Category.Exporter;

import com.eShope.common.entity.Category;
import com.eShope.common.entity.User;
import com.eshope.admin.Main.Exporter.AbstractExporter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter  extends AbstractExporter {

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

        super.setResponseHeader("category_",response,"text/csv",".csv");

        ICsvBeanWriter csvBeanWriter=new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader={"Category Id","Name","Alias","Enabled"};
        String[] fieldMapping={"id","name","alias","enabled"};

        csvBeanWriter.writeHeader(csvHeader);

        for(Category category:listCategories){
            csvBeanWriter.write(category,fieldMapping);
        }

        csvBeanWriter.close();
    }
}
