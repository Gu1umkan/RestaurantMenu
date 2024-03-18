package peaksoft.validations;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = PasswordValidator.class
)
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {
    String message() default "{Password must be at least 4 characters}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
