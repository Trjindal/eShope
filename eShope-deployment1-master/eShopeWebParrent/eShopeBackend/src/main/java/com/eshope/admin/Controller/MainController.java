package com.eshope.admin.Controller;

import com.eshope.admin.Entity.DashboardInfo;
import com.eshope.admin.Security.EshopeUserDetails;
import com.eshope.admin.Service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private DashboardService dashboardService;
    @GetMapping({"", "/home","/"})
    public String viewHomePage(Model model,Principal principale) {

        EshopeUserDetails principal = (EshopeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullName=principal.fullName();
        DashboardInfo summary = dashboardService.loadSummary();
        model.addAttribute("summary", summary);
        model.addAttribute("principale",principale);
        model.addAttribute("fullName",fullName);
        return "index";
    }

    @GetMapping("/error")
    public String viewErrorPage() {
        return "error";
    }

//


}


