package server.router;

import java.util.Optional;

/**
 * NoInputCommandHandler is a class that handles commands that don't require any input parameters.
 * It uses a Runnable to define the action to be performed.
 */
public class NoInputCommandHandler implements Executable {
    private final Runnable action;

    public NoInputCommandHandler(Runnable action) {
        this.action = action;
    }

    /**
     * Executes the action with no input parameters.
     *
     * @throws Exception If the action throws an exception.
     */
    public void execute(Optional<String> args) throws Exception {
        assert args.isEmpty();
        action.run();
    }
}
