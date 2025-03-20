package database.types;

/**
 * Represents a constraint in a database table
 */
public enum Constraint {
    PRIMARY_KEY,
    UNIQUE,
    NOT_NULL,
    AUTO_INCREMENT;

    public String toString() {
        if (this == AUTO_INCREMENT)
            return this.name();

        return this.name().replace("_", " ");
    }
}
