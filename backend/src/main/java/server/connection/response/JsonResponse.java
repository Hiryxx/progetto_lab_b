package server.connection.response;

import utils.JSONUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JsonResponse is a class that represents a JSON response to be sent over a socket connection.
 * It implements the Sendable interface, allowing it to be sent as a response to a command execution.
 */
public non-sealed class JsonResponse implements Sendable {
    private String json;

    public JsonResponse(ResultSet resultSet) throws SQLException {
        this.json = JSONUtil.resultSetToJson(resultSet).toString();
    }

    public String getJson() {
        return json;
    }
}
