package server.connection.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import database.models.base.Entity;

/**
 * Sendable is an interface that represents a response that can be sent over a network connection.
 * It is implemented by classes that represent different types of responses.
 */
public non-sealed class SingleResponse implements Sendable {
    private final String object;

    public SingleResponse(String object) {
        this.object = object;
    }

    public SingleResponse(Entity entity) throws JsonProcessingException {
        this.object = entity.asJson();
    }

    public String object() {
        return object;
    }
}
