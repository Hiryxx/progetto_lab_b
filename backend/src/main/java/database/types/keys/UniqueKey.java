package database.types.keys;
import database.types.Constraint;
import database.types.Field;

/**
 * Unique key field
 * This class only represents a unique key field
 * @param <T>
 */
public class UniqueKey<T> extends Field<T>{
    public UniqueKey(String name, T value, String sqlType, Constraint[] constraints) {
        super(name, value, sqlType, constraints);
    }
}
