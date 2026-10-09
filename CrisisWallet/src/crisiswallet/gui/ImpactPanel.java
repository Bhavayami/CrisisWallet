package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import crisiswallet.calculator.ImpactCalculator;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.MoneyFormat;

public class ImpactPanel extends JPanel {
    private DefaultTableModel model;
    private JLabel titleLabel = new JLabel("Run a simulation first");
    private JTextArea summary = new JTextArea();
    private BarChartPanel bars = new BarChartPanel();
    private StatCard inflationCard = new StatCard("Personal inflation");
    private StatCard monthlyCard = new StatCard("Extra per month");
    private StatCard yearlyCard = new StatCard("Extra per year");
    private StatCard biggestCard = new StatCard("Biggest impact");

    public ImpactPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // GridLayout(1, 4): four equal summary cards in one row
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.add(inflationCard);
        cards.add(monthlyCard);
        cards.add(yearlyCard);
        cards.add(biggestCard);

        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.add(titleLabel, BorderLayout.NORTH);
        north.add(cards, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        String[] columns = {"Category", "Before (Rs.)", "During crisis (Rs.)", "Extra (Rs.)", "Assumed change"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(24);

        summary.setEditable(false);
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summary.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel bottom = new JPanel(new GridLayout(1, 2, 10, 0));
        bottom.add(new JScrollPane(summary));
        bottom.add(bars);

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 10));
        center.add(new JScrollPane(table));
        center.add(bottom);
        add(center, BorderLayout.CENTER);
    }

    public void update(Crisis crisis, ImpactCalculator ic) {
        titleLabel.setText("Impact of: " + crisis.describe());
        model.setRowCount(0);

        String[] labels = new String[User.CATEGORIES.length];
        double[] extras = new double[User.CATEGORIES.length];
        int i = 0;
        for (String category : User.CATEGORIES) {
            model.addRow(new Object[] {
                category,
                String.format("%,.2f", ic.getNormalExpense(category)),
                String.format("%,.2f", ic.getCrisisExpense(category)),
                MoneyFormat.signedRs(ic.getExtra(category)),
                String.format("%+.1f%%", crisis.getPercent(category))
            });
            labels[i] = category.split("[ /&]")[0];     // short name for the chart
            extras[i] = ic.getExtra(category);
            i++;
        }
        model.addRow(new Object[] {
            "TOTAL",
            String.format("%,.2f", ic.getNormalTotal()),
            String.format("%,.2f", ic.getCrisisTotal()),
            MoneyFormat.signedRs(ic.getExtraMonthly()),
            MoneyFormat.percent(ic.getPersonalInflation())
        });

        inflationCard.setValue(MoneyFormat.percent(ic.getPersonalInflation()));
        monthlyCard.setValue(MoneyFormat.rs(ic.getExtraMonthly()));
        yearlyCard.setValue(MoneyFormat.rs(ic.getExtraYearly()));
        biggestCard.setValue(ic.getBiggestCategory());

        StringBuilder sb = new StringBuilder();
        sb.append("Personal inflation : ").append(MoneyFormat.percent(ic.getPersonalInflation())).append("\n");
        sb.append("Extra per month    : ").append(MoneyFormat.rs(ic.getExtraMonthly())).append("\n");
        sb.append("Extra per year     : ").append(MoneyFormat.rs(ic.getExtraYearly())).append("\n\n");
        sb.append("Biggest impact     : ").append(ic.getBiggestCategory())
          .append(" (").append(MoneyFormat.signedRs(ic.getBiggestExtra())).append(")\n");
        sb.append("Categories that rose:\n  ").append(ic.getIncreasedCategories()).append("\n");
        summary.setText(sb.toString());
        summary.setCaretPosition(0);

        bars.setData(labels, extras);
    }
}
