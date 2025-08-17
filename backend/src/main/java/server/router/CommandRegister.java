package server.router;

import database.models.base.Entity;
import server.connection.response.Sendable;
import server.executors.CommandHandler;
import server.executors.Executable;
import server.executors.NoInputCommandHandler;
import server.executors.StringCommandHandler;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The Router class is responsible for routing commands to their corresponding actions.
 * It allows registering commands with different types of input and executing them.
 */
public class CommandRegister {
    private final Map<String, Executable> commands = new HashMap<>();

    /***
     Set of commands that can be executed without authentication
     * This is used to allow public access to certain commands
     */
    private final Set<String> freeCommands = new HashSet<>();

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
     * @param args The arguments to pass to the command (optional)
     */
    public Sendable execute(String command, Optional<String> args) throws Exception {
        Executable executable = commands.get(command);
        if (executable == null) {
            throw new RouterNotFoundException("Command not found: " + command);
        }
        return executable.execute(args);
    }

    /***
     Set a command that can be executed without authentication
     * @param command The command to set
     */
    public void setFreeCommand(String command) {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be null or empty");
        }
        if (!commands.containsKey(command)) {
            throw new RuntimeException("Command not found: " + command);
        }
        freeCommands.add(command);
    }

    /***
     Check if a command is free (can be executed without authentication)
     * @param command The command to check
     * @return true if the command is free, false otherwise
     */
    public boolean isFreeCommand(String command) {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be null or empty");
        }
        return freeCommands.contains(command);
    }
}


