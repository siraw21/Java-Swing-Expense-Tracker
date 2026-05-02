-- setup.sql
-- Run this manually in MySQL if you prefer to set up the DB yourself:
--   mysql -u root -p < setup.sql

CREATE DATABASE IF NOT EXISTS expense_db;
USE expense_db;

CREATE TABLE IF NOT EXISTS expenses (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    amount      DOUBLE       NOT NULL,
    category    VARCHAR(100) NOT NULL,
    date        DATE         NOT NULL,
    description VARCHAR(255)
);

-- Optional sample data
INSERT INTO expenses (amount, category, date, description) VALUES
    (45.00,  'Food',          '2025-01-05', 'Weekly groceries'),
    (12.50,  'Transport',     '2025-01-06', 'Bus pass'),
    (120.00, 'Bills',         '2025-01-07', 'Electricity bill'),
    (30.00,  'Entertainment', '2025-01-08', 'Netflix subscription'),
    (15.75,  'Food',          '2025-01-09', 'Café lunch'),
    (200.00, 'Shopping',      '2025-01-10', 'Winter jacket'),
    (50.00,  'Healthcare',    '2025-01-11', 'Pharmacy'),
    (25.00,  'Education',     '2025-01-12', 'Online course');
