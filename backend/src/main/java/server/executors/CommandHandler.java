package server.executors;

import database.models.base.Entity;
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
    private final Function<T, Sendable> action;
    private final Class<T> entityType;

    public CommandHandler(Function<T, Sendable> action, Class<T> entityType) {
        this.action = action;
        this.entityType = entityType;
    }

    /**
     * Executes the action on the entity parsed from the given JSON string.
     *
     * @param args The JSON string representing the entity.
     * @throws Exception If parsing fails or if the action throws an exception.
     */
    public Sendable execute(Optional<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided");
        }

        T entity = (T) JSONUtil.parse(args.get(), entityType);
        return action.apply(entity);
    }
}
