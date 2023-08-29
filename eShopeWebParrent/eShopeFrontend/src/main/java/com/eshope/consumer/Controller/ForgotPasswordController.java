package com.eshope.consumer.Controller;

import com.eShope.common.entity.Customer;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.SettingService;
import com.eshope.consumer.SettingBag.EmailSettingBag;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;


    @GetMapping("/forgotPassword")
    public String showRequestForm() {
        return "Customer/forgotPasswordForm";
    }

    @PostMapping("/forgotPassword")
    public String processRequestForm(HttpServletRequest request, RedirectAttributes redirectAttributes) {

            String email = request.getParameter("email");
        try {
            String token= customerService.updateResetPassword(email);
            String link= Utility.getSiteURL(request)+"/reset_password?token="+token;
            sendEmail(link,email);
            redirectAttributes.addFlashAttribute("successMessage","We have sent a reset password link to your email.");
            return "redirect:/forgotPassword";
        } catch (UsernameNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/forgotPassword";
        } catch (MessagingException |UnsupportedEncodingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not send email to :"+ email) ;
            return "redirect:/forgotPassword";
        }
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model){

        Customer customer=customerService.getCustomerByResetPasswordToken(token);
        if(customer!=null){
            model.addAttribute("token",token);
        }else {
            model.addAttribute("errorMessage","You entered an invalid token. Please try again.");
            return "Customer/passwordResetFailed.html";
        }
        return "Customer/resetPasswordForm";

    }

    @PostMapping("resetPassword")
    public String processResetForm(HttpServletRequest request,Model model){
        String token=request.getParameter("token");
        String password=request.getParameter("password");

        try {
            customerService.updatePassword(token,password);
            return "Customer/passwordResetSuccess.html";

        }catch (UsernameNotFoundException ex){
            return "Customer/passwordResetFailed.html";
        }

    }

    private void sendEmail(String link,String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings=settingService.getEmailSettings();

        JavaMailSenderImpl mailSender= Utility.prepareMailSender(emailSettings);


        String toAddress=email;
        String subject="Reset Password";

        String content="<p>Hi ,</p>" +
                "<br><p>A password reset for your account was requested.</p>" +
                "<br><p>Please click the button below to change your password.</p>" +
                "<a href="+link+">Change Your Password</a>" +
                "<br><p>If you did not make this request, please contact Support.</p>" +
                "<p>Delivered by eShope.</p>";

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content,true);
        mailSender.send(message);
    }

}
