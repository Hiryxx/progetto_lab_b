package server.router.executors;

import server.router.connection.response.Sendable;

import java.util.Optional;
import java.util.function.Function;

public class StringSplitCommandHandler implements Executable {
    private final Function<String, Sendable> action;

    public StringSplitCommandHandler(Function<String, Sendable> action) {
        this.action = action;
    }

    @Override
    public Sendable execute(Optional<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided");
        }
        return action.apply(args.get());
    }
}
