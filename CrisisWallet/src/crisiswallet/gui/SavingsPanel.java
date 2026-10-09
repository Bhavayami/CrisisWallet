package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import crisiswallet.calculator.EmergencyFundCalculator;
import crisiswallet.calculator.SavingsCalculator;
import crisiswallet.util.MoneyFormat;

public class SavingsPanel extends JPanel {
    private JTextArea text = new JTextArea();
    private JProgressBar bar = new JProgressBar(0, 100);
    private StatCard beforeCard = new StatCard("Monthly savings before");
    private StatCard duringCard = new StatCard("Monthly savings during");
    private StatCard reductionCard = new StatCard("Savings reduction");
    private StatCard extraIncomeCard = new StatCard("Extra income needed");
    private StatCard monthsCard = new StatCard("Emergency fund lasts");

    public SavingsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // GridLayout(1, 5): five equal cards across the top
        JPanel cards = new JPanel(new GridLayout(1, 5, 10, 0));
        cards.add(beforeCard);
        cards.add(duringCard);
        cards.add(reductionCard);
        cards.add(extraIncomeCard);
        cards.add(monthsCard);
        add(cards, BorderLayout.NORTH);

        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(text), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setBorder(BorderFactory.createTitledBorder("Emergency fund coverage"));
        bar.setStringPainted(true);
        south.add(new JLabel("If your income stopped, how long would savings last?"), BorderLayout.NORTH);
        south.add(bar, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public void update(SavingsCalculator savings, EmergencyFundCalculator fund) {
        StringBuilder sb = new StringBuilder();
        sb.append("MONTHLY SAVINGS\n---------------\n");
        sb.append(savings.toReportText());
        sb.append("\nEMERGENCY FUND\n--------------\n");
        sb.append(fund.toReportText());
        text.setText(sb.toString());
        text.setCaretPosition(0);

        beforeCard.setValue(MoneyFormat.rs(savings.getNormalSavings()));
        duringCard.setValue(MoneyFormat.rs(savings.getCrisisSavings()));
        reductionCard.setValue(MoneyFormat.rs(savings.getSavingsReduction()));
        extraIncomeCard.setValue(MoneyFormat.rs(savings.getExtraIncomeNeeded()));
        monthsCard.setValue(String.format("%.1f months", fund.getMonths()));

        double months = fund.getMonths();
        bar.setValue((int) Math.min(100, months / 12 * 100));
        bar.setString(String.format("%.2f months covered (full bar = 12 months)", months));
    }
}
