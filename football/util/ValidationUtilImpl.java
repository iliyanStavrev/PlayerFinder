package com.example.football.util;

import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ValidationUtilImpl implements ValidationUtil {
    @Override
    public <E> boolean isValid(E entity) {

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        return validator.validate(entity).isEmpty();
    }
}
