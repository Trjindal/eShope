//package com.eshope.admin.RestController;
//
//import java.util.List;
//
//import com.eshope.admin.Entity.ReportItem;
//import com.eshope.admin.Service.MasterOrderReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//@RestController
//public class ReportRestController {
//    @Autowired
//    private MasterOrderReportService masterOrderReportService;
//
//    @GetMapping("/reports/sales_by_date/{period}")
//    public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period) {
//        System.out.println("Report period: " + period);
//        return masterOrderReportService.getReportDataLast7Days();
//    }
//}