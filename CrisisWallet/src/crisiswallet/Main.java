package crisiswallet;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import crisiswallet.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // if it fails, Swing simply keeps the default look
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
