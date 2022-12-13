package com.eshope.admin.Controller;

import com.eshope.admin.Security.EshopeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MainController {

    @GetMapping({"", "/home","/"})
    public String viewHomePage(Model model) {
        EshopeUserDetails principal = (EshopeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullName=principal.fullName();
        model.addAttribute("fullName",fullName);
        return "index";
    }

    @GetMapping("/error")
    public String viewErrorPage() {
        return "error";
    }

//


}


