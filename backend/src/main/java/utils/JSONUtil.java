package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import database.models.base.Entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JSONUtil {
    private static final ObjectMapper MAPPER = createObjectMapper();


    /**
     * Returns a singleton ObjectMapper instance with the ParameterNamesModule registered.
     * This ensures that the module is only registered once, improving performance.
     *
     * @return a singleton ObjectMapper instance
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the module here. This will now only happen one time.
        mapper.registerModule(new ParameterNamesModule());
        return mapper;
    }

    /**
     * Converts a JSON string to an Entity object.
     *
     * @param json the JSON string to convert
     * @param classType the class type of the Entity
     * @return the Entity object
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    public static Entity parse(String json, Class<? extends Entity> classType) throws JsonProcessingException {
        return MAPPER.readValue(json, classType);
    }
    /**
     * Converts an object to a JSON string.
     *
     * @param obj the object to convert
     * @return the JSON string representation of the object
     * @throws JsonProcessingException if there is an error processing the object
     */
    public static String stringify(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
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
