package org.avalon.avalonrest.utils.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Component
public class EntityValidator {
    private static final Logger logger = LoggerFactory.getLogger(EntityValidator.class);
    private final Validator validator;

    public EntityValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.append(String.format("Property '%s': %s; ",
                        violation.getPropertyPath(),
                        violation.getMessage()));
            }
            logger.error("Validation failed for entity {}: {}", entity.getClass().getSimpleName(), errorMessages);
            throw new ConstraintViolationException("Validation failed: " + errorMessages, violations);
        }
    }
}