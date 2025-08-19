package database.models;

import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;
import database.query.SelectBuilder;

@Table(name = "Libraries")
public class Library extends Entity {
    @Id(autoIncrement = true)
    @Column( type = "SERIAL")
    private int id;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String name;

    @ForeignKey(references = User.class, column = "cf")
    @Column( type = "CHAR(36)", nullable = false)
    private String userCf;

    //todo add timestamp

    public Library(int id, String name, String userCf) {
        this.id = id;
        this.name = name;
        this.userCf = userCf;
    }


    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Library.class);
    }


    public Object getId() {
        return id;
    }
}
