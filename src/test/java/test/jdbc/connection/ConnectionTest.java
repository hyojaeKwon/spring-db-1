package test.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection c1 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        Connection c2 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        log.info("connectinos={} , class={}", c1, c1.getClass());
        log.info("connectinos={} , class={}", c2, c2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        useDateSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPassword(ConnectionConst.PASSWORD);
        dataSource.setMaximumPoolSize(8);
        dataSource.setPoolName("hikari");

        useDateSource(dataSource);
        Thread.sleep(5000);
    }
    private void useDateSource(DataSource ds) throws SQLException {
        Connection c1 = ds.getConnection();
        Connection c2 = ds.getConnection();

        log.info("connectinos={} , class={}", c1, c1.getClass());
        log.info("connectinos={} , class={}", c2, c2.getClass());
    }
}
