package crisiswallet.calculator;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public class ImpactCalculator extends Calculator {
    private Map<String, Double> crisisExpenses = new LinkedHashMap<String, Double>();
    private Map<String, Double> extraExpenses = new LinkedHashMap<String, Double>();
    private Set<String> increasedCategories = new LinkedHashSet<String>();   // Set
    private double normalTotal;
    private double crisisTotal;
    private String biggestCategory = "";

    public ImpactCalculator(User user, Crisis crisis, double salaryCutPercent) {
        super(user, crisis, salaryCutPercent);          // super constructor
    }

    @Override
    public void calculate() {
        crisisExpenses.clear();
        extraExpenses.clear();
        increasedCategories.clear();
        normalTotal = 0;
        crisisTotal = 0;
        biggestCategory = "";

        for (String category : User.CATEGORIES) {
            double normal = user.getExpense(category);
            double now = normal + normal * crisis.getPercent(category) / 100;
            double diff = now - normal;

            crisisExpenses.put(category, now);
            extraExpenses.put(category, diff);
            normalTotal += normal;
            crisisTotal += now;

            if (diff > 0) {
                increasedCategories.add(category);
            }
            if (biggestCategory.equals("") || diff > extraExpenses.get(biggestCategory)) {
                biggestCategory = category;
            }
        }
    }

    public double getNormalExpense(String category) { return user.getExpense(category); }
    public double getCrisisExpense(String category) { return crisisExpenses.get(category); }
    public double getExtra(String category)         { return extraExpenses.get(category); }

    public double getNormalTotal()  { return normalTotal; }
    public double getCrisisTotal()  { return crisisTotal; }
    public double getExtraMonthly() { return crisisTotal - normalTotal; }
    public double getExtraYearly()  { return getExtraMonthly() * 12; }

    public double getPersonalInflation() {
        if (normalTotal == 0) {
            return 0;
        }
        return (crisisTotal - normalTotal) / normalTotal * 100;
    }

    public String getBiggestCategory()      { return biggestCategory; }
    public double getBiggestExtra()         { return extraExpenses.get(biggestCategory); }
    public Set<String> getIncreasedCategories() { return increasedCategories; }

    @Override
    public String getSummary() {
        return "Personal inflation " + MoneyFormat.percent(getPersonalInflation())
                + ", extra " + MoneyFormat.rs(getExtraMonthly()) + " per month";
    }

    @Override
    public String toReportText() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-18s %14s %14s %14s%n", "Category", "Before", "Crisis", "Extra"));
        for (String category : User.CATEGORIES) {
            sb.append(String.format("%-18s %14s %14s %14s%n", category,
                    String.format("%,.2f", getNormalExpense(category)),
                    String.format("%,.2f", getCrisisExpense(category)),
                    MoneyFormat.signedRs(getExtra(category))));
        }
        sb.append(String.format("%-18s %14s %14s %14s%n", "TOTAL",
                String.format("%,.2f", normalTotal),
                String.format("%,.2f", crisisTotal),
                MoneyFormat.signedRs(getExtraMonthly())));
        sb.append(String.format("%nPersonal inflation rate : %s%n", MoneyFormat.percent(getPersonalInflation())));
        sb.append(String.format("Extra cost per month    : %s%n", MoneyFormat.rs(getExtraMonthly())));
        sb.append(String.format("Extra cost per year     : %s%n", MoneyFormat.rs(getExtraYearly())));
        sb.append(String.format("Biggest impact          : %s (%s)%n",
                biggestCategory, MoneyFormat.signedRs(getBiggestExtra())));
        sb.append(String.format("Categories that rose    : %s%n", increasedCategories));
        return sb.toString();
    }
}
