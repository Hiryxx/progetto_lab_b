package server.connection.request;

import database.models.base.Entity;

public class EntityRequest<T extends Entity> extends Request {
    private T entity;

    public EntityRequest(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
