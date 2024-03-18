package peaksoft.validations;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = AgeValidator.class
)
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeValidation {
    String message() default "{Age of the wait must be between 18 and 30 years}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
