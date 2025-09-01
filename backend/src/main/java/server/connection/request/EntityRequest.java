package server.connection.request;

import database.models.base.Entity;

/**
 * EntityRequest is a class that represents a request containing an entity.
 * It extends the Request class and holds an entity of type T, where T is a subclass of Entity.
 *
 * @param <T> The type of the entity contained in the request, which must extend the Entity class.
 */
public class EntityRequest<T extends Entity> extends Request {
    private T entity;

    public EntityRequest(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
