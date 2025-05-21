package server.router;

public interface Executable {
    // TODO COULD RETURN SOMETHING IN THE FUTURE
    /**
     * Executes the command with the given arguments.
     *
     * @param args The arguments for the command.
     * @throws Exception If an error occurs during execution.
     */
    public void execute(String args) throws Exception;
}
