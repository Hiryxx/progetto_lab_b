package server.router;

import database.models.base.Entity;
import server.connection.request.EntityRequest;
import server.connection.request.Request;
import server.connection.request.StringRequest;
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
    public <T extends Entity> void register(String command, Function<EntityRequest<T>, Sendable> action, Class<T> entityType) {
        commands.put(command, new CommandHandler<>(action, entityType));
    }

    /***
     Register an endpoint with its function that takes a string as input
     * @param command The command to register
     * @param action The action to execute when the command is called
     */
    public void register(String command, Function<StringRequest, Sendable> action) {
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
     Parse a request from the given parts
     * @param parts The parts of the request
     * @return The parsed request
     * @throws Exception if the command is not found or if the arguments are invalid
     */
    public Request parseRequest(String[] parts) throws Exception {
        if (parts.length < 1) {
            throw new InvalidArgs("Error: Invalid command format. You need to provide a command.");
        }

        String command = parts[0].toUpperCase();
        // Checks if it has a json argument
        Optional<String> args = parts.length > 1 ? Optional.of(parts[1]) : Optional.empty();

        String userCf = parts.length > 2 ? parts[2] : null;

        if (!isFreeCommand(command) && userCf == null) {
            throw new InvalidArgs("You need to provide a userCf for this command.");
        }

        Executable executable = commands.get(command);
        Request request = executable.parseRequest(command, args);
        request.setCommand(command);

        request.setUserCf(userCf);
        return request;
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

    /***
     Execute a command with the given arguments
     * @param request The request containing the command and its arguments
     * @return The response from the command execution
     */
    public Sendable execute(Request request) {
        String command = request.getCommand();
        Executable executable = commands.get(command);
        if (executable == null) {
            throw new RuntimeException("Command not found: " + command);
        }
        try {
            return executable.execute(request);
        } catch (Exception e) {
            throw new RuntimeException("Error executing command: " + command, e);
        }
    }
}


