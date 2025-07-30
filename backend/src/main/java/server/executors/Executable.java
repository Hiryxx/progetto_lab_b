package server.executors;

import server.connection.response.Sendable;

import java.util.Optional;

public interface Executable {
    /**
     * Executes the command with the given arguments.
     *
     * @param args The arguments for the command.
     * @throws Exception If an error occurs during execution.
     */
    Sendable execute(Optional<String> args) throws Exception;
}
