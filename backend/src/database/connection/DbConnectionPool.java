package database.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionPool {

    private Connection connection;

    private static ComboPooledDataSource dataSource;

    static {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/database_name");
            dataSource.setUser("username");
            dataSource.setPassword("password");

            // Pool configuration
            dataSource.setMinPoolSize(5);
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
