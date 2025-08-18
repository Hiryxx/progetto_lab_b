package database.query;

import database.models.base.Entity;
import database.models.base.annotations.Table;
import utils.DbUtil;

import java.util.List;

/**
 * SelectBuilder is a class that helps to build SQL SELECT queries.
 * It allows to add multiple clauses to the query.
 */
public class SelectBuilder {
    private final String queryParameters;
    private final String fromTable;
    private String whereClause = "";
    private String orderByClause = "";
    private String groupByClause = "";
    private String limitClause = "";
    private String joinClause = "";


    public SelectBuilder(String queryParameters, String fromTable) {
        this.queryParameters = queryParameters;
        this.fromTable = fromTable;
    }

    public SelectBuilder(String queryParameters, Class<? extends Entity> fromTable) {
        this.queryParameters = queryParameters;
        this.fromTable = DbUtil.getTableName(fromTable);
    }
    /**
     * Prepares the query for execution
     *
     * @return a PrepareQuery object that can be executed
     */
    public PrepareQuery prepare(){
        Query query = this.build();
        return new PrepareQuery(query);
    }

    /**
     * Prepares the query for execution with the given values
     *
     * @param values the values to be used in the query
     * @return a PrepareQuery object that can be executed
     */
    public PrepareQuery prepare(Object... values) {
        Query query = this.build();
        return new PrepareQuery(query, List.of(values));
    }

    /**
     * Builds the query
     *
     * @return the query
     */
    private Query build() {
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
     * @param from the class to join from
     * @param on the join clause
     * @return the SelectBuilder instance
     */
    //todo maybe make it better with a JoinBuilder?
    public SelectBuilder join(Class<?extends Entity> from, String on) {
        String joinTable = DbUtil.getTableName(from);

        joinClause = " JOIN " + joinTable + " ON " + on;
        return this;
    }

}
