package server.connection.request;

/**
 * Abstract class representing a generic request with a command and user identifier.
 */
public abstract class Request {
    private String command;
    private String userCf;

    public String getUserCf() {
        return userCf;
    }

    public void setUserCf(String userCf) {
        this.userCf = userCf;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
