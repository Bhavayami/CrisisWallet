package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import crisiswallet.calculator.CrisisResult;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.task.CompareWorker;
import crisiswallet.util.MoneyFormat;

public class ComparePanel extends JPanel implements ActionListener {
    private MainFrame frame;
    private DefaultTableModel model;
    private JButton runButton = new JButton("Run comparison again");
    private JLabel status = new JLabel("Run a simulation first");
    private StatCard hardestCard = new StatCard("Hardest hit for you");
    private StatCard mildestCard = new StatCard("Mildest for you");
    private StatCard countCard = new StatCard("Crises compared");

    private User user;
    private double salaryCut;
    private List<Crisis> crises;
    private int generation = 0;         // ignores results of an older run

    public ComparePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Rank", "Crisis", "Extra / month (Rs.)", "Extra / year (Rs.)", "Personal inflation"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);

        status.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Row 1: GridLayout(1, 2) -> button and status share the width equally
        JPanel controls = new JPanel(new GridLayout(1, 2, 10, 0));
        controls.add(runButton);
        controls.add(status);

        // Row 2: GridLayout(1, 3) -> three equal summary cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 10, 0));
        cards.add(hardestCard);
        cards.add(mildestCard);
        cards.add(countCard);

        JPanel top = new JPanel(new GridLayout(2, 1, 0, 8));
        top.add(controls);
        top.add(cards);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        runButton.addActionListener(this);
    }

    public void update(User user, double salaryCut, List<Crisis> crises) {
        this.user = user;
        this.salaryCut = salaryCut;
        this.crises = crises;
        runComparison();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (user != null) {
            runComparison();
        }
    }

    // One thread per crisis, then wait for all of them (join)
    private void runComparison() {
        final int myRun = ++generation;
        final User u = user;
        final double cut = salaryCut;
        final List<Crisis> list = new ArrayList<Crisis>(crises);

        runButton.setEnabled(false);
        status.setText("Comparing " + list.size() + " crises using " + list.size() + " threads...");

        Thread coordinator = new Thread(new Runnable() {
            public void run() {
                List<CrisisResult> shared = Collections.synchronizedList(new ArrayList<CrisisResult>());
                List<CompareWorker> workers = new ArrayList<CompareWorker>();
                for (Crisis c : list) {
                    CompareWorker worker = new CompareWorker(u, c, cut, shared);
                    workers.add(worker);
                    worker.start();
                }
                for (CompareWorker worker : workers) {
                    try {
                        worker.join();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                final List<CrisisResult> sorted = new ArrayList<CrisisResult>(shared);
                Collections.sort(sorted);       // uses CrisisResult.compareTo

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (myRun == generation) {
                            showResults(sorted);
                        }
                    }
                });
            }
        }, "compare-coordinator");
        coordinator.start();
    }

    private void showResults(List<CrisisResult> sorted) {
        model.setRowCount(0);
        int rank = 1;
        for (CrisisResult r : sorted) {
            model.addRow(new Object[] {
                rank++,
                r.getName(),
                String.format("%,.2f", r.getExtraMonthly()),
                String.format("%,.2f", r.getExtraYearly()),
                MoneyFormat.percent(r.getInflation())
            });
        }
        status.setText("Comparison complete");
        hardestCard.setValue(sorted.get(0).getName());
        mildestCard.setValue(sorted.get(sorted.size() - 1).getName());
        countCard.setValue(String.valueOf(sorted.size()));
        runButton.setEnabled(true);
        frame.setComparison(sorted);
    }
}
