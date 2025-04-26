package server.router;
import database.models.Entity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Router {
    private Map<String, CommandHandler<?>> commands = new ConcurrentHashMap<>();

    /***
        Register an endpoint with its function
     */
    public <T extends Entity> void register(String command, Consumer<T> action, Class<T> entityType) {
        commands.put(command, new CommandHandler<>(action, entityType));
    }

    public void execute(String command, String args) throws Exception {
        CommandHandler<?> handler = commands.get(command);
        if (handler != null) {
            handler.execute(args);
        } else {
            throw new RouterNotFoundException("Command not found: " + command);
        }
    }
}


