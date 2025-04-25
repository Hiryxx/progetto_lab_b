package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.models.Entity;

public class JSONParser {
    public static Entity parse(String json, Class<? extends Entity> classType) throws JsonProcessingException {
        // Implement JSON parsing logic here
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, classType);
    }
}
