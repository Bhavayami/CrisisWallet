package crisiswallet.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

// Graphics: bar chart of extra monthly cost per category (drawn by hand)
public class BarChartPanel extends JPanel {
    private String[] labels = new String[0];
    private double[] values = new double[0];

    public BarChartPanel() {
        setPreferredSize(new Dimension(420, 240));
        setBackground(Color.WHITE);
    }

    public void setData(String[] labels, double[] values) {
        this.labels = labels;
        this.values = values;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("Extra monthly cost by category (Rs.)", 10, 18);
        if (values.length == 0) {
            return;
        }

        int left = 15, right = 15, top = 50, bottom = 40;
        double maxV = 0, minV = 0;
        for (double v : values) {
            maxV = Math.max(maxV, v);
            minV = Math.min(minV, v);
        }
        double range = maxV - minV;
        if (range == 0) {
            range = 1;
        }
        int plotH = h - top - bottom;
        int plotW = w - left - right;
        int zeroY = top + (int) (maxV / range * plotH);

        g2.setColor(Color.GRAY);
        g2.drawLine(left, zeroY, w - right, zeroY);

        int slot = plotW / values.length;
        int barW = (int) (slot * 0.6);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < values.length; i++) {
            int x = left + i * slot + (slot - barW) / 2;
            int barH = (int) Math.round(Math.abs(values[i]) / range * plotH);
            int y = values[i] >= 0 ? zeroY - barH : zeroY;

            g2.setColor(values[i] >= 0 ? new Color(200, 60, 60) : new Color(60, 160, 90));
            g2.fillRect(x, y, barW, barH);

            g2.setColor(Color.BLACK);
            String val = String.format("%,.0f", values[i]);
            int valY = values[i] >= 0 ? y - 4 : y + barH + 12;
            g2.drawString(val, x + (barW - fm.stringWidth(val)) / 2, valY);
            String name = labels[i];
            while (name.length() > 3 && fm.stringWidth(name) > slot - 2) {
                name = name.substring(0, name.length() - 2) + ".";   // shorten to fit
            }
            g2.drawString(name, x + (barW - fm.stringWidth(name)) / 2, h - 14);
        }
    }
}
