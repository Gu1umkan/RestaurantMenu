package peaksoft.validations;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(
        validatedBy = ExperienceValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExperienceValidation {
    String message() default "{Can not be zero or negative number}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
