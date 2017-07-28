package org.georchestra.seditor.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Constraint(validatedBy={AttributeNameAvailableValidator.class})
public @interface AttributeNameAvailable {
String message() default "{org.georchestra.seditor.validator.AttributeNameAvailable}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
