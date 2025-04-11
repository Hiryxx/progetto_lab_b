package database.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DbConnectionPool {
    private static ComboPooledDataSource dataSource;

    static {
        String driverName = "org.postgresql.Driver";

        String currentDir = System.getProperty("user.dir");

        // todo make an example .env file so it is a fallback
        Dotenv dotenv = Dotenv.configure()
                .directory(currentDir + "/backend/")
                .filename(".env")
                .load();

        String host = dotenv.get("HOST");
        String port = dotenv.get("PORT");
        String dbName = dotenv.get("DB_NAME");

        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        if (host == null || port == null || dbName == null) {
            throw new RuntimeException("Database connection parameters are not set");
        }

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

        System.out.println(url);

        //System.out.println("Connecting to database: " + host + ":" + port + "/" + dbName);
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverName);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPassword);

            // Pool configuration
            dataSource.setMinPoolSize(3);
            dataSource.setMaxPoolSize(20);
            dataSource.setMaxIdleTime(300); // 5 minutes
        } catch (Exception e) {
            throw new RuntimeException("Error initializing data source", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
