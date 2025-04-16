import database.models.User;
import database.types.query.Query;
import utils.DbUtil;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        User user = new User("John Doe", "john@gmail.com", "password");
        // Need to pass an instance unless we have static attributes
        DbUtil.init(user);

        user.create();


        var res = User.selectBy("name")
                .where("name = 'John Doe'")
                .build()
                .execute();

        for (Query it = res; it.hasNext(); ) {
            var r = it.next();


        }
        //
    }
}