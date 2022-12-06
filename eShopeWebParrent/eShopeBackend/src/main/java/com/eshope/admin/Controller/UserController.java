package com.eshope.admin.Controller;

import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAllUsers(Model model){
        List<User> listAllUsers=userService.listAllUsers();
        model.addAttribute("listAllUsers",listAllUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser( Model model){
        List<Role> listAllRoles=userService.listAllRoles();
        User user=new User();

        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listAllRoles",listAllRoles);
        return "userForm.html";
    }

    @PostMapping("/users/saveUser")
    public String saveUser( RedirectAttributes redirectAttributes,@Valid @ModelAttribute(value = "user") User user, Errors errors,Model model){
        if(errors.hasErrors()){
            log.error("Contact form validation failed due to : " + errors.toString());
            List<Role> listAllRoles=userService.listAllRoles();
            model.addAttribute("listAllRoles",listAllRoles);
            return "userForm.html";
        }
        System.out.println(errors.toString())   ;
        System.out.println(user);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");
        return "redirect:/users";
    }

}



