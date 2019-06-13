package hierarchicalcatalog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static hierarchicalcatalog.ElementType.*;

public class GUI {

    private static final int FRM_WIDTH = 500;
    private static final int FRM_HEIGHT = 800;

    private Resources resources;
    private ActionHandler actionHandler;
    private  DBHandler dbHandler;
    private JFrame frm;
    private CatalogTable catalogTable;

    private JButton addBtn;
    private JButton addDirBtn;
    private JButton removeBtn;

    public GUI() {
        resources = MainClass.getResources();
        actionHandler = MainClass.getActionHandler();
        dbHandler = MainClass.getDbHandler();

        frm = new JFrame("Hierarchical Catalog");
        frm.setIconImage(resources.getImage("logo"));
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(FRM_WIDTH, FRM_HEIGHT);
        frm.setResizable(false);
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - FRM_WIDTH / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - FRM_HEIGHT / 2;
        frm.setLocation(xPos, yPos);

        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        frm.setContentPane(contentPane);

        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        toolBar.setFloatable(false);
        contentPane.add(toolBar, BorderLayout.NORTH);

        addDirBtn = new JButton(resources.getImageIcon("add_dir"));
        addDirBtn.setToolTipText("Добавить группу");
        toolBar.add(addDirBtn);
        toolBar.add(Box.createHorizontalStrut(3));

        addBtn = new JButton(resources.getImageIcon("add"));
        addBtn.setToolTipText("Добавить элемент каталога");
        toolBar.add(addBtn);
        toolBar.add(Box.createHorizontalStrut(3));

        removeBtn = new JButton(resources.getImageIcon("remove"));
        removeBtn.setToolTipText("Удалить");
        toolBar.add(removeBtn);

        catalogTable = new CatalogTable();

        addDirBtn.addActionListener(addDirBtnListener);
        addBtn.addActionListener(addBtnListener);
        removeBtn.addActionListener(removeBtnListener);
        frm.addWindowListener(frmCloseListener);

        //Внедряем в класс логики объект, который будет отображать данные
        MainClass.getActionHandler().setCatalogTable(catalogTable);

        //И сразу же даем команду отобразить корневой каталог справочника
        MainClass.getActionHandler().showElements(null);

        frm.add(catalogTable.getVisualComponent(), BorderLayout.CENTER);

        frm.setVisible(true);
    }

    private ActionListener addBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            actionHandler.addElement(ELEMENT);
        }
    };

    private ActionListener addDirBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            actionHandler.addElement(DIR);
        }
    };

    private ActionListener removeBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            actionHandler.removeElement();
        }
    };

    private WindowListener frmCloseListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            dbHandler.dispose();
        }
    };

}
