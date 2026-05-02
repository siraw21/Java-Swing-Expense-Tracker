// HomeScreen.java
// The first screen the user sees.
// Has a welcome message and two buttons: Go to Dashboard, Exit.

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    private final JFrame frame;

    public HomeScreen(JFrame frame) {
        this.frame = frame;
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout());

        add(buildTop(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    // Top banner
    private JPanel buildTop() {
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(new Color(41, 98, 255));
        top.setPreferredSize(new Dimension(0, 180));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("💰", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("Expense Tracker");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Manage your expenses easily");
        sub.setFont(new Font("Arial", Font.PLAIN, 14));
        sub.setForeground(new Color(200, 220, 255));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(icon);
        inner.add(Box.createVerticalStrut(6));
        inner.add(title);
        inner.add(Box.createVerticalStrut(4));
        inner.add(sub);
        top.add(inner);
        return top;
    }

    // Center card with buttons
    private JPanel buildCenter() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel welcome = new JLabel("Welcome!");
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(new Color(30, 30, 60));
        welcome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel msg = new JLabel("<html><center>Track your daily expenses,<br>view summaries, and stay on budget.</center></html>");
        msg.setFont(new Font("Arial", Font.PLAIN, 13));
        msg.setForeground(new Color(100, 100, 120));
        msg.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 1));
        sep.setForeground(new Color(220, 220, 230));

        JButton dashBtn = makeButton("📊  Open Dashboard", new Color(41, 98, 255));
        dashBtn.addActionListener(e -> navigate(new DashboardScreen(frame)));

        JButton exitBtn = makeButton("✖  Exit", new Color(220, 60, 60));
        exitBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(frame, "Exit the application?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) System.exit(0);
        });

        card.add(welcome);
        card.add(Box.createVerticalStrut(10));
        card.add(msg);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(dashBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(exitBtn);

        outer.add(card);
        return outer;
    }

    // Bottom status bar
    private JPanel buildBottom() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bar.setBackground(new Color(235, 238, 245));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 215, 225)));
        JLabel lbl = new JLabel("Expense Tracker v1.0  •  MySQL + JDBC + Swing");
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(new Color(130, 140, 160));
        bar.add(lbl);
        return bar;
    }

    // Helper: create a styled button
    static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(220, 40));
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

   
    // Switch content pane
    void navigate(JPanel screen) {
        frame.setContentPane(screen);
        frame.revalidate();
        frame.repaint();
    }
}