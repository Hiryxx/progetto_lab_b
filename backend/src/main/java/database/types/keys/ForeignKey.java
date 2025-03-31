package database.types.keys;

import database.models.Entity;
import database.types.Constraint;
import database.types.Field;

/**
 * Foreign key field
 * This class only represents a foreign key field
 *
 * @param <T>
 * @param <ReferencedEntity>
 */

public class ForeignKey<T, ReferencedEntity extends Entity> extends Field<T> {

    public ForeignKey(String name, T value, String sqlType, Constraint[] constraints) {
        super(name, value, sqlType, constraints);
    }

    // todo do i need a value of that class or this is ok?
    public PrimaryKey<String> referencedPrimaryKey() {
        return ReferencedEntity.getPrimaryKey();
    }

    // todo maybe i dont even need this because i just need to know what is the class of the referenced entity because i am assuming that the pk is always id
}
