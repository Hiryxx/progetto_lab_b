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
    PrimaryKey<?> refrerencedPrimaryKey;

    public ForeignKey(String name, T value, String sqlType, Constraint[] constraints, PrimaryKey<?> refrerencedPrimaryKey) {
        super(name, value, sqlType, constraints);
        this.refrerencedPrimaryKey = refrerencedPrimaryKey;
    }

    // maybe I just need the name of the field
    public PrimaryKey<?> referencedPrimaryKey() {
        return refrerencedPrimaryKey;
    }

    public String getReferencedEntityClassName() {
        return ReferencedEntity.getTableName();
    }

}
