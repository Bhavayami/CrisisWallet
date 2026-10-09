package crisiswallet.calculator;

import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public class SavingsCalculator extends Calculator {
    private ImpactCalculator impact;
    private double crisisSalary;
    private double normalSavings;
    private double crisisSavings;

    public SavingsCalculator(User user, Crisis crisis, double salaryCutPercent,
                             ImpactCalculator impact) {
        super(user, crisis, salaryCutPercent);
        this.impact = impact;
    }

    @Override
    public void calculate() {
        crisisSalary = computeCrisisSalary();
        normalSavings = user.getSalary() - impact.getNormalTotal();
        crisisSavings = crisisSalary - impact.getCrisisTotal();
    }

    public double getCrisisSalary()     { return crisisSalary; }
    public double getNormalSavings()    { return normalSavings; }
    public double getCrisisSavings()    { return crisisSavings; }
    public double getSavingsReduction() { return normalSavings - crisisSavings; }

    public double getNormalRate() {
        return user.getSalary() == 0 ? 0 : normalSavings / user.getSalary() * 100;
    }

    public double getCrisisRate() {
        return crisisSalary == 0 ? 0 : crisisSavings / crisisSalary * 100;
    }

    // income needed to keep the old monthly savings
    public double getRequiredSalary() {
        return impact.getCrisisTotal() + normalSavings;
    }

    public double getExtraIncomeNeeded() {
        double needed = getRequiredSalary() - crisisSalary;
        return needed > 0 ? needed : 0;
    }

    @Override
    public String getSummary() {
        return "Monthly savings fall from " + MoneyFormat.rs(normalSavings)
                + " to " + MoneyFormat.rs(crisisSavings);
    }

    @Override
    public String toReportText() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Salary before           : %s%n", MoneyFormat.rs(user.getSalary())));
        sb.append(String.format("Salary during crisis    : %s (cut %.1f%%)%n",
                MoneyFormat.rs(crisisSalary), salaryCutPercent));
        sb.append(String.format("Monthly savings before  : %s (%s of salary)%n",
                MoneyFormat.rs(normalSavings), MoneyFormat.percent(getNormalRate())));
        sb.append(String.format("Monthly savings during  : %s (%s of salary)%n",
                MoneyFormat.rs(crisisSavings), MoneyFormat.percent(getCrisisRate())));
        sb.append(String.format("Savings reduction       : %s%n", MoneyFormat.rs(getSavingsReduction())));
        if (crisisSavings < 0) {
            sb.append("WARNING: Your expenses are higher than your income!\n");
        }
        sb.append(String.format("Salary needed to keep old savings : %s%n", MoneyFormat.rs(getRequiredSalary())));
        sb.append(String.format("Extra income needed per month     : %s%n", MoneyFormat.rs(getExtraIncomeNeeded())));
        return sb.toString();
    }
}
