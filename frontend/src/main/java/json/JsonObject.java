package json;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static json.JsonUtil.mapper;

public class JsonObject {
    ObjectNode objectNode;

    public JsonObject(){
        this.objectNode = mapper.createObjectNode();
    }

    public void put(String key, String value) {
        objectNode.put(key, value);
    }

    public String toString() {
        return objectNode.toString();
    }
}
