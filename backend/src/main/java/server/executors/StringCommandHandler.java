package server.executors;

import server.connection.request.Request;
import server.connection.request.StringRequest;
import server.connection.response.Sendable;

import java.util.Optional;
import java.util.function.Function;

/**
 * StringCommandHandler is a command handler that takes a string as input and executes the action.
 * It implements the Executable interface.
 */
public class StringCommandHandler implements Executable {
    private final Function<StringRequest, Sendable> action;

    public StringCommandHandler(Function<StringRequest, Sendable> action) {
        this.action = action;
    }

    /**
     * Executes the action with the provided optional string arguments.
     *
     * @param request The request to pass to the action
     * @return The result of the action
     * @throws Exception if an error occurs during execution
     */
    @Override
    public Sendable execute(Request request) throws Exception {
        return action.apply((StringRequest) request);
    }

    @Override
    public Request parseRequest(String command, Optional<String> args)  {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided");
        }
        return new StringRequest(args.get());
    }
}
