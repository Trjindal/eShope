package com.eshope.admin.Controller;

import com.eShope.common.entity.User;
import com.eshope.admin.Security.EshopeUserDetails;
import com.eshope.admin.Service.UserService;
import com.eshope.admin.Utility.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
public class AccountController {


    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal EshopeUserDetails loggedUser, Model model){

        String email=loggedUser.getUsername();
        User user=userService.getUserByEmail(email);

        model.addAttribute("user",user);
        model.addAttribute("users",user);

        return "account_form.html";
    }

    @PostMapping("/account/update")
    public String updateAccount(RedirectAttributes redirectAttributes,@AuthenticationPrincipal EshopeUserDetails loggedUser, @Valid @ModelAttribute(value = "user") User user, Errors errors, Model model , @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String email=user.getEmail();
        User existingUser=userService.getUserByEmail(email);

        String savedPassword=existingUser.getPassword();

        log.error(String.valueOf(existingUser.getRoles()));

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("Edit form validation failed due to : " + errors.toString());
            model.addAttribute("users",existingUser);
            return "account_form.html";
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setChangePassword(user.getChangePassword());
        user.setChangePassword("");

        //        PHOTOS SAVE

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingUser.setPhotos(fileName);
            User savedUser=userService.editUser(existingUser);
            String uploadDir="user-photos/"+savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }

        //SAVE DETAILS
        userService.editUser(existingUser);

        //Update authentication
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());


        redirectAttributes.addFlashAttribute("message","The user has been edited successfully");
        return "redirect:/account";

    }

}
