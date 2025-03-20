package database.types.keys;

import database.types.Constraint;
import database.types.Field;

// todo look at this
// i think this is wrong because i should pass him the class of the referenced key
public class ForeignKey<T, ReferencedKey> extends Field<T> {
    ReferencedKey referencedKey;
    public ForeignKey(String name, T value, String sqlType, Constraint[] constraints, Class<ReferencedKey> referencedKey) {
        super(name, value, sqlType, constraints);
        this.referencedKey = (ReferencedKey) referencedKey;
    }

    public PrimaryKey<ReferencedKey> referencedPrimaryKey() {
        if (referencedKey instanceof PrimaryKey) {
            return (PrimaryKey<ReferencedKey>) referencedKey;
        } else {
            return null;
        }
    }
}
