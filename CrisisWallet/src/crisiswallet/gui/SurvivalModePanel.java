package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import crisiswallet.calculator.EmergencyFundCalculator;
import crisiswallet.calculator.ImpactCalculator;
import crisiswallet.calculator.SavingsCalculator;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

// A focused "what if income stops?" survival screen using the existing emergency-fund calculation.
public class SurvivalModePanel extends JPanel {
    private StatCard monthsCard = new StatCard("Months you can survive");
    private StatCard expenseCard = new StatCard("Crisis expense / month");
    private StatCard savingsCard = new StatCard("Savings available");
    private StatCard riskCard = new StatCard("Survival status");
    private JProgressBar bar = new JProgressBar(0, 100);
    private JTextArea text = new JTextArea();

    public SurvivalModePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.add(monthsCard);
        cards.add(expenseCard);
        cards.add(savingsCard);
        cards.add(riskCard);
        add(cards, BorderLayout.NORTH);

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(new JScrollPane(text), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setBorder(BorderFactory.createTitledBorder("Emergency survival meter"));
        bar.setStringPainted(true);
        south.add(bar, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public void update(User user, Crisis crisis, ImpactCalculator impact,
                       SavingsCalculator savings, EmergencyFundCalculator fund) {
        double months = fund.getMonths();
        String status;
        if (months >= 6) {
            status = "Strong";
        } else if (months >= 3) {
            status = "Caution";
        } else {
            status = "High risk";
        }

        monthsCard.setValue(String.format("%.2f months", months));
        expenseCard.setValue(MoneyFormat.rs(impact.getCrisisTotal()));
        savingsCard.setValue(MoneyFormat.rs(user.getSavings()));
        riskCard.setValue(status);

        bar.setValue((int)Math.min(100, Math.max(0, months / 12.0 * 100)));
        bar.setString(String.format("%.2f months covered (full bar = 12 months)", months));

        StringBuilder sb = new StringBuilder();
        sb.append("FINANCIAL SURVIVAL MODE\n");
        sb.append("=======================\n\n");
        sb.append("Question: If your income stopped completely today, how long could your current savings cover your crisis-level expenses?\n\n");
        sb.append("Current savings       : ").append(MoneyFormat.rs(user.getSavings())).append("\n");
        sb.append("Crisis expenses       : ").append(MoneyFormat.rs(impact.getCrisisTotal())).append(" per month\n");
        sb.append("Estimated survival    : ").append(String.format("%.2f months", months)).append("\n");
        sb.append("Survival status       : ").append(status).append("\n\n");

        if (months >= 6) {
            sb.append("You have a relatively strong emergency cushion. Keep protecting it and avoid unnecessary withdrawals.\n");
        } else if (months >= 3) {
            sb.append("You have a useful cushion, but a prolonged crisis could put pressure on your savings. Look for ways to reduce discretionary spending.\n");
        } else {
            sb.append("Your emergency cushion is short. Prioritize essential expenses, reduce discretionary spending and consider additional income.\n");
        }

        sb.append("\nMonthly savings during the crisis: ")
          .append(MoneyFormat.rs(savings.getCrisisSavings())).append("\n");
        sb.append("Extra income needed to keep old savings: ")
          .append(MoneyFormat.rs(savings.getExtraIncomeNeeded())).append("\n\n");
        sb.append("Remember: this is a planning estimate. It assumes the crisis-level monthly expense stays constant and income is zero during the survival calculation.");

        text.setText(sb.toString());
        text.setCaretPosition(0);
    }
}
