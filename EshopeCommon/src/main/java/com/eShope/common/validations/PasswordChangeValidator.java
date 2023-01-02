package com.eShope.common.validations;

import com.eShope.common.annotation.ChangePasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class PasswordChangeValidator implements ConstraintValidator<ChangePasswordValidator,String> {

    String chanePassword="";

    @Override
    public void initialize(ChangePasswordValidator changePassword) {
        this.chanePassword=chanePassword;
    }

    public boolean isValid(String passwordField, ConstraintValidatorContext cxt) {
        System.out.println("Password Field="+passwordField);
        if (passwordField == null||passwordField.isEmpty()) {
            return true;
        }
        else if (passwordField.length() >= 5) {
            return true;
        }
        return false;
    }
}
