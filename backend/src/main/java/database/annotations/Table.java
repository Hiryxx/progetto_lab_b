package database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Annotation available at runtime
@Target(ElementType.TYPE) // Can only be applied to classes/interfaces
public @interface Table {
    String name() default ""; // Table name parameter
    String schema() default ""; // Optional schema parameter
}

