package server.router;
import database.models.Entity;
import server.router.connection.response.Sendable;
import server.router.executors.CommandHandler;
import server.router.executors.Executable;
import server.router.executors.NoInputCommandHandler;
import server.router.executors.StringSplitCommandHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Router {
    private Map<String, Executable> commands = new ConcurrentHashMap<>();

    // TODO NEED TO ACCEPT STRING ARGUMENT?
    /***
        Register an endpoint with its function
     */
    public <T extends Entity> void register(String command, Function<T, Sendable> action, Class<T> entityType) {
        commands.put(command, new CommandHandler<>(action, entityType));
    }

    public void register(String command, Function<String, Sendable> action) {
        commands.put(command, new StringSplitCommandHandler(action));
    }


    public void register(String command, Supplier<Sendable> action) {
        commands.put(command, new NoInputCommandHandler(action));
    }

    public Sendable execute(String command, Optional<String> args) throws Exception {
        Executable executable = commands.get(command);
        if (executable == null) {
            throw new RouterNotFoundException("Command not found: " + command);
        }
        return executable.execute(args);
    }
}


