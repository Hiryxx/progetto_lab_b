import database.models.User;
import database.query.QueryResult;
import utils.DbUtil;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
          /*
        DbUtil.init(User.class);

        User user = new User ("a", "b", "c", "d");

        user.create();




        QueryResult res = User.selectBy("name")
                .where("name='John Doe'")
                .build()
                .execute();

        for (var row : res) {
            System.out.println(row);
        }

        Query q = User.selectBy("*")
                .where("name='John Doe'")
                .orderBy("name")
                .build();

        System.out.println(q.getInnerQuery());
         */

        // todo fix name
        var res = User.selectBy("name")
                .where("name='John Doe'")
                .build();

        System.out.println(res.getInnerQuery());
    }
}