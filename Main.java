// Main.java
// Entry point. Initialises the DB then opens the HomeScreen.

import db.DBConnection;
import ui.HomeScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Set up the database (creates DB + table if they don't exist)
        try {
            DBConnection.init();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Cannot connect to MySQL.\n\n"
                + "Check that:\n"
                + "  • MySQL is running\n"
                + "  • Your username/password in db/DBConnection.java are correct\n\n"
                + "Error: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            // App opens anyway; DB errors will be shown when features are used
        }

        // 2. Launch the UI on the Swing event thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Expense Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(860, 600);
            frame.setMinimumSize(new java.awt.Dimension(700, 500));
            frame.setLocationRelativeTo(null);

            // Start with the Home screen
            frame.setContentPane(new HomeScreen(frame));
            frame.setVisible(true);
        });
    }
}
