package database.types;

/**
 * Represents a constraint in a database table
 */
public enum Constraint {
    NOT_NULL,
    DEFAULT,
    AUTO_INCREMENT; // TODO maybe remove this since pk is uuid

    public String toString() {
        if (this == AUTO_INCREMENT)
            return this.name();

        return this.name().replace("_", " ");
    }
}
