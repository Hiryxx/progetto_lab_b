package server.router.executors;

import server.router.connection.response.Sendable;

import java.util.Optional;
import java.util.function.Function;

/**
 * StringCommandHandler is a command handler that takes a string as input and executes the action.
 * It implements the Executable interface.
 */
public class StringCommandHandler implements Executable {
    private final Function<String, Sendable> action;

    public StringCommandHandler(Function<String, Sendable> action) {
        this.action = action;
    }

    /**
     * Executes the action with the provided optional string arguments.
     *
     * @param args The arguments to pass to the action
     * @return The result of the action
     * @throws Exception if an error occurs during execution
     */
    @Override
    public Sendable execute(Optional<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided");
        }
        return action.apply(args.get());
    }
}
