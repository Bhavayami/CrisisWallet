package crisiswallet.calculator;

import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public class ProjectionCalculator extends Calculator {
    private SavingsCalculator savings;
    private int months;
    // 2D array: each row = {month, savings during crisis, savings without crisis}
    private double[][] table;

    public ProjectionCalculator(User user, Crisis crisis, double salaryCutPercent,
                                SavingsCalculator savings, int months) {
        super(user, crisis, salaryCutPercent);
        this.savings = savings;
        this.months = months;
    }

    @Override
    public void calculate() {
        table = new double[months + 1][3];
        for (int m = 0; m <= months; m++) {
            table[m][0] = m;
            table[m][1] = user.getSavings() + m * savings.getCrisisSavings();
            table[m][2] = user.getSavings() + m * savings.getNormalSavings();
        }
    }

    public double[][] getTable() { return table; }
    public int getMonths()       { return months; }

    @Override
    public String getSummary() {
        return "After " + months + " months your savings would be "
                + MoneyFormat.rs(table[months][1]);
    }

    @Override
    public String toReportText() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s %18s %18s%n", "Month", "With crisis", "Without crisis"));
        for (int m = 0; m <= months; m++) {
            if (m % 3 == 0) {
                sb.append(String.format("%-8d %18s %18s%n", m,
                        String.format("%,.2f", table[m][1]),
                        String.format("%,.2f", table[m][2])));
            }
        }
        return sb.toString();
    }
}
