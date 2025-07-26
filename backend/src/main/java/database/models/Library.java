package database.models;

import database.annotations.Column;
import database.annotations.ForeignKey;
import database.annotations.Id;
import database.annotations.Table;
import database.query.SelectBuilder;

@Table(name = "Libraries")
public class Library extends Entity {
    @Id
    @Column(name = "id", type = "CHAR(36)")
    private String id;

    @Column(name = "name", type = "VARCHAR(255)", nullable = false)
    private String name;

    @ForeignKey(references = User.class, column = "cf")
    @Column(name = "user_id", type = "CHAR(36)", nullable = false)
    private String userId;

    public Library(String id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }


    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Library.class);
    }


}
