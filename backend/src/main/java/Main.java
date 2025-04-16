import database.models.User;
import database.types.query.Query;
import database.types.query.QueryResult;
import utils.DbUtil;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        User user = new User("John Doe", "john@gmail.com", "password");
        // Need to pass an instance unless we have static attributes
        DbUtil.init(user);

        user.create();


        QueryResult res = User.selectBy("name")
                .where("name='John Doe'")
                .build()
                .execute();

        for (var row : res) {
            System.out.println(row);
        }
        //
    }
}