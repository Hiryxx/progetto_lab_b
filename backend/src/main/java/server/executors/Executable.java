package server.executors;

import server.connection.request.Request;
import server.connection.response.Sendable;

import java.util.Optional;

/**
 * Executable is an interface that defines methods for executing commands and parsing requests.
 */
public interface Executable {
    /**
     * Executes the command with the given arguments.
     *
     * @param request The request for the command.
     * @throws Exception If an error occurs during execution.
     */
    Sendable execute(Request request) throws Exception;

    Request parseRequest(String command, Optional<String> args) throws Exception;
}
