// Expense.java
// Simple data class — mirrors one row in the expenses table.
// No logic here, just fields + getters/setters.

public class Expense {

    public static final String[] CATEGORIES = {
            "Food", "Transport", "Bills", "Entertainment",
            "Healthcare", "Shopping", "Education", "Other"
    };

    private int id;
    private double amount;
    private String category;
    private String date;
    private String description;

    public Expense(double amount, String category, String date, String description) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public Expense(int id, double amount, String category, String date, String description) {
        this(amount, category, date, description);
        this.id = id;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String c) { this.category = c; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String desc) { this.description = desc; }
}