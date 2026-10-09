package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import crisiswallet.calculator.ImpactCalculator;
import crisiswallet.calculator.SavingsCalculator;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

// Turns the crisis result into a simple rule-based plan for recovering lost monthly savings.
public class BudgetRescuePanel extends JPanel {
    private StatCard gapCard = new StatCard("Amount to recover");
    private StatCard planCard = new StatCard("Planned monthly cut");
    private StatCard statusCard = new StatCard("Rescue status");
    private JTextArea text = new JTextArea();

    public BudgetRescuePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel cards = new JPanel(new GridLayout(1, 3, 10, 0));
        cards.add(gapCard);
        cards.add(planCard);
        cards.add(statusCard);
        add(cards, BorderLayout.NORTH);

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(new JScrollPane(text), BorderLayout.CENTER);
    }

    public void update(User user, ImpactCalculator impact, SavingsCalculator savings) {
        double amountToRecover = Math.max(0, savings.getSavingsReduction());
        double planned = 0;
        StringBuilder sb = new StringBuilder();

        sb.append("BUDGET RESCUE PLAN\n");
        sb.append("==================\n\n");
        sb.append("Your crisis has changed your monthly savings by ")
          .append(MoneyFormat.rs(savings.getSavingsReduction())).append(".\n");
        sb.append("The plan below uses simple rules: discretionary spending is considered first, while essential expenses such as rent and healthcare are protected.\n\n");

        // Priority order: Entertainment, Shopping, Transport, then Food & Groceries.
        String[] priority = {"Entertainment", "Shopping", "Transport", "Food & Groceries"};
        double[] cutRates = {0.50, 0.35, 0.15, 0.10};
        boolean any = false;

        for (int i = 0; i < priority.length && planned < amountToRecover; i++) {
            String category = priority[i];
            double current = impact.getCrisisExpense(category);
            double possible = current * cutRates[i];
            double cut = Math.min(possible, amountToRecover - planned);
            if (cut > 0.005) {
                planned += cut;
                any = true;
                sb.append("• Reduce ").append(category).append(" by ")
                  .append(MoneyFormat.rs(cut)).append(" per month.\n");
            }
        }

        sb.append("\n");
        if (!any) {
            sb.append("No discretionary reduction is available from the current budget. Consider increasing income or reviewing the crisis assumptions.\n");
        } else if (planned + 0.01 >= amountToRecover) {
            sb.append("Good news: this plan can recover the full amount needed to restore your previous monthly savings.\n");
        } else {
            sb.append("This is a partial rescue plan. You would still need ")
              .append(MoneyFormat.rs(amountToRecover - planned))
              .append(" more each month through extra income or further reductions.\n");
        }

        sb.append("\nProtected essentials\n--------------------\n");
        sb.append("Rent / Housing, Healthcare and Utilities are not targeted first because they are essential household costs.\n\n");
        sb.append("Crisis impact\n-------------\n");
        sb.append("Extra monthly cost : ").append(MoneyFormat.rs(impact.getExtraMonthly())).append("\n");
        sb.append("Normal savings     : ").append(MoneyFormat.rs(savings.getNormalSavings())).append("\n");
        sb.append("Crisis savings     : ").append(MoneyFormat.rs(savings.getCrisisSavings())).append("\n");
        sb.append("Salary needed to keep old savings : ")
          .append(MoneyFormat.rs(savings.getRequiredSalary())).append("\n\n");
        sb.append("Tip: Your wallet does not need panic first. It needs priorities. 😄");

        gapCard.setValue(MoneyFormat.rs(amountToRecover));
        planCard.setValue(MoneyFormat.rs(planned));
        statusCard.setValue(planned + 0.01 >= amountToRecover ? "Recoverable" : "Partial");
        text.setText(sb.toString());
        text.setCaretPosition(0);
    }
}
