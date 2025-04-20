import database.models.User;
import utils.DbUtil;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
       //User user = new User();
        // Need to pass an instance unless we have static attributes
        DbUtil.init(User.class);

        //user.create();

  /*
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
        //
    }
}