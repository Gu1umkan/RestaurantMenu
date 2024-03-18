package peaksoft.validations;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = PriceValidator.class
)
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceValidation {
    String message() default "{Price should be }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
