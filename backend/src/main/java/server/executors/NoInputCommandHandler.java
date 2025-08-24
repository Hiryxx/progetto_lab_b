package server.executors;

import server.connection.request.NoInputRequest;
import server.connection.request.Request;
import server.connection.response.Sendable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * NoInputCommandHandler is a class that handles commands that don't require any input parameters.
 * It uses a Runnable to define the action to be performed.
 */
public class NoInputCommandHandler implements Executable {
    private final Supplier<Sendable> action;

    public NoInputCommandHandler(Supplier<Sendable> action) {
        this.action = action;
    }

    /**
     * Executes the action with no input parameters.
     *
     * @throws Exception If the action throws an exception.
     */
    public Sendable execute(Request request) throws Exception {
        return action.get();
    }

    @Override
    public Request parseRequest(String command, Optional<String> args) {
        return new NoInputRequest();
    }
}
