package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Home screen: GridLayout(2, 3) with six clickable boxes, one for each section.
public class DashboardPanel extends JPanel {
    private static final String[] TITLES = {
        "Setup", "Impact", "Savings & Emergency Fund",
        "Budget Rescue Plan", "Survival Mode", "Final Report"
    };
    private static final String[] DESCRIPTIONS = {
        "Enter your salary, savings and expenses, pick a crisis and run the simulation.",
        "See how each expense category changes and what your personal inflation is.",
        "Check monthly savings during the crisis and how long your fund would last.",
        "Find practical expense cuts to recover the savings lost during the crisis.",
        "See how many months your savings can survive if income stops completely.",
        "Read the complete report and save it as a text file."
    };
    private static final String[] PAGES = {
        "SETUP", "IMPACT", "SAVINGS", "RESCUE", "SURVIVAL", "REPORT"
    };
    private static final Color[] COLORS = {
        new Color(52, 120, 246),     // blue
        new Color(220, 70, 70),      // red
        new Color(40, 167, 100),     // green
        new Color(140, 90, 210),     // purple
        new Color(240, 140, 30),     // orange
        new Color(20, 150, 160)      // teal
    };

    private MenuCard[] cards = new MenuCard[TITLES.length];
    private JLabel hint = new JLabel("", SwingConstants.CENTER);

    public DashboardPanel(final MainFrame frame) {
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel heading = new JLabel("CrisisWallet", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 30));
        JLabel sub = new JLabel("Personal Financial Crisis Impact Simulator", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(new Color(110, 110, 110));
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 2));
        header.add(heading);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        for (int i = 0; i < TITLES.length; i++) {
            final String page = PAGES[i];
            cards[i] = new MenuCard(i + 1, TITLES[i], DESCRIPTIONS[i], COLORS[i],
                    new Runnable() {
                        public void run() {
                            frame.showPage(page);
                        }
                    }, i == 0);                 // only Setup is open at the start
            grid.add(cards[i]);
        }
        add(grid, BorderLayout.CENTER);

        hint.setFont(new Font("SansSerif", Font.ITALIC, 13));
        add(hint, BorderLayout.SOUTH);
        setUnlocked(false);
    }

    // called by MainFrame after a simulation has been run
    public void setUnlocked(boolean unlocked) {
        for (int i = 1; i < cards.length; i++) {
            cards[i].setUnlocked(unlocked);
        }
        hint.setText(unlocked
                ? "All sections are open. Click any box (press Esc inside a section to come back here)."
                : "Start with box 1: fill in your details and run a simulation to unlock the rest.");
    }
}
