package database.models.base.annotations;

import database.models.base.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {
    Class<?extends Entity> references();
    String column() default "id";
    String onDelete() default "CASCADE";
    String onUpdate() default "CASCADE";
}
