package crisiswallet.model;

import java.util.LinkedHashMap;
import java.util.Map;
import crisiswallet.util.Displayable;
import crisiswallet.util.MoneyFormat;

public class User implements Displayable {
    // static + final: shared by all users, never changes
    public static final String[] CATEGORIES = {
        "Food & Groceries", "Fuel / Petrol", "Transport", "Rent / Housing",
        "Utilities", "Healthcare", "Shopping", "Entertainment"
    };

    private static int userCount = 0;           // static counter

    private double salary;
    private double savings;
    private Map<String, Double> expenses;       // Collection: category -> amount

    public User() {                             // default constructor
        this(0, 0);                             // constructor invocation
    }

    public User(double salary, double savings) {
        if (salary < 0 || savings < 0) {
            throw new IllegalArgumentException("Salary and savings cannot be negative");
        }
        this.salary = salary;
        this.savings = savings;
        this.expenses = new LinkedHashMap<String, Double>();
        for (String category : CATEGORIES) {
            expenses.put(category, 0.0);
        }
        userCount++;
    }

    public double getSalary()  { return salary; }
    public double getSavings() { return savings; }

    public void setExpense(String category, double amount) throws InvalidInputException {
        if (!expenses.containsKey(category)) {
            throw new InvalidInputException("Unknown category: " + category);
        }
        if (amount < 0) {
            throw new InvalidInputException(category + " cannot be negative");
        }
        expenses.put(category, amount);
    }

    public double getExpense(String category) {
        return expenses.get(category);
    }

    public double getTotalExpenses() {
        double total = 0;
        for (double amount : expenses.values()) {
            total += amount;
        }
        return total;
    }

    public static int getUserCount() { return userCount; }

    @Override
    public String getSummary() {
        return "Salary " + MoneyFormat.rs(salary) + ", savings " + MoneyFormat.rs(savings)
                + ", expenses " + MoneyFormat.rs(getTotalExpenses());
    }

    @Override
    public String toString() {              // overriding Object class method
        return "User[" + getSummary() + "]";
    }
}
