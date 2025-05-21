package server.router;

import database.models.Entity;
import utils.JSONParser;

import java.util.function.Consumer;

/**
 * CommandHandler is a generic class that handles commands for different types of entities.
 * It uses a Consumer to define the action to be performed on the entity.
 *
 * @param <T> The type of entity this handler will work with.
 */
public class CommandHandler<T extends Entity> implements Executable {
    private final Consumer<T> action;
    private final Class<T> entityType;

    public CommandHandler(Consumer<T> action, Class<T> entityType) {
        this.action = action;
        this.entityType = entityType;
    }

    /**
     * Executes the action on the entity parsed from the given JSON string.
     *
     * @param args The JSON string representing the entity.
     * @throws Exception If parsing fails or if the action throws an exception.
     */
    public void execute(String args) throws Exception {
        T entity = (T) JSONParser.parse(args, entityType);
        action.accept(entity);
    }
}
