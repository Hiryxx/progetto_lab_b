package server.router;

import java.util.Optional;

public interface Executable {
    // TODO COULD RETURN SOMETHING IN THE FUTURE
    /**
     * Executes the command with the given arguments.
     *
     * @param args The arguments for the command.
     * @throws Exception If an error occurs during execution.
     */
    void execute(Optional<String> args) throws Exception;
}
