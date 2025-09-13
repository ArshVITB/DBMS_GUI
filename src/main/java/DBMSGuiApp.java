import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;

public class DBMSGuiApp extends JFrame {
    private JTextArea queryArea;
    private JButton executeButton;
    private JTextField filterField;
    private JButton filterButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public DBMSGuiApp() {
        // Set a professional look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(240, 240, 240));
            UIManager.put("nimbusBase", new Color(0, 102, 204));
            UIManager.put("nimbusBlueGrey", new Color(200, 200, 200));
            UIManager.put("text", new Color(50, 50, 50));
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex2) {}
        }

        setTitle("DBMS GUI Application");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Set a professional font
        Font uiFont = new Font("Arial", Font.PLAIN, 16);
        UIManager.put("Label.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        UIManager.put("TextArea.font", uiFont);

        // Initialize components with professional styling
        queryArea = new JTextArea(8, 40);
        queryArea.setFont(uiFont);
        queryArea.setBackground(new Color(255, 255, 255));
        queryArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        executeButton = new JButton("Execute Query");
        executeButton.setFont(uiFont);
        executeButton.setBackground(new Color(0, 102, 204));
        executeButton.setForeground(Color.WHITE);
        executeButton.setFocusPainted(false);
        executeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        filterField = new JTextField(25);
        filterField.setFont(uiFont);
        filterField.setBackground(new Color(255, 255, 255));
        filterField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        filterButton = new JButton("Filter");
        filterButton.setFont(uiFont);
        filterButton.setBackground(new Color(0, 102, 204));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);
        filterButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setFont(uiFont);
        resultTable.setRowHeight(25);
        resultTable.setBackground(new Color(255, 255, 255));
        resultTable.setGridColor(new Color(220, 220, 220));
        resultTable.getTableHeader().setFont(uiFont.deriveFont(Font.BOLD));
        resultTable.getTableHeader().setBackground(new Color(0, 102, 204));
        resultTable.getTableHeader().setForeground(new Color(64, 64, 64));

        // Professional title label
        JLabel titleLabel = new JLabel("DBMS GUI Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(240, 240, 240));
        add(titleLabel, BorderLayout.NORTH);

        // Top panel for query input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        JLabel queryLabel = new JLabel("SQL Query:");
        queryLabel.setFont(uiFont);
        topPanel.add(queryLabel);
        topPanel.add(new JScrollPane(queryArea));
        topPanel.add(executeButton);

        // Middle panel for filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filterPanel.setBackground(new Color(240, 240, 240));
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(uiFont);
        filterPanel.add(filterLabel);
        filterPanel.add(filterField);
        filterPanel.add(filterButton);

        // Results table with styled scroll pane
        JScrollPane resultsScrollPane = new JScrollPane(resultTable);
        resultsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        resultsScrollPane.setPreferredSize(new Dimension(950, 400));

        // Main panel with consistent background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(resultsScrollPane, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Setup database connection
        setupDatabaseConnection();

        // Add action listeners
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeQuery();
            }
        });
        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterResults();
            }
        });
        queryArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    executeQuery();
                    if (!filterField.getText().trim().isEmpty()) {
                        filterResults();
                    }
                }
            }
        });
        filterField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    if (filterField.getText().trim().isEmpty()) {
                        executeQuery();
                    } else {
                        filterResults();
                    }
                }
            }
        });
    }

    private void setupDatabaseConnection() {
        String user = "root";
        String password = "abcd1234";
        try {
            String url = "jdbc:mysql://localhost:3306";
            connection = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(this, "Connected to MySQL server successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void executeQuery() {
        String query = queryArea.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a query", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Statement stmt = connection.createStatement();
            boolean hasResultSet = stmt.execute(query);
            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    tableModel.addRow(row);
                }
                rs.close();
                autoResizeAllColumns();
            } else {
                int updateCount = stmt.getUpdateCount();
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);
                JOptionPane.showMessageDialog(this, "Query executed successfully. Rows affected: " + updateCount,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Query execution failed: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterResults() {
        String filterText = filterField.getText().trim().toLowerCase();
        if (filterText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a filter term", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int row = tableModel.getRowCount() - 1; row >= 0; row--) {
            boolean matches = false;
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Object value = tableModel.getValueAt(row, col);
                if (value != null && value.toString().toLowerCase().contains(filterText)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                tableModel.removeRow(row);
            }
        }
    }

    private void autoResizeColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= resultTable.getColumnCount()) {
            return;
        }

        TableColumn column = resultTable.getColumnModel().getColumn(columnIndex);
        int maxWidth = 0;

        TableCellRenderer renderer = resultTable.getCellRenderer(0, columnIndex);
        if (renderer == null) {
            renderer = resultTable.getDefaultRenderer(resultTable.getColumnClass(columnIndex));
        }

        Component headerComp = resultTable.getTableHeader().getDefaultRenderer()
                .getTableCellRendererComponent(resultTable, column.getHeaderValue(), false, false, -1, columnIndex);
        maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width + 20);

        for (int row = 0; row < resultTable.getRowCount(); row++) {
            Component cellComp = renderer.getTableCellRendererComponent(
                    resultTable, resultTable.getValueAt(row, columnIndex),
                    false, false, row, columnIndex);
            maxWidth = Math.max(maxWidth, cellComp.getPreferredSize().width + 20);
        }

        maxWidth = Math.max(maxWidth, 120);
        maxWidth = Math.min(maxWidth, 450);

        column.setPreferredWidth(maxWidth);
        column.setWidth(maxWidth);

        resultTable.revalidate();
        resultTable.repaint();
    }

    private void autoResizeAllColumns() {
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            autoResizeColumn(i);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DBMSGuiApp().setVisible(true);
            }
        });
    }
}
