package database.types.query;

// todo change names
public class QueryBuilder {
    private String query = "";
    private String queryParameters;
    private String fromTable;
    private String whereClause = "";

    public QueryBuilder(String queryParameters, String fromTable) {
        this.queryParameters = queryParameters;
        this.fromTable = fromTable;
    }

    public Query execute() {
        String query = "SELECT " + queryParameters + " FROM " + fromTable + whereClause;
        // check if ok with pattern
        return new Query(query).execute();
    }

    public QueryBuilder where(String clause) {
        whereClause = " WHERE " + clause;
        return this;
    }

    // todo implement others


}
