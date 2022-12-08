package com.eShope.common.validations;

import com.eShope.common.annotation.ChangePasswordValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class PasswordChangeValidator implements ConstraintValidator<ChangePasswordValidator,String> {

    String chngPassword;

    @Override
    public void initialize(ChangePasswordValidator changePassword) {
        chngPassword = "";
    }

    public boolean isValid(String passwordField, ConstraintValidatorContext cxt) {
        System.out.println("Password Field="+passwordField);
        log.error(String.valueOf(passwordField+" "+passwordField==null));
        if (passwordField == null||passwordField=="") return true;
        else if (passwordField.length() <= 5) return true;
        return false;
    }
}
