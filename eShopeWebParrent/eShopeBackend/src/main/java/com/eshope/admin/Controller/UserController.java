package com.eshope.admin.Controller;

import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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
    public String saveUser( RedirectAttributes redirectAttributes,@Valid @ModelAttribute(value = "user") User user, Errors errors,Model model,HttpSession session){



        //TO CHECK UNIQUE EMAIL ID
            if (user.getEmail() != "" && !userService.isEmailUnique(user.getEmail())) {
                log.error("Contact form validation failed due to email ");
                model.addAttribute("emailNotUnique", "There is another user having same email id");
                List<Role> listAllRoles = userService.listAllRoles();
                model.addAttribute("listAllRoles", listAllRoles);
                return "userForm.html";
            }
//        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("Contact form validation failed due to : " + errors.toString());
            List<Role> listAllRoles=userService.listAllRoles();
            model.addAttribute("listAllRoles",listAllRoles);
            return "userForm.html";
        }


        //SAVE DETAILS
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
        User user=userService.getUserById(id);
        model.addAttribute("user",user);
        List<Role> listAllRoles=userService.listAllRoles();
        model.addAttribute("listAllRoles",listAllRoles);
        session.setAttribute("userId",id);
            return "userUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/users";
        }
    }

    @PostMapping("/users/editUser")
    public String editUser( RedirectAttributes redirectAttributes,@Valid @ModelAttribute(value = "user") User user, Errors errors,Model model,HttpSession session) {
         Integer id= (Integer) session.getAttribute("userId");
         String savedPassword=userService.getUserById(id).getPassword();


        //TO CHECK UNIQUE EMAIL ID
        if(id!=null&&userService.getUserById(id)!=null&&!(userService.getUserById(id).getEmail().matches(user.getEmail()))) {
            if (user.getEmail() != "" && !userService.isEmailUnique(user.getEmail())) {
                log.error("Contact form validation failed due to email ");
                model.addAttribute("emailNotUnique", "There is another user having same email id");
                List<Role> listAllRoles = userService.listAllRoles();
                model.addAttribute("listAllRoles", listAllRoles);
                return "userUpdateForm.html";
            }
        }


        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("Contact form validation failed due to : " + errors.toString());
            List<Role> listAllRoles=userService.listAllRoles();
            model.addAttribute("listAllRoles",listAllRoles);
            return "userUpdateForm.html";
        }

        if(user.getChangePassword()!=null||user.getChangePassword()!=""){
            user.setPassword(user.getChangePassword());
            user.setChangePassword("");
        }

        //SAVE DETAILS
        userService.editUser(user);
        redirectAttributes.addFlashAttribute("message","The user has been edited successfully");
        return "redirect:/users";

    }




    }



