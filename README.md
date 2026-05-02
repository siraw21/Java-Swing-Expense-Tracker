# Java-Swing-Expense-Tracker

# 💰 Expense Tracker

### Simple Java Swing + JDBC + MySQL Desktop App

---

## 📁 Project Structure

```
ExpenseTracker/
│
├── Main.java               ← Entry point: boots DB, opens HomeScreen
│
├── db/
│   └── DBConnection.java   ← Opens the MySQL connection, creates DB/table
│
├── model/
│   └── Expense.java        ← Data class: id, amount, category, date, description
│
├── dao/
│   └── ExpenseDAO.java     ← All SQL here: add, update, delete, getAll, search, total
│
├── ui/
│   ├── HomeScreen.java         ← Welcome screen with navigation buttons
│   ├── DashboardScreen.java    ← Main table view + Add/Edit/Delete/Search
│   └── ExpenseFormDialog.java  ← Modal form for Add and Edit
│
├── lib/
│   └── mysql-connector-j.jar   ← ⚠ Download and put here (see setup below)
│
├── setup.sql       ← Optional manual DB setup with sample data
├── compile.bat     ← Windows: compile + run in one click
└── run.sh          ← Linux/Mac: compile + run
```

---

## 🔄 How the Layers Work Together

```
HomeScreen
    │  click "Open Dashboard"
    ▼
DashboardScreen  ──calls──▶  ExpenseDAO  ──SQL──▶  MySQL DB
    │                              ▲
    │  click "Add / Edit"          │ returns List<Expense>
    ▼                              │
ExpenseFormDialog  ────calls───────┘
```

**Rule:** UI never writes SQL. SQL never builds UI.

---

## ⚙️ Setup Steps

### 1. Install Requirements

- Java JDK 17+
- MySQL 8.0+

### 2. Download the MySQL JDBC Driver

Go to: https://dev.mysql.com/downloads/connector/j/  
Download the **Platform Independent** ZIP.  
Extract it and copy **`mysql-connector-j-x.x.x.jar`** into the **`lib/`** folder.  
Rename it to `mysql-connector-j.jar`.

### 3. Set your MySQL credentials

Open **`db/DBConnection.java`** and change:

```java
private static final String USER = "root";       // your MySQL user
private static final String PASS = "password";  // your MySQL password
```

### 4. Compile and Run

**Windows:**

```
Double-click compile.bat
```

**Linux / Mac:**

```bash
bash run.sh
```

**Or manually:**

```bash
# Compile
javac -cp ".:lib/mysql-connector-j.jar" db/DBConnection.java model/Expense.java dao/ExpenseDAO.java ui/HomeScreen.java ui/DashboardScreen.java ui/ExpenseFormDialog.java Main.java

# Run
java -cp ".:lib/mysql-connector-j.jar" Main
```

> On Windows use semicolons: `-cp ".;lib\mysql-connector-j.jar"`

---

## 🖥️ Screens

| Screen          | What it does                                                    |
| --------------- | --------------------------------------------------------------- |
| **Home**        | Welcome screen, button to open Dashboard or Exit                |
| **Dashboard**   | Table of all expenses + total + search. Add/Edit/Delete buttons |
| **Form Dialog** | Modal form for adding or editing one expense with validation    |

---

## ✅ Features

- Add, Edit, Delete expenses
- Search by category or description
- Total expense sum always visible
- Input validation (empty fields, bad numbers, wrong date format)
- Auto-creates the database and table on first run
- Clean layered structure: `db` → `model` → `dao` → `ui`
