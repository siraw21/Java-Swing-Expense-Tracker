// Main.java
// Entry point. Initialises the DB then opens the HomeScreen.

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            DBConnection.init();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to MySQL.\n\n"
                            + "Check that:\n"
                            + "  • MySQL is running\n"
                            + "  • Your username/password in DBConnection.java are correct\n\n"
                            + "Error: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Expense Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(860, 600);
            frame.setMinimumSize(new java.awt.Dimension(700, 500));
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new HomeScreen(frame));
            frame.setVisible(true);
        });
    }
}