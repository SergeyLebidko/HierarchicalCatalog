package hierarchicalcatalog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class MainClass {

    private static DBHandler dbHandler;
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

}
