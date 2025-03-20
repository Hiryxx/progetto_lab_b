import database.models.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        User user = new User("John Doe", "john@gmail.com", "password");

        user.create();

        //
    }
}