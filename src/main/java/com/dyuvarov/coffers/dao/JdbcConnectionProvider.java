package com.dyuvarov.coffers.dao;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/** Creates database connections. Reads configs from db.properties file */
@ApplicationScoped
@Log4j
public class JdbcConnectionProvider {
    /** JDBC connection url */
    private final String url;

    /** Database username */
    private final String user;

    /** Database password */
    private final String password;

    public JdbcConnectionProvider() throws IOException {
        InputStream inputStream  = JdbcConnectionProvider.class.getClassLoader().getResourceAsStream("db.properties");
        Properties prop = new Properties();
        prop.load(inputStream);

        this.url = prop.getProperty("jdbc-url");
        this.user = prop.getProperty("user");
        this.password = prop.getProperty("password");
    }

    /** Create and return database connection */
    @SneakyThrows
    public Connection getConnection() {
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(url, user, password);
    }
}
