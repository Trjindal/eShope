package com.eshope.admin.Controller;

import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Service.UserService;
import com.eshope.admin.Utility.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/users")
    public String listAllUsers(Model model){
        return listByPage(1,model,"id","asc");
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField,@Param("sortDir") String sortDir){
        Page<User> page=userService.listByPage(pageNum,sortField,sortDir);
        List<User> listUsers=page.getContent();

        long startCount =(pageNum-1)*UserService.USERS_PER_PAGE+1;
        long endCount=startCount+UserService.USERS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllUsers",listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
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
    public String saveUser(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "user") User user, Errors errors, Model model, HttpSession session, @RequestParam("image")MultipartFile multipartFile) throws IOException {



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

//       UPLOAD PHOTOS AND SAVE

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser=userService.saveUser(user);
            String uploadDir="user-photos/"+savedUser.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }

        //SAVE DETAILS
        User savedUser=userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");
        return "redirect:/users";
    }



    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
            User user=userService.getUserById(id);
            model.addAttribute("user",user);
            session.setAttribute("id",id);
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
    public String editUser( RedirectAttributes redirectAttributes,@Valid @ModelAttribute(value = "user") User user, Errors errors,Model model,HttpSession session ,@RequestParam("image")MultipartFile multipartFile) throws IOException{
//         User existingUser= (User) model.getAttribute("user");
         Integer id= (Integer) session.getAttribute("id");
         log.error(String.valueOf(id));
         User existingUser=userService.getUserById(id);
         String savedPassword=existingUser.getPassword();

        log.error(String.valueOf(existingUser.getId()));

        //TO CHECK UNIQUE EMAIL ID
        if(existingUser!=null&&userService.getUserById(existingUser.getId())!=null&&!(userService.getUserById(existingUser.getId()).getEmail().matches(user.getEmail()))) {
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
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setRoles(user.getRoles());
        existingUser.setEnabled(user.isEnabled());
//        existingUser.setPhotos(user.getPhotos());
        log.error("Previous password"+savedPassword);
        log.error("Changed password"+user.getChangePassword());
        if(!(user.getChangePassword().isEmpty())){
            existingUser.setPassword(user.getChangePassword());
            log.error("Password in existingUser "+existingUser.getPassword());
            log.error("CPassword in existingUser"+existingUser.getChangePassword());
            log.error("Password in User "+user.getPassword());
            log.error("CPassword in User"+user.getChangePassword());
            user.setChangePassword("");
        }

        //        PHOTOS SAVE

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingUser.setPhotos(fileName);
            User savedUser=userService.editUser(existingUser);
            String uploadDir="user-photos/"+savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }else{
            if(existingUser.getPhotos().isEmpty()) existingUser.setPhotos(null);
            userService.saveUser(existingUser);
        }

        //SAVE DETAILS
        log.error("AFTER Changing password"+existingUser.getPassword());
        log.error("MATCHING with previous "+ savedPassword.matches(existingUser.getPassword()));
//        userService.editUser(existingUser);
        redirectAttributes.addFlashAttribute("message","The user has been edited successfully");
        return "redirect:/users";

    }


    //DELETE CONTROLLER
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The user ID "+id+"has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        userService.updateUserEnabledStatus(id,enabled);
        String status=enabled?"enabled":"disabled";
        String message="The user Id "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }


    }



