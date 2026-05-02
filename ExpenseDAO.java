// dao/ExpenseDAO.java
// All database operations (SQL) live here.
// The UI calls these methods — it never writes SQL itself.

package dao;

import db.DBConnection;
import model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // ── INSERT a new expense ───────────────────────────────────────────────
    public void add(Expense e) throws SQLException {
        String sql = "INSERT INTO expenses (amount, category, date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, e.getAmount());
            ps.setString(2, e.getCategory());
            ps.setString(3, e.getDate());
            ps.setString(4, e.getDescription());
            ps.executeUpdate();
        }
    }

    // ── UPDATE an existing expense ─────────────────────────────────────────
    public void update(Expense e) throws SQLException {
        String sql = "UPDATE expenses SET amount=?, category=?, date=?, description=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, e.getAmount());
            ps.setString(2, e.getCategory());
            ps.setString(3, e.getDate());
            ps.setString(4, e.getDescription());
            ps.setInt   (5, e.getId());
            ps.executeUpdate();
        }
    }

    // ── DELETE by id ───────────────────────────────────────────────────────
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ── GET ALL expenses (newest first) ───────────────────────────────────
    public List<Expense> getAll() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY date DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(toExpense(rs));
        }
        return list;
    }

    // ── SEARCH by keyword in category or description ───────────────────────
    public List<Expense> search(String keyword) throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE category LIKE ? OR description LIKE ? ORDER BY date DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            String p = "%" + keyword + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(toExpense(rs));
            }
        }
        return list;
    }

    // ── SUM of all amounts ─────────────────────────────────────────────────
    public double getTotal() throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM expenses";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    // Helper: turns a ResultSet row into an Expense object
    private Expense toExpense(ResultSet rs) throws SQLException {
        return new Expense(
            rs.getInt   ("id"),
            rs.getDouble("amount"),
            rs.getString("category"),
            rs.getString("date"),
            rs.getString("description")
        );
    }
}
