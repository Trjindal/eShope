package com.eShope.common.validations;

import com.eShope.common.annotation.ChangePasswordValidator;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j

public class PasswordChangeValidator implements ConstraintValidator<ChangePasswordValidator,String> {

    String chanePassword="";

    @Override
    public void initialize(ChangePasswordValidator changePassword) {
        this.chanePassword=chanePassword;
    }

    public boolean isValid(String passwordField, ConstraintValidatorContext cxt) {
        System.out.println("Password Field="+passwordField);
        log.error(String.valueOf(passwordField+" "+passwordField==null));
        if (passwordField == null||passwordField.isEmpty()) {
            log.error("PASSWORD FIELD NULL :::: TRUEEEE");
            return true;
        }
        else if (passwordField.length() >= 5) {
            log.error("PASSWORD FIELD LESS THAN 5 :::: TRUEEEE");
            return true;
        }
         log.error(String.valueOf("PASSWORD FIELD NULL?? :::: "+ passwordField==null));
        log.error("PASSWORD FIELD EMPTY?? :::: "+passwordField.isEmpty()+" "+passwordField.isBlank());
        log.error("PASSWORD FIELD  :::: "+chanePassword+"CC"+passwordField+"TO");
        log.error("PASSWORD FIELD  :::: FALSEEEE");
        return false;
    }
}
