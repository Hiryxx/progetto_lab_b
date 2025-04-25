package server.router;
import database.models.Entity;
import utils.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Router {
    private Map<String, CommandHandler<?>> commands = new HashMap<>();

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


