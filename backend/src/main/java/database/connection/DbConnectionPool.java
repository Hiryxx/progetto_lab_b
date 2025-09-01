package database.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DbConnectionPool is a singleton class that manages a connection pool to a PostgresSQL database.
 * It uses the C3P0 library for connection pooling.
 * The database connection parameters are loaded from a .env file.
 */
public class DbConnectionPool {
    private static ComboPooledDataSource dataSource;

    public static void connect(String host, String port, String dbName, String dbUser, String dbPassword) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        String driverName = "org.postgresql.Driver";

        // System.out.println(url);

        //System.out.println("Connecting to database: " + host + ":" + port + "/" + dbName);
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverName);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPassword);

            // Pool configuration
            dataSource.setMinPoolSize(5);
            dataSource.setInitialPoolSize(5);
            dataSource.setMaxPoolSize(30);
            dataSource.setAcquireIncrement(5);
            dataSource.setMaxIdleTime(300);
            dataSource.setMaxConnectionAge(900);
            dataSource.setCheckoutTimeout(5000);
            dataSource.setIdleConnectionTestPeriod(60);
            dataSource.setTestConnectionOnCheckin(true);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing data source", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
