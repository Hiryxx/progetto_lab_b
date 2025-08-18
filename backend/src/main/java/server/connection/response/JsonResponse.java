package server.connection.response;

import utils.JSONUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public non-sealed class JsonResponse implements Sendable {
    private String json;

    public JsonResponse(ResultSet resultSet) throws SQLException {
        this.json = JSONUtil.resultSetToJson(resultSet).toString();
    }

    public String getJson() {
        return json;
    }
}
