package com.eshope.admin.Controller;

import com.eshope.admin.Entity.ReportItem;
import com.eshope.admin.Service.MasterOrderReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private MasterOrderReportService masterOrderReportService;

    @GetMapping("/reports")
    public String viewSalesReportHome() {
        return "reports/reports";
    }



    @GetMapping("/reports/sales_by_date/{period}")
    public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period) {
        System.out.println("Report period: " + period);
        return masterOrderReportService.getReportDataLast7Days();
    }
}