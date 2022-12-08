package com.eShope.common.annotation;

import com.eShope.common.validations.PasswordChangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordChangeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangePasswordValidator {

        String message() default "Please choose a strong password or if you want to go with previous password leave this field blank!! ";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }



