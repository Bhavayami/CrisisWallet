package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import crisiswallet.calculator.ProjectionCalculator;
import crisiswallet.calculator.SavingsCalculator;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.task.AnimationListener;
import crisiswallet.task.ProjectionAnimator;
import crisiswallet.util.MoneyFormat;

// Implements TWO interfaces: ActionListener (buttons) and AnimationListener (thread callbacks)
public class ProjectionPanel extends JPanel implements ActionListener, AnimationListener {
    private static final int[] MONTH_OPTIONS = {3, 6, 12, 24};      // 1D array

    private JComboBox<String> monthsBox =
            new JComboBox<String>(new String[] {"3 months", "6 months", "12 months", "24 months"});
    private JButton animateButton = new JButton("Animate projection");
    private StatCard monthCard = new StatCard("Month");
    private StatCard withCard = new StatCard("Savings with crisis");
    private StatCard withoutCard = new StatCard("Savings without crisis");
    private StatCard gapCard = new StatCard("Difference");
    private LineChartPanel chart = new LineChartPanel();

    private User user;
    private Crisis crisis;
    private double salaryCut;
    private SavingsCalculator savings;
    private double[][] table;
    private ProjectionAnimator animator;

    public ProjectionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        monthsBox.setSelectedIndex(2);
        // Row 1: GridLayout(1, 3) for the controls
        JPanel controls = new JPanel(new GridLayout(1, 3, 10, 0));
        controls.add(new JLabel("Project for:"));
        controls.add(monthsBox);
        controls.add(animateButton);

        // Row 2: GridLayout(1, 4) for the result cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.add(monthCard);
        cards.add(withCard);
        cards.add(withoutCard);
        cards.add(gapCard);

        // GridLayout(2, 1) stacks the two rows
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 8));
        top.add(controls);
        top.add(cards);
        add(top, BorderLayout.NORTH);
        add(chart, BorderLayout.CENTER);

        monthsBox.addActionListener(this);
        animateButton.addActionListener(this);
    }

    public void update(User user, Crisis crisis, double salaryCut, SavingsCalculator savings) {
        this.user = user;
        this.crisis = crisis;
        this.salaryCut = salaryCut;
        this.savings = savings;
        showFullChart();
    }

    private int selectedMonths() {
        return MONTH_OPTIONS[monthsBox.getSelectedIndex()];
    }

    private void buildTable() {
        ProjectionCalculator pc = new ProjectionCalculator(user, crisis, salaryCut, savings, selectedMonths());
        pc.calculate();
        table = pc.getTable();
    }

    private void stopAnimation() {
        if (animator != null) {
            animator.stop();
            animator = null;
        }
        animateButton.setEnabled(true);
    }

    private void showFullChart() {
        if (user == null) {
            return;
        }
        stopAnimation();
        buildTable();
        int last = table.length - 1;
        chart.setData(table, last);
        showCards(last);
    }

    private void showCards(int month) {
        monthCard.setValue(String.valueOf(month));
        withCard.setValue(MoneyFormat.rs(table[month][1]));
        withoutCard.setValue(MoneyFormat.rs(table[month][2]));
        gapCard.setValue(MoneyFormat.rs(table[month][2] - table[month][1]));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (user == null) {
            return;
        }
        if (e.getSource() == monthsBox) {
            showFullChart();
        } else if (e.getSource() == animateButton) {
            stopAnimation();
            buildTable();
            chart.setData(table, 0);
            animateButton.setEnabled(false);
            animator = new ProjectionAnimator(table.length - 1, this);
            new Thread(animator, "projection-animator").start();
        }
    }

    // ---- AnimationListener (called on the Swing thread) ----
    @Override
    public void onFrame(int month) {
        chart.setVisibleMonths(month);
        showCards(month);
    }

    @Override
    public void onFinished() {
        animateButton.setEnabled(true);
    }
}
