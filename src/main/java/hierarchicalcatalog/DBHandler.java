package hierarchicalcatalog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {

    //Параметры подключения к базе данных
    private static final String jdbcClassName = "org.sqlite.JDBC";
    private static final String databaseConnectionString = "jdbc:sqlite:database\\database.db";

    private Connection connection;
    private Statement statement;

    public DBHandler() throws Exception{
        Class.forName(jdbcClassName);
        connection = DriverManager.getConnection(databaseConnectionString);
        statement = connection.createStatement();
    }

    public void dispose(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {}
    }

}
