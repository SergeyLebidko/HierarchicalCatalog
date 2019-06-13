package hierarchicalcatalog;

import java.sql.*;
import java.util.ArrayList;

import static hierarchicalcatalog.ElementType.*;

public class DBHandler {

    //Параметры подключения к базе данных
    private static final String jdbcClassName = "org.sqlite.JDBC";
    private static final String databaseConnectionString = "jdbc:sqlite:database\\database.db";

    private Connection connection;
    private Statement statement;

    public DBHandler() throws Exception {
        Class.forName(jdbcClassName);
        connection = DriverManager.getConnection(databaseConnectionString);
        statement = connection.createStatement();
    }

    public void dispose() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
        }
    }

    public CatalogElement getElement(int id) throws SQLException {
        String query = "SELECT * FROM CATALOG WHERE ID=" + id;
        ResultSet resultSet = statement.executeQuery(query);

        int intType;
        ElementType type = null;
        int parent;
        String name;

        resultSet.next();
        intType = resultSet.getInt(2);
        if (intType == 0) type = DIR;
        if (intType == 1) type = ELEMENT;

        parent = resultSet.getInt(3);
        name = resultSet.getString(4);
        return new CatalogElement(id, type, parent, name);
    }

    public ArrayList<CatalogElement> getElementsList(CatalogElement rootElement) throws SQLException {
        int parentId = 0;
        if (rootElement != null) {
            parentId = rootElement.getId();
        }

        ArrayList<CatalogElement> list = new ArrayList<>();

        String query = "SELECT * FROM CATALOG WHERE PARENT=" + parentId;
        ResultSet resultSet = statement.executeQuery(query);
        int id;
        int intType;
        ElementType type = null;
        int parent;
        String name;
        while (resultSet.next()) {
            id = resultSet.getInt(1);

            intType = resultSet.getInt(2);
            if (intType == 0) type = DIR;
            if (intType == 1) type = ELEMENT;

            parent = resultSet.getInt(3);
            name = resultSet.getString(4);

            list.add(new CatalogElement(id, type, parent, name));
        }

        return list;
    }

    public void addElement(ElementType type, int parent, String name) throws SQLException {
        String query = "INSERT INTO CATALOG (TYPE, PARENT, NAME) " +
                "VALUES(" + type.getIntType() + ", " + parent + ", \"" + name + "\")";
        statement.executeUpdate(query);
    }

    public void removeElement(CatalogElement element) throws SQLException {
        if (element.getType() == ELEMENT) {
            String query = "DELETE FROM CATALOG WHERE ID=" + element.getId();
            statement.executeUpdate(query);
            return;
        }
        if (element.getType() == DIR) {
            ArrayList<CatalogElement> list = getElementsList(element);
            for (CatalogElement listElement : list) {
                removeElement(listElement);
            }
            String query = "DELETE FROM CATALOG WHERE ID=" + element.getId();
            statement.executeUpdate(query);
            return;
        }
    }

}
