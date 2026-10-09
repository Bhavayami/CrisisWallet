package crisiswallet.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import crisiswallet.calculator.*;           // wildcard import
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public final class ReportGenerator {

    private ReportGenerator() { }           // private constructor: no objects needed

    // default (package-private) access
    static String line() {
        return "============================================================\n";
    }

    static String heading(String title) {
        return "\n" + line() + "  " + title + "\n" + line();
    }

    public static String build(User user, Crisis crisis, double salaryCut,
                               ImpactCalculator impact, SavingsCalculator savings,
                               EmergencyFundCalculator fund, ProjectionCalculator projection) {
        StringBuilder sb = new StringBuilder();
        sb.append(line());
        sb.append("  CRISIS WALLET - FINAL REPORT\n");
        sb.append(line());
        sb.append("Profile : ").append(user.getSummary()).append("\n");
        sb.append("Crisis  : ").append(crisis.describe()).append("\n");
        sb.append(String.format("Salary cut during crisis: %.1f%%%n", salaryCut));
        sb.append("(Percentages are scenario assumptions, not exact historical figures.)\n");

        sb.append(heading("1. CRISIS IMPACT"));
        sb.append(impact.toReportText());
        sb.append(heading("2. SAVINGS IMPACT"));
        sb.append(savings.toReportText());
        sb.append(heading("3. EMERGENCY FUND"));
        sb.append(fund.toReportText());
        sb.append(heading("4. BUDGET RESCUE PLAN"));
        double amountToRecover = Math.max(0, savings.getSavingsReduction());
        double planned = 0;
        String[] priority = {"Entertainment", "Shopping", "Transport", "Food & Groceries"};
        double[] cutRates = {0.50, 0.35, 0.15, 0.10};
        for (int i = 0; i < priority.length && planned < amountToRecover; i++) {
            double current = impact.getCrisisExpense(priority[i]);
            double cut = Math.min(current * cutRates[i], amountToRecover - planned);
            if (cut > 0.005) {
                planned += cut;
                sb.append(String.format("Reduce %-18s by %s per month%n", priority[i], MoneyFormat.rs(cut)));
            }
        }
        sb.append(String.format("Amount to recover      : %s%n", MoneyFormat.rs(amountToRecover)));
        sb.append(String.format("Planned monthly cut    : %s%n", MoneyFormat.rs(planned)));
        sb.append(planned + 0.01 >= amountToRecover
                ? "Plan status             : Recoverable\n"
                : "Plan status             : Partial - extra income or further cuts are needed\n");
        sb.append("Protected essentials    : Rent / Housing, Healthcare, Utilities\n");

        sb.append(heading("5. SURVIVAL MODE"));
        double months = fund.getMonths();
        String status = months >= 6 ? "Strong" : (months >= 3 ? "Caution" : "High risk");
        sb.append(String.format("Savings available       : %s%n", MoneyFormat.rs(user.getSavings())));
        sb.append(String.format("Crisis expenses/month   : %s%n", MoneyFormat.rs(impact.getCrisisTotal())));
        sb.append(String.format("Estimated survival      : %.2f months%n", months));
        sb.append("Survival status         : ").append(status).append("\n");
        sb.append("Assumption              : income is zero and crisis-level monthly expenses stay constant.\n");
        return sb.toString();
    }

    // checked IOException is declared; finally always closes the file
    public static void saveToFile(String text, File file) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(text);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
