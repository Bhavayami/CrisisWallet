package crisiswallet.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import crisiswallet.report.ReportGenerator;

public class ReportPanel extends JPanel implements ActionListener {
    private JTextArea area = new JTextArea();
    private JButton saveButton = new JButton("Save report as .txt");
    private JButton copyButton = new JButton("Copy to clipboard");

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(area), BorderLayout.CENTER);

        // GridLayout(1, 3): two equal buttons and an empty cell on the right
        JPanel top = new JPanel(new GridLayout(1, 3, 10, 0));
        top.add(saveButton);
        top.add(copyButton);
        top.add(new JPanel());
        add(top, BorderLayout.NORTH);
        saveButton.addActionListener(this);
        copyButton.addActionListener(this);
    }

    public void setText(String text) {
        area.setText(text);
        area.setCaretPosition(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == copyButton) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                   .setContents(new StringSelection(area.getText()), null);
            JOptionPane.showMessageDialog(this, "Report copied to clipboard.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("CrisisWallet_Report.txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ReportGenerator.saveToFile(area.getText(), chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Report saved.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not save file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
