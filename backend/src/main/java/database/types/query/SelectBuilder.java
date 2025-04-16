package database.types.query;

public class SelectBuilder {
    private String query = "";
    private String queryParameters;
    private String fromTable;
    private String whereClause = "";

    public SelectBuilder(String queryParameters, String fromTable) {
        this.queryParameters = queryParameters;
        this.fromTable = fromTable;
    }

    public Query build() {
        String query = "SELECT " + queryParameters + " FROM " + fromTable + whereClause;
        // check if ok with pattern
        return new Query(query);
    }

    public SelectBuilder where(String clause) {
        whereClause = " WHERE " + clause;
        return this;
    }

    // todo implement others


}
