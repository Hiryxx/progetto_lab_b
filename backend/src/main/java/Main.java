import database.connection.DbConnectionPool;
import server.Server;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        String HOST = "localhost";
        int PORT = 5432;
        String DB_NAME = "database";
        String DB_USER = "postgres";
        String DB_PASSWORD = "postgres";

        Scanner in = new Scanner(System.in);
        System.out.println("Per favore inserire le credenziali del database.");
        System.out.printf("Host (%s): ", HOST);
        String input = in.nextLine();

        if (!input.isBlank()) HOST = input;
        System.out.printf("Port (%d): ", PORT);
        input = in.nextLine();

        if (!input.isBlank()) PORT = Integer.parseInt(input);
        System.out.printf("Database name (%s): ", DB_NAME);
        input = in.nextLine();

        if (!input.isBlank()) DB_NAME = input;
        System.out.printf("User (%s): ", DB_USER);
        input = in.nextLine();

        if (!input.isBlank()) DB_USER = input;
        System.out.printf("Password (%s): ", DB_PASSWORD);
        input = in.nextLine();

        if (!input.isBlank()) DB_PASSWORD = input;

        DbConnectionPool.connect(HOST, String.valueOf(PORT), DB_NAME, DB_USER, DB_PASSWORD);


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