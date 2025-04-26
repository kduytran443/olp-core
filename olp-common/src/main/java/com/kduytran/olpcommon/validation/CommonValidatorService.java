package com.kduytran.olpcommon.validation;

import com.kduytran.olpcommon.exception.ResourceAlreadyExistedException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class CommonValidatorService {

    public <T> void validate(T object, List<ValidationRule> rules, FieldValueExists service) {
        Object idValue = getFieldValue(object, "id");

        for (ValidationRule rule : rules) {
            Object fieldValue = getFieldValue(object, rule.getFieldName());

            boolean exists = (rule.isUpdate())
                    ? service.exists(fieldValue, rule.getFieldName(), idValue)
                    : service.exists(fieldValue, rule.getFieldName());

            if (exists) {
                Optional.ofNullable(rule.getMessage()).ifPresentOrElse(
                        message -> {
                            throw new ResourceAlreadyExistedException(message);
                        },
                        () -> {
                            throw new ResourceAlreadyExistedException("Resource", rule.getFieldName(), String.valueOf(fieldValue));
                        }
                );
            }
        }
    }

    private Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
