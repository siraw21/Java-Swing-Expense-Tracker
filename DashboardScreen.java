// DashboardScreen.java
// Main screen: shows all expenses in a JTable with totals.
// Buttons: Add, Edit, Delete, Search, back to Home.

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardScreen extends JPanel {

    private final JFrame frame;
    private final ExpenseDAO dao = new ExpenseDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel countLabel;

    private JTextField searchField;

    private static final String[] COLS = {"ID", "Date", "Category", "Amount (€)", "Description"};

    public DashboardScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 250));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildMain(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        loadTable();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(41, 98, 255));
        bar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JButton homeBtn = HomeScreen.makeButton("← Home", new Color(255, 255, 255, 60));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setPreferredSize(new Dimension(90, 32));
        homeBtn.setMaximumSize(new Dimension(90, 32));
        homeBtn.addActionListener(e -> {
            frame.setContentPane(new HomeScreen(frame));
            frame.revalidate();
            frame.repaint();
        });

        JLabel title = new JLabel("📊  Expense Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        left.add(homeBtn);
        left.add(Box.createHorizontalStrut(8));
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton searchBtn = HomeScreen.makeButton("🔍 Search", new Color(255, 255, 255, 60));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(100, 32));
        searchBtn.setMaximumSize(new Dimension(100, 32));
        searchBtn.addActionListener(e -> doSearch());

        JButton resetBtn = HomeScreen.makeButton("↺ Reset", new Color(255, 255, 255, 40));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setPreferredSize(new Dimension(80, 32));
        resetBtn.setMaximumSize(new Dimension(80, 32));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            loadTable();
        });

        right.add(searchField);
        right.add(searchBtn);
        right.add(resetBtn);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        main.add(buildSummaryRow(), BorderLayout.NORTH);
        main.add(buildTablePanel(), BorderLayout.CENTER);
        main.add(buildActionRow(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel buildSummaryRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        totalLabel = new JLabel("€ 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(new Color(41, 98, 255));

        countLabel = new JLabel("0 records");
        countLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countLabel.setForeground(new Color(16, 160, 100));

        row.add(summaryCard("💰  Total Spent", totalLabel, new Color(235, 242, 255)));
        row.add(summaryCard("📋  Total Records", countLabel, new Color(235, 252, 244)));

        return row;
    }

    private JPanel summaryCard(String title, JLabel valueLabel, Color bg) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 240), 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.PLAIN, 11));
        t.setForeground(new Color(100, 110, 140));

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        return card;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 3 ? Double.class : String.class;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 228, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 98, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 34));

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(280);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (v instanceof Double d) setText(String.format("€ %.2f", d));
                return this;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                    setForeground(new Color(30, 30, 50));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 230), 1));
        return scroll;
    }

    private JPanel buildActionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JButton addBtn = HomeScreen.makeButton("➕  Add Expense", new Color(16, 160, 100));
        JButton editBtn = HomeScreen.makeButton("✏️  Edit Expense", new Color(41, 98, 255));
        JButton delBtn = HomeScreen.makeButton("🗑  Delete Expense", new Color(220, 60, 60));

        addBtn.setPreferredSize(new Dimension(160, 36));
        editBtn.setPreferredSize(new Dimension(160, 36));
        delBtn.setPreferredSize(new Dimension(160, 36));

        addBtn.addActionListener(e -> openAddForm());
        editBtn.addActionListener(e -> openEditForm());
        delBtn.addActionListener(e -> deleteSelected());

        row.add(addBtn);
        row.add(editBtn);
        row.add(delBtn);
        return row;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(235, 238, 245));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 215, 225)));
        JLabel lbl = new JLabel("  Tip: select a row, then click Edit or Delete");
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(new Color(130, 140, 160));
        bar.add(lbl);
        return bar;
    }

    public void loadTable() {
        try {
            List<Expense> list = dao.getAll();
            double total = dao.getTotal();
            fillTable(list, total);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillTable(List<Expense> list, double total) {
        tableModel.setRowCount(0);
        for (Expense e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getDate(), e.getCategory(), e.getAmount(), e.getDescription()
            });
        }
        totalLabel.setText(String.format("€ %.2f", total));
        countLabel.setText(list.size() + " record" + (list.size() == 1 ? "" : "s"));
    }

    private void doSearch() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) {
            loadTable();
            return;
        }
        try {
            List<Expense> results = dao.search(kw);
            double total = results.stream().mapToDouble(Expense::getAmount).sum();
            fillTable(results, total);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddForm() {
        ExpenseFormDialog dlg = new ExpenseFormDialog(frame, null, dao);
        dlg.setVisible(true);
        if (dlg.wasSaved()) loadTable();
    }

    private void openEditForm() {
        Expense sel = getSelectedExpense();
        if (sel == null) {
            showInfo("Please select a row to edit.");
            return;
        }
        ExpenseFormDialog dlg = new ExpenseFormDialog(frame, sel, dao);
        dlg.setVisible(true);
        if (dlg.wasSaved()) loadTable();
    }

    private void deleteSelected() {
        Expense sel = getSelectedExpense();
        if (sel == null) {
            showInfo("Please select a row to delete.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this,
                "Delete the " + sel.getCategory() + " expense of € " + sel.getAmount() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (ok == JOptionPane.YES_OPTION) {
            try {
                dao.delete(sel.getId());
                loadTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Expense getSelectedExpense() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) tableModel.getValueAt(row, 0);
        String date = (String) tableModel.getValueAt(row, 1);
        String cat = (String) tableModel.getValueAt(row, 2);
        double amt = (double) tableModel.getValueAt(row, 3);
        String desc = (String) tableModel.getValueAt(row, 4);
        return new Expense(id, amt, cat, date, desc);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}