package server.router;
import database.models.Entity;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Router {
    private Map<String, Executable> commands = new ConcurrentHashMap<>();

    /***
        Register an endpoint with its function
     */
    public <T extends Entity> void register(String command, Consumer<T> action, Class<T> entityType) {
        commands.put(command, new CommandHandler<>(action, entityType));
    }

    public void register(String command, Runnable action) {
        commands.put(command, new NoInputCommandHandler(action));
    }

    public void execute(String command, Optional<String> args) throws Exception {
        Executable executable = commands.get(command);
        if (executable == null) {
            throw new RouterNotFoundException("Command not found: " + command);
        }
        executable.execute(args);
        /*
         if (executable instanceof CommandHandler<?>) {
            executable.execute(args);
        } else if (executable instanceof NoInputCommandHandler) {
            executable.execute("");
        }
         */
    }
}


