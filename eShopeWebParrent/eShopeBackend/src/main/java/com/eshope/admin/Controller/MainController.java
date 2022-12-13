package com.eshope.admin.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class MainController {

    @GetMapping({"", "/home"})
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/error")
    public String viewErrorPage() {
        return "error";
    }

//


}


