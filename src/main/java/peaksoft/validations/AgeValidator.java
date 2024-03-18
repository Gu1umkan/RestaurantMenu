package peaksoft.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<AgeValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        int age =  LocalDate.now().getYear() - dateOfBirth.getYear();
        return age >= 18 && age <= 45;
    }
}
