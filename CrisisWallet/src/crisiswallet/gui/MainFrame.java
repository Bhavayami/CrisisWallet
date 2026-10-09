package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import crisiswallet.calculator.CrisisResult;
import crisiswallet.calculator.EmergencyFundCalculator;
import crisiswallet.calculator.ImpactCalculator;
import crisiswallet.calculator.ProjectionCalculator;
import crisiswallet.calculator.SavingsCalculator;
import crisiswallet.crisis.*;
import crisiswallet.model.User;
import crisiswallet.report.ReportGenerator;

// JFrame (inheritance from another package). A CardLayout shows ONE screen at a time:
// the home screen with six boxes, or one of the six section pages.
public class MainFrame extends JFrame {
    public static final String HOME = "HOME";

    private List<Crisis> crises = new ArrayList<Crisis>();     // upcasting: subclasses stored as Crisis
    private CardLayout cardLayout = new CardLayout();
    private JPanel content = new JPanel(cardLayout);

    private DashboardPanel dashboard;
    private SetupPanel setupPanel;
    private ImpactPanel impactPanel = new ImpactPanel();
    private SavingsPanel savingsPanel = new SavingsPanel();
    private BudgetRescuePanel budgetRescuePanel = new BudgetRescuePanel();
    private SurvivalModePanel survivalModePanel = new SurvivalModePanel();
    private ReportPanel reportPanel = new ReportPanel();

    // result of the latest simulation (used to build the report)
    private User user;
    private Crisis chosen;
    private double salaryCut;
    private ImpactCalculator impact;
    private SavingsCalculator savings;
    private EmergencyFundCalculator fund;
    private ProjectionCalculator projection;

    public MainFrame() {
        super("CrisisWallet - Personal Financial Crisis Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        crises.add(new CovidCrisis());
        crises.add(new RussiaUkraineCrisis());
        crises.add(new MiddleEastCrisis());
        crises.add(new LpgShortageCrisis());

        setupPanel = new SetupPanel(this, crises);
        dashboard = new DashboardPanel(this);

        content.add(dashboard, HOME);
        content.add(buildPage("1. Setup", setupPanel), "SETUP");
        content.add(buildPage("2. Impact", impactPanel), "IMPACT");
        content.add(buildPage("3. Savings & Emergency Fund", savingsPanel), "SAVINGS");
        content.add(buildPage("4. Budget Rescue Plan", budgetRescuePanel), "RESCUE");
        content.add(buildPage("5. Survival Mode", survivalModePanel), "SURVIVAL");
        content.add(buildPage("6. Final Report", reportPanel), "REPORT");

        add(content);
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        // Esc = back to the home screen
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "goHome");
        getRootPane().getActionMap().put("goHome", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showPage(HOME);
            }
        });
        showPage(HOME);
    }

    // Wraps a section in a page with a header bar: [ Back to Home | title | (empty) ]
    private JPanel buildPage(String title, JComponent body) {
        JButton back = new JButton("< Back to Home");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPage(HOME);
            }
        });
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.add(back);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel header = new JPanel(new GridLayout(1, 3, 10, 0));     // 3 equal columns
        header.add(left);
        header.add(titleLabel);
        header.add(new JPanel());
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JPanel page = new JPanel(new BorderLayout());
        page.add(header, BorderLayout.NORTH);
        page.add(body, BorderLayout.CENTER);
        return page;
    }

    public void showPage(String name) {
        cardLayout.show(content, name);
    }

    public void runSimulation(User user, Crisis crisis, double salaryCut) {
        this.user = user;
        this.chosen = crisis;
        this.salaryCut = salaryCut;
        impact = new ImpactCalculator(user, crisis, salaryCut);
        impact.calculate();
        savings = new SavingsCalculator(user, crisis, salaryCut, impact);
        savings.calculate();
        fund = new EmergencyFundCalculator(user, crisis, salaryCut, impact);
        fund.calculate();
        projection = new ProjectionCalculator(user, crisis, salaryCut, savings, 12);
        projection.calculate();

        impactPanel.update(crisis, impact);
        savingsPanel.update(savings, fund);
        budgetRescuePanel.update(user, impact, savings);
        survivalModePanel.update(user, crisis, impact, savings, fund);

        refreshReport();
        dashboard.setUnlocked(true);            // the other five boxes can now be opened
        showPage("IMPACT");
    }

    // Kept for compatibility with the existing comparison panel source.
    public void setComparison(List<CrisisResult> sorted) {
        // Comparison is no longer a home-screen section, so its old callback is intentionally unused.
    }

    private void refreshReport() {
        reportPanel.setText(ReportGenerator.build(user, chosen, salaryCut,
                impact, savings, fund, projection));
    }
}
