package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// One small box showing a title and a big value.
// Several StatCards are placed together in a GridLayout row so they get equal size.
public class StatCard extends JPanel {
    private JLabel valueLabel = new JLabel("-", SwingConstants.CENTER);

    public StatCard(String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}
