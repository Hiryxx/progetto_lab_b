package database.query;

public class SelectBuilder {
    private String queryParameters;
    private String fromTable;
    private String whereClause = "";
    private String orderByClause = "";
    private String groupByClause = "";
    private String limitClause = "";
    private String joinClause = "";


    public SelectBuilder(String queryParameters, String fromTable) {
        this.queryParameters = queryParameters;
        this.fromTable = fromTable;
    }

    /**
     * Builds the query
     *
     * @return the query
     */
    public Query build() {
        String query = "SELECT " + queryParameters + " FROM " + fromTable + joinClause + whereClause + groupByClause + orderByClause + limitClause;
        // check if ok with pattern
        return new Query(query);
    }

    /**
     * Adds a where clause to the query
     *
     * @param clause the where clause
     * @return the SelectBuilder instance
     */
    public SelectBuilder where(String clause) {
        whereClause = " WHERE " + clause;
        return this;
    }

    /**
     * Adds an order by clause to the query
     *
     * @param orderBy the order by clause
     * @return the SelectBuilder instance
     */

    public SelectBuilder orderBy(String orderBy) {
        orderByClause = " ORDER BY " + orderBy;
        return this;
    }

    /**
     * Adds a group by clause to the query
     *
     * @param groupBy the group by clause
     * @return the SelectBuilder instance
     */

    public SelectBuilder groupBy(String groupBy) {
        groupByClause = " GROUP BY " + groupBy;
        return this;
    }

    /**
     * Adds a limit clause to the query
     *
     * @param limit the limit clause
     * @return the SelectBuilder instance
     */
    public SelectBuilder limit(int limit) {
        limitClause = " LIMIT " + limit;
        return this;
    }

    /**
     * Adds a join clause to the query
     *
     * @param join the join clause
     * @return the SelectBuilder instance
     */
    //todo maybe make it better with a JoinBuilder?
    public SelectBuilder join(String join) {
        joinClause = " JOIN " + join;
        return this;
    }

    // todo implement others


}
