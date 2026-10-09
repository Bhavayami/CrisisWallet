package crisiswallet.calculator;

import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public class EmergencyFundCalculator extends Calculator {
    private ImpactCalculator impact;
    private double months;

    public EmergencyFundCalculator(User user, Crisis crisis, double salaryCutPercent,
                                   ImpactCalculator impact) {
        super(user, crisis, salaryCutPercent);
        this.impact = impact;
    }

    @Override
    public void calculate() {
        if (impact.getCrisisTotal() == 0) {
            months = 0;
        } else {
            months = user.getSavings() / impact.getCrisisTotal();
        }
    }

    public double getMonths() { return months; }

    @Override
    public String getSummary() {
        return String.format("Savings last about %.2f months with no income", months);
    }

    @Override
    public String toReportText() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Current savings         : %s%n", MoneyFormat.rs(user.getSavings())));
        sb.append(String.format("Crisis expenses         : %s per month%n", MoneyFormat.rs(impact.getCrisisTotal())));
        sb.append(String.format("Emergency fund coverage : %.2f months (if income stops)%n", months));
        return sb.toString();
    }
}
