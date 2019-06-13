package hierarchicalcatalog;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import static hierarchicalcatalog.SortOrder.*;
import static hierarchicalcatalog.ElementType.*;

public class CatalogTable {

    private static final Font mainFont = new Font("Arial", Font.PLAIN, 16);
    private static final Color gridColor = Color.LIGHT_GRAY;
    private static final Color headerColor = new Color(230, 230, 230);
    private static final int ROW_HEIGHT = 28;

    private ActionHandler actionHandler;
    private int sortColumn;
    private SortOrder sortOrder;
    private Resources resources;

    private JPanel contentPane;
    private JTable table;
    private Model model;
    private CellRenderer cellRenderer;
    private HeaderRenderer headerRenderer;

    private CatalogElementComparator elementComparator;
    private CatalogElement rootElement;
    private ArrayList<CatalogElement> content;

    private class CatalogElementComparator implements Comparator<CatalogElement> {

        @Override
        public int compare(CatalogElement o1, CatalogElement o2) {
            if (sortColumn == 0) {
                Integer id1 = o1.getId();
                Integer id2 = o2.getId();
                return sortOrder.getMul() * id1.compareTo(id2);
            }
            if (sortColumn == 2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                return sortOrder.getMul() * name1.compareTo(name2);
            }
            return 0;
        }

    }

    private class Model extends AbstractTableModel {

        private int rowCount;
        private int columntCount;

        public Model() {
            rowCount = 0;
            columntCount = 3;
        }

        public void refresh() {
            if (content == null) return;

            ArrayList<CatalogElement> dirs = new ArrayList<>();
            ArrayList<CatalogElement> elements = new ArrayList<>();

            for (CatalogElement element: content){
                if (element.getType()==DIR){
                    dirs.add(element);
                    continue;
                }
                if (element.getType()==ELEMENT){
                    elements.add(element);
                }
            }
            content.clear();
            dirs.sort(elementComparator);
            elements.sort(elementComparator);

            if (sortColumn==1){
                if (sortOrder==TO_UP){
                    content.addAll(dirs);
                    content.addAll(elements);
                }
                if (sortOrder==TO_DOWN){
                    content.addAll(elements);
                    content.addAll(dirs);
                }
            }else{
                content.addAll(dirs);
                content.addAll(elements);
            }

            rowCount = content.size();
            if (rootElement != null) {
                rowCount++;
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            return columntCount;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rootElement != null) {
                if (rowIndex == 0) {
                    return rootElement;
                }
                return content.get(rowIndex - 1);
            }
            if (rootElement == null) {
                return content.get(rowIndex);
            }
            return null;
        }

    }

    private class CellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            CatalogElement element = (CatalogElement) value;

            if (column == 0) {
                lab.setText(element.getId() + "");
                lab.setHorizontalAlignment(SwingConstants.CENTER);
                lab.setIcon(null);
            }
            if (column == 1) {
                lab.setText("");
                lab.setHorizontalAlignment(SwingConstants.CENTER);
                if (element.getType() == DIR) {
                    if (rootElement != null) {
                        if (row == 0) lab.setIcon(resources.getImageIcon("open_dir"));
                        if (row != 0) lab.setIcon(resources.getImageIcon("dir"));
                    }
                    if (rootElement == null) {
                        lab.setIcon(resources.getImageIcon("dir"));
                    }
                }
                if (element.getType() == ELEMENT) {
                    lab.setIcon(resources.getImageIcon("element"));
                }
            }
            if (column == 2) {
                lab.setIcon(null);
                lab.setText(element.getName());
                lab.setHorizontalAlignment(SwingConstants.LEFT);
            }

            return lab;
        }

    }

    private class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 0) {
                lab.setText("№");
            }
            if (column == 1) {
                lab.setText("Тип");
            }
            if (column == 2) {
                lab.setText("Наименование");
            }

            lab.setBackground(headerColor);
            lab.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            lab.setHorizontalAlignment(SwingConstants.CENTER);

            if (column == sortColumn) {
                if (sortOrder == TO_UP) {
                    lab.setIcon(resources.getImageIcon("to_up"));
                }
                if (sortOrder == TO_DOWN) {
                    lab.setIcon(resources.getImageIcon("to_down"));
                }
            } else {
                lab.setIcon(resources.getImageIcon("no_order"));
            }

            return lab;
        }

    }

    public CatalogTable() {
        rootElement = null;
        content = null;
        sortColumn = 2;
        sortOrder = TO_UP;
        resources = MainClass.getResources();
        elementComparator = new CatalogElementComparator();
        actionHandler = MainClass.getActionHandler();

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));

        model = new Model();
        table = new JTable(model);
        cellRenderer = new CellRenderer();
        headerRenderer = new HeaderRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.setRowHeight(ROW_HEIGHT);
        table.setFont(mainFont);
        table.setShowVerticalLines(false);
        table.setGridColor(gridColor);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(mainFont);
        table.getColumnModel().getColumn(0).setMaxWidth(120);
        table.getColumnModel().getColumn(1).setMaxWidth(120);

        table.addMouseListener(tableClickListener);
        table.getTableHeader().addMouseListener(headerClickListener);

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public JPanel getVisualComponent() {
        return contentPane;
    }

    public void refresh(ArrayList<CatalogElement> list) {
        if (list == null) {
            content = new ArrayList<>();
        } else {
            content = list;
        }
        model.refresh();
    }

    public CatalogElement getRootElement() {
        return rootElement;
    }

    public CatalogElement getSelectedeElement() {
        int selectedeRow = table.getSelectedRow();
        if (selectedeRow == (-1)) return null;
        CatalogElement selectedElement = (CatalogElement) model.getValueAt(selectedeRow, 0);
        if (selectedElement == rootElement) return null;
        return selectedElement;
    }

    private void revertSortOrder() {
        SortOrder nextOrder = null;
        if (sortOrder == NO_ORDER) {
            nextOrder = TO_UP;
        }
        if (sortOrder == TO_UP) {
            nextOrder = TO_DOWN;
        }
        if (sortOrder == TO_DOWN) {
            nextOrder = TO_UP;
        }
        sortOrder = nextOrder;
        model.refresh();
        table.getTableHeader().repaint();
    }

    private MouseListener headerClickListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1 & e.getButton() == MouseEvent.BUTTON1) {
                int columnNumber = table.getTableHeader().columnAtPoint(e.getPoint());
                sortColumn = columnNumber;
                revertSortOrder();
            }
        }
    };

    private MouseListener tableClickListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 & e.getButton() == MouseEvent.BUTTON1) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == (-1)) return;
                CatalogElement selectedElement = (CatalogElement) model.getValueAt(selectedRow, 0);

                if (selectedElement == rootElement) {
                    if (rootElement.getParent() == 0) {
                        rootElement = null;
                        actionHandler.showElements(rootElement);
                        return;
                    }
                    if (rootElement.getParent() != 0) {
                        try {
                            rootElement = actionHandler.getElement(rootElement.getParent());
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Не удалось получить список элементов. Ошибка: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        actionHandler.showElements(rootElement);
                        return;
                    }
                }

                if (selectedElement.getType() == DIR) {
                    rootElement = selectedElement;
                    actionHandler.showElements(rootElement);
                    return;
                }

                if (selectedElement.getType() == ELEMENT) {
                    JOptionPane.showMessageDialog(null, selectedElement.getName(), "", JOptionPane.PLAIN_MESSAGE);
                }

            }
        }
    };

}
