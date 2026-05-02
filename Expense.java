// model/Expense.java
// Simple data class — mirrors one row in the expenses table.
// No logic here, just fields + getters/setters.

package model;

public class Expense {

    // All categories available in the app
    public static final String[] CATEGORIES = {
        "Food", "Transport", "Bills", "Entertainment",
        "Healthcare", "Shopping", "Education", "Other"
    };

    private int    id;
    private double amount;
    private String category;
    private String date;        // stored as "YYYY-MM-DD" string for simplicity
    private String description;

    // Constructor for a new expense (id not known yet)
    public Expense(double amount, String category, String date, String description) {
        this.amount      = amount;
        this.category    = category;
        this.date        = date;
        this.description = description;
    }

    // Constructor when loading from the database (id is known)
    public Expense(int id, double amount, String category, String date, String description) {
        this(amount, category, date, description);
        this.id = id;
    }

    // Getters
    public int    getId()          { return id; }
    public double getAmount()      { return amount; }
    public String getCategory()    { return category; }
    public String getDate()        { return date; }
    public String getDescription() { return description; }

    // Setters
    public void setId(int id)               { this.id = id; }
    public void setAmount(double amount)    { this.amount = amount; }
    public void setCategory(String c)       { this.category = c; }
    public void setDate(String date)        { this.date = date; }
    public void setDescription(String desc) { this.description = desc; }
}
