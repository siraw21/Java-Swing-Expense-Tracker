// ExpenseFormDialog.java
// A simple modal dialog used for both Add and Edit.
// Pass expense=null for Add mode, or an existing Expense for Edit mode.

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ExpenseFormDialog extends JDialog {

    private final ExpenseDAO dao;
    private final Expense existing; // null = Add mode

    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;
    private JTextField descField;

    private boolean saved = false;

    public ExpenseFormDialog(JFrame parent, Expense existing, ExpenseDAO dao) {
        super(parent, existing == null ? "Add New Expense" : "Edit Expense", true);
        this.dao = dao;
        this.existing = existing;

        setSize(420, 380);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(buildContent());

        if (existing != null) fillFields();
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(existing == null ? new Color(16, 160, 100) : new Color(41, 98, 255));
        header.setPreferredSize(new Dimension(0, 52));
        header.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        String title = existing == null ? "➕  Add New Expense" : "✏️  Edit Expense";
        JLabel hLabel = new JLabel(title);
        hLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hLabel.setForeground(Color.WHITE);
        header.add(hLabel, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 0, 6, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        amountField = new JTextField();
        categoryCombo = new JComboBox<>(Expense.CATEGORIES);
        dateField = new JTextField();
        descField = new JTextField();

        dateField.setText(java.time.LocalDate.now().toString());

        styleField(amountField);
        styleField(dateField);
        styleField(descField);

        addRow(form, g, 0, "Amount (€) *", amountField);
        addRow(form, g, 1, "Category *", categoryCombo);
        addRow(form, g, 2, "Date * (YYYY-MM-DD)", dateField);
        addRow(form, g, 3, "Description", descField);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnRow.setBackground(new Color(245, 247, 250));
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 222, 230)));

        JButton cancel = HomeScreen.makeButton("Cancel", new Color(150, 155, 170));
        JButton save = HomeScreen.makeButton(existing == null ? "✅  Save" : "💾  Update",
                existing == null ? new Color(16, 160, 100) : new Color(41, 98, 255));
        cancel.setPreferredSize(new Dimension(100, 36));
        save.setPreferredSize(new Dimension(120, 36));

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> handleSave());

        btnRow.add(cancel);
        btnRow.add(save);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(btnRow, BorderLayout.SOUTH);
        return root;
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy = row * 2;
        g.insets = new Insets(row == 0 ? 0 : 8, 0, 2, 0);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(new Color(100, 110, 130));
        form.add(lbl, g);

        g.gridy = row * 2 + 1;
        g.insets = new Insets(0, 0, 0, 0);
        form.add(field, g);
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 220), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private void fillFields() {
        amountField.setText(String.valueOf(existing.getAmount()));
        categoryCombo.setSelectedItem(existing.getCategory());
        dateField.setText(existing.getDate());
        descField.setText(existing.getDescription() == null ? "" : existing.getDescription());
    }

    private void handleSave() {
        String amtText = amountField.getText().trim();
        if (amtText.isEmpty()) {
            warn("Amount is required.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
            if (amount <= 0) {
                warn("Amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            warn("Amount must be a number (e.g. 12.50).");
            return;
        }

        String date = dateField.getText().trim();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            warn("Date must be in YYYY-MM-DD format (e.g. 2025-01-15).");
            return;
        }

        String category = (String) categoryCombo.getSelectedItem();
        String desc = descField.getText().trim();
        Expense e = new Expense(amount, category, date, desc);

        try {
            if (existing == null) {
                dao.add(e);
                JOptionPane.showMessageDialog(this, "Expense added!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                e.setId(existing.getId());
                dao.update(e);
                JOptionPane.showMessageDialog(this, "Expense updated!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            saved = true;
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean wasSaved() {
        return saved;
    }
}