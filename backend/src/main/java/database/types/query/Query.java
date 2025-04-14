package database.types.query;

import java.sql.ResultSet;

public class Query {
    private String query;

    public Query(String query){
        this.query = query;
    }
    public Query execute() {
        // Execute the query using a database connection
        return null;
    }
}

// todo implement iterable