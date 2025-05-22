package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.models.Entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JSONUtil {
    /**
     * Converts a JSON string to an Entity object.
     *
     * @param json the JSON string to convert
     * @param classType the class type of the Entity
     * @return the Entity object
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    public static Entity parse(String json, Class<? extends Entity> classType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, classType);
    }

    /**
     * Converts a ResultSet to a JSON ObjectNode.
     *
     * @param rs the ResultSet to convert
     * @return an ObjectNode representing the JSON
     * @throws SQLException if an SQL error occurs
     */
    public static ObjectNode resultSetToJson(ResultSet rs) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = rs.getObject(i);

            if (value != null)
                json.putPOJO(columnName, value);
            else // should never happen
                System.out.println("Null value for column: " + columnName);
        }

        return json;
    }
}
