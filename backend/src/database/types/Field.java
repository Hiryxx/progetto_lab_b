package database.types;

/**
 * Represents a field in a database table
 *
 * @param <T>
 */
public class Field<T> {
    private String name;
    private T value;
    private String sqlType;
    private Constraint[] constraints;

    public Field(String name, T value, String sqlType, Constraint[] constraints) {
        this.name = name;
        this.value = value;
        this.sqlType = sqlType;
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getSqlType() {
        return sqlType;
    }

    public Constraint[] getConstraints() {
        return constraints;
    }

}
