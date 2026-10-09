package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import crisiswallet.crisis.Crisis;
import crisiswallet.crisis.CustomCrisis;
import crisiswallet.crisis.ScenarioCrisis;
import crisiswallet.model.InvalidInputException;
import crisiswallet.model.User;

public class SetupPanel extends JPanel implements ActionListener {
    private MainFrame frame;
    private List<Crisis> crises;

    private JTextField salaryField = new JTextField();
    private JTextField savingsField = new JTextField();
    private JTextField[] expenseFields = new JTextField[User.CATEGORIES.length];   // 1D array
    private JTextField[] customFields = new JTextField[User.CATEGORIES.length];

    private JComboBox<String> crisisBox = new JComboBox<String>();
    private JTextArea infoArea = new JTextArea(8, 20);
    private JTextField cutField = new JTextField("0");
    private JButton sampleButton = new JButton("Fill sample data");
    private JButton simulateButton = new JButton("Simulate crisis");
    private JLabel statusLabel = new JLabel(" ");

    public SetupPanel(MainFrame frame, List<Crisis> crises) {
        this.frame = frame;
        this.crises = crises;
        setLayout(new GridLayout(1, 2, 15, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildProfilePanel());
        add(buildCrisisPanel());

        crisisBox.addActionListener(this);
        sampleButton.addActionListener(this);
        simulateButton.addActionListener(this);
        updateCrisisInfo();
    }

    private JPanel buildProfilePanel() {
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 8));
        form.add(new JLabel("Monthly salary (Rs.)"));
        form.add(salaryField);
        form.add(new JLabel("Current savings (Rs.)"));
        form.add(savingsField);
        for (int i = 0; i < User.CATEGORIES.length; i++) {
            expenseFields[i] = new JTextField();
            form.add(new JLabel("Monthly " + User.CATEGORIES[i]));
            form.add(expenseFields[i]);
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("1. Your financial profile"));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(form, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCrisisPanel() {
        for (Crisis c : crises) {
            crisisBox.addItem(c.getName());
        }
        crisisBox.addItem("Custom Crisis (enter your own %)");

        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.add(crisisBox, BorderLayout.NORTH);
        top.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JPanel custom = new JPanel(new GridLayout(0, 2, 6, 6));
        custom.setBorder(BorderFactory.createTitledBorder("Custom crisis: % change per category"));
        for (int i = 0; i < User.CATEGORIES.length; i++) {
            customFields[i] = new JTextField("0");
            custom.add(new JLabel(User.CATEGORIES[i]));
            custom.add(customFields[i]);
        }

        JPanel cutRow = new JPanel(new GridLayout(1, 2, 6, 0));
        cutRow.add(new JLabel("Salary cut during crisis (%)"));
        cutRow.add(cutField);

        JPanel bottom = new JPanel(new GridLayout(0, 1, 6, 6));
        bottom.add(cutRow);
        bottom.add(sampleButton);
        bottom.add(simulateButton);
        bottom.add(statusLabel);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("2. Choose a crisis"),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)));
        panel.add(top, BorderLayout.NORTH);
        panel.add(custom, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void updateCrisisInfo() {
        int index = crisisBox.getSelectedIndex();
        boolean custom = index >= crises.size();
        for (JTextField field : customFields) {
            field.setEnabled(custom);
        }
        if (custom) {
            infoArea.setText("Type your own percentage change for each category below "
                    + "(for example Food 15, Fuel 25). Negative values are allowed.");
        } else {
            Crisis c = crises.get(index);
            StringBuilder sb = new StringBuilder(c.describe());
            sb.append("\n\nScenario assumptions (% change):\n");
            for (String category : User.CATEGORIES) {
                sb.append("  ").append(category).append(": ")
                  .append(String.format("%+.0f%%", c.getPercent(category))).append("\n");
            }
            infoArea.setText(sb.toString());
        }
        infoArea.setCaretPosition(0);
    }

    private void fillSample() {
        salaryField.setText("50000");
        savingsField.setText("200000");
        String[] sample = {"8000", "4000", "2000", "12000", "3000", "2000", "3000", "2000"};
        for (int i = 0; i < expenseFields.length; i++) {
            expenseFields[i].setText(sample[i]);
        }
        cutField.setText("0");
    }

    // Reads a number from a text field; throws the CHECKED exception for bad input
    private double parse(JTextField field, String label, boolean allowNegative)
            throws InvalidInputException {
        String text = field.getText().trim().replace(",", "");
        if (text.isEmpty()) {
            throw new InvalidInputException(label + " is empty");
        }
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {            // unchecked exception caught here
            throw new InvalidInputException(label + " must be a number");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new InvalidInputException(label + " is not a valid number");
        }
        if (!allowNegative && value < 0) {
            throw new InvalidInputException(label + " cannot be negative");
        }
        return value;
    }

    private void simulate() {
        simulateButton.setEnabled(false);
        try {
            double salary = parse(salaryField, "Monthly salary", false);
            double savings = parse(savingsField, "Current savings", false);
            User user = new User(salary, savings);
            for (int i = 0; i < User.CATEGORIES.length; i++) {
                user.setExpense(User.CATEGORIES[i], parse(expenseFields[i], User.CATEGORIES[i], false));
            }
            double cut = parse(cutField, "Salary cut", false);
            if (cut > 100) {
                throw new InvalidInputException("Salary cut cannot be more than 100%");
            }

            Crisis chosen;
            int index = crisisBox.getSelectedIndex();
            if (index < crises.size()) {
                chosen = crises.get(index);
            } else {
                CustomCrisis custom = new CustomCrisis();
                for (int i = 0; i < User.CATEGORIES.length; i++) {
                    custom.setPercent(User.CATEGORIES[i],
                            parse(customFields[i], "Custom % for " + User.CATEGORIES[i], true));
                }
                chosen = custom;
            }

            // downcasting: only ScenarioCrisis has getYear()
            if (chosen instanceof ScenarioCrisis) {
                ScenarioCrisis scenario = (ScenarioCrisis) chosen;
                statusLabel.setText("Simulated: " + scenario.getName() + " (" + scenario.getYear() + ")");
            } else {
                statusLabel.setText("Simulated: custom crisis");
            }
            frame.runSimulation(user, chosen, cut);
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid input",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid input",
                    JOptionPane.WARNING_MESSAGE);
        } finally {
            simulateButton.setEnabled(true);           // always runs
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == crisisBox) {
            updateCrisisInfo();
        } else if (source == sampleButton) {
            fillSample();
        } else if (source == simulateButton) {
            simulate();
        }
    }
}
