package hierarchicalcatalog;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private static final int FRM_WIDTH = 500;
    private static final int FRM_HEIGHT = 800;

    private Resources resources;
    private JFrame frm;

    public GUI() {
        resources = new Resources();
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

        frm.setVisible(true);
    }
}
