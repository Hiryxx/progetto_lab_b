package database.types.keys;

import database.types.Constraint;
import database.types.Field;

public class PrimaryKey<T> extends Field<T> {
    public PrimaryKey(String name, T value, String sqlType, Constraint[] constraints) {
        super(name, value, sqlType, constraints);
    }


}
