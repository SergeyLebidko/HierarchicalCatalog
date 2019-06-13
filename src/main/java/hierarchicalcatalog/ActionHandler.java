package hierarchicalcatalog;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActionHandler {

    private CatalogTable catalogTable;
    private DBHandler dbHandler;

    public void setCatalogTable(CatalogTable catalogTable) {
        this.catalogTable = catalogTable;
    }

    public void setDbHandler(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public CatalogElement getElement(int id) throws SQLException {
        return dbHandler.getElement(id);
    }

    public void showElements(CatalogElement rootElement) {
        ArrayList<CatalogElement> list;
        try {
            list = dbHandler.getElementsList(rootElement);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Не удалось получить список элементов. Ошибка: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catalogTable.refresh(list);
    }

    public void addElement(ElementType type) {
        String name;
        while (true) {
            name = JOptionPane.showInputDialog(null, "Введите наименование", "", JOptionPane.PLAIN_MESSAGE);
            if (name == null) return;
            name = name.trim();
            if (name.equals("")) continue;
            if (name.indexOf("_") != (-1) | name.indexOf("%") != (-1)) continue;
            break;
        }

        int parent = 0;
        CatalogElement rootElement = catalogTable.getRootElement();
        if (rootElement != null) {
            parent = rootElement.getId();
        }

        try {
            dbHandler.addElement(type, parent, name);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Не удалось добавить элемент в БД. Ошибка: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showElements(rootElement);
    }

    public void removeElement() {
        CatalogElement element = catalogTable.getSelectedeElement();
        if (element == null) return;

        try {
            dbHandler.removeElement(element);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Не удалось удалить элемент. Ошибка: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showElements(catalogTable.getRootElement());
    }

}
