package server.router;

import database.models.Entity;
import server.router.connection.response.Sendable;
import server.router.executors.CommandHandler;
import server.router.executors.Executable;
import server.router.executors.NoInputCommandHandler;
import server.router.executors.StringCommandHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class Router {
    private Map<String, Executable> commands = new ConcurrentHashMap<>();

    /***
     Register an endpoint with its function
     * @param command The command to register
     * @param action The action to execute when the command is called
     * @param entityType The type of the entity to be passed to the action
     */
    public <T extends Entity> void register(String command, Function<T, Sendable> action, Class<T> entityType) {
        commands.put(command, new CommandHandler<>(action, entityType));
    }

    /***
     Register an endpoint with its function that takes a string as input
     * @param command The command to register
     * @param action The action to execute when the command is called
     */
    public void register(String command, Function<String, Sendable> action) {
        commands.put(command, new StringCommandHandler(action));
    }

    /***
     Register an endpoint with no input
     * @param command The command to register
     * @param action The action to execute when the command is called
     */
    public void register(String command, Supplier<Sendable> action) {
        commands.put(command, new NoInputCommandHandler(action));
    }

    /***
     Execute a command with the given arguments
     * @param command The command to execute
     * @param args The arguments to pass to the command
     */
    public Sendable execute(String command, Optional<String> args) throws Exception {
        Executable executable = commands.get(command);
        if (executable == null) {
            throw new RouterNotFoundException("Command not found: " + command);
        }
        return executable.execute(args);
    }
}


