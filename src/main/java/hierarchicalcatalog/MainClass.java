package hierarchicalcatalog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class MainClass {

    private static DBHandler dbHandler;
    private static Resources resources;
    private static GUI gui;
    private static ActionHandler actionHandler;

    public static void main(String[] args) {

        //Создаем подключение к БД
        try {
            dbHandler = new DBHandler();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Не удалось создать подключение к БД. Ошибка: "+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Получаем доступ к графическим ресурсам
        resources = new Resources();

        //Создаем класс "логики"
        actionHandler = new ActionHandler();

        //Создаем главное окно
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    gui = new GUI();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Не удалось создать графический интерфейс. Ошибка: "+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public static DBHandler getDbHandler() {
        return dbHandler;
    }

    public static Resources getResources() {
        return resources;
    }

    public static GUI getGui() {
        return gui;
    }

    public static ActionHandler getActionHandler() {
        return actionHandler;
    }

}
