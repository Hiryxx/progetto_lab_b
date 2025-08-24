package server.executors;

import database.models.base.Entity;
import server.connection.request.EntityRequest;
import server.connection.request.Request;
import server.connection.response.Sendable;
import utils.JSONUtil;

import java.util.Optional;
import java.util.function.Function;

/**
 * CommandHandler is a generic class that handles commands for different types of entities.
 * It uses a Consumer to define the action to be performed on the entity.
 *
 * @param <T> The type of entity this handler will work with.
 */
public class CommandHandler<T extends Entity> implements Executable {
    private final Function<EntityRequest<T>, Sendable> action;
    private final Class<T> entityType;

    public CommandHandler(Function<EntityRequest<T>, Sendable> action, Class<T> entityType) {
        this.action = action;
        this.entityType = entityType;
    }

    /**
     * Executes the action on the entity parsed from the given JSON string.
     *
     * @param request The JSON string representing the entity.
     * @throws Exception If parsing fails or if the action throws an exception.
     */
    public Sendable execute(Request request) throws Exception {
        return action.apply((EntityRequest<T>) request);
    }

    @Override
    public Request parseRequest(String command, Optional<String> args) throws Exception {
        T entity = (T) JSONUtil.parse(args.get(), entityType);
        return new EntityRequest<>(entity);
    }
}
