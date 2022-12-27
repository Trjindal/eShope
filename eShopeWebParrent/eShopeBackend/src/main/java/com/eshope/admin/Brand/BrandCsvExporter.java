package com.eshope.admin.Brand;

import com.eShope.common.entity.Brand;
import com.eshope.admin.Main.Exporter.AbstractExporter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BrandCsvExporter extends AbstractExporter {

    public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {

        super.setResponseHeader("brand_",response,"text/csv",".csv");

        ICsvBeanWriter csvBeanWriter=new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);


        String[] csvHeader={"Brand Id","Name","Categories"};
        String[] fieldMapping={"id","name","categories"};

        csvBeanWriter.writeHeader(csvHeader);

        for(Brand brand:listBrand){


            csvBeanWriter.write(brand,fieldMapping);
        }

        csvBeanWriter.close();
    }
}
