package peaksoft.validations;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = PhoneNumberValidator.class
)
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberValidation {
    String message() default "{Phone number start with '+996' and length 13 symbol}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
