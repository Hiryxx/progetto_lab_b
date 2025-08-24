import database.models.User;
import database.query.PrepareQuery;
import database.query.QueryResult;
import server.router.CommandRegister;
import server.Server;
import utils.DbUtil;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        startServer();
    }

    private static void startServer() {
        try (Server server = new Server()) {
            server.setup();
            server.start(9000);
        } catch (Exception e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }


   /* private static void tryDb() throws SQLException, IllegalAccessException {
        Scanner in = new Scanner(System.in);

        int res;
        do {
            System.out.println("1. Create user table");
            System.out.println("2. Fill user table");
            System.out.println("3. Print user table");
            System.out.println("0. Exit");
            res = in.nextInt();

            switch (res) {
                case 1 -> {
                    DbUtil.init(User.class);
                    System.out.println("User table created");
                }
                case 2 -> {
                    System.out.println("Insert user cf");
                    String cf = in.next();
                    System.out.println("Insert user name");
                    String name = in.next();
                    System.out.println("Insert user email");
                    String email = in.next();
                    System.out.println("Insert user password");
                    String password = in.next();

                    User user = new User(cf, name, email, password);
                    user.create();
                }
                case 3 -> {
                    PrepareQuery query = User.selectBy("*").prepare();

                    System.out.println("Executing query: " + query);
                    try (QueryResult result = query.executeResult()) {
                        for (var r : result) {
                            String cf = r.getString("cf");
                            String name = r.getString("name");
                            String email = r.getString("email");
                            String password = r.getString("password");

                            System.out.println("USER: " + cf + " " + name + " " + email + " " + password);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        } while (res != 0);
    }*/
}