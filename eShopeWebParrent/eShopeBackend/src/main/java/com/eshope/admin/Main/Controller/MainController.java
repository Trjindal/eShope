package com.eshope.admin.Main.Controller;

import com.eshope.admin.Main.Security.EshopeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Slf4j
public class MainController {

    @GetMapping({"", "/home","/"})
    public String viewHomePage(Model model,Principal principale) {

        EshopeUserDetails principal = (EshopeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullName=principal.fullName();
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


