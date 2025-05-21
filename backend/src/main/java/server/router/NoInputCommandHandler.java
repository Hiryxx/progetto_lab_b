package server.router;

import java.util.function.Consumer;

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
     * Executes the action without needing any input parameters.
     *
     * @throws Exception If the action throws an exception.
     */
    public void execute(String args) throws Exception {
        action.run();
    }
}
