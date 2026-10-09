package crisiswallet.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

// Graphics: line chart of savings (2D array data). Months after "visibleMonths" are hidden,
// so a thread can reveal the line one month at a time.
public class LineChartPanel extends JPanel {
    private double[][] table;
    private int visibleMonths;

    public LineChartPanel() {
        setPreferredSize(new Dimension(600, 340));
        setBackground(Color.WHITE);
    }

    public void setData(double[][] table, int visibleMonths) {
        this.table = table;
        this.visibleMonths = visibleMonths;
        repaint();
    }

    public void setVisibleMonths(int visibleMonths) {
        this.visibleMonths = visibleMonths;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("Savings projection (Rs.)", 10, 18);
        if (table == null) {
            g2.drawString("Run a simulation first", 10, 40);
            return;
        }

        int w = getWidth();
        int h = getHeight();
        int left = 90, right = 30, top = 50, bottom = 45;
        int plotW = w - left - right;
        int plotH = h - top - bottom;
        int months = table.length - 1;

        double min = 0, max = 0;
        for (int m = 0; m <= months; m++) {
            min = Math.min(min, Math.min(table[m][1], table[m][2]));
            max = Math.max(max, Math.max(table[m][1], table[m][2]));
        }
        double range = max - min;
        if (range == 0) {
            range = 1;
        }

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();

        // horizontal grid lines and y labels
        for (int i = 0; i <= 5; i++) {
            double value = min + range * i / 5;
            int y = top + plotH - (int) (plotH * i / 5.0);
            g2.setColor(new Color(225, 225, 225));
            g2.drawLine(left, y, w - right, y);
            g2.setColor(Color.DARK_GRAY);
            String label = String.format("%,.0f", value);
            g2.drawString(label, left - 8 - fm.stringWidth(label), y + 4);
        }

        // x labels
        int step = months > 12 ? 3 : 1;
        for (int m = 0; m <= months; m += step) {
            int x = left + (int) ((double) m / months * plotW);
            g2.setColor(Color.DARK_GRAY);
            String label = String.valueOf(m);
            g2.drawString(label, x - fm.stringWidth(label) / 2, h - bottom + 16);
        }
        g2.drawString("Month", left + plotW / 2 - 15, h - 8);

        // zero line
        int zeroY = top + (int) ((max - 0) / range * plotH);
        g2.setColor(Color.GRAY);
        g2.drawLine(left, zeroY, w - right, zeroY);

        // two series
        drawSeries(g2, 2, new Color(50, 110, 200), true, min, max, range, left, top, plotW, plotH, months);
        drawSeries(g2, 1, new Color(200, 60, 60), false, min, max, range, left, top, plotW, plotH, months);

        // legend
        g2.setColor(new Color(200, 60, 60));
        g2.fillRect(left, 28, 14, 4);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("With crisis", left + 20, 34);
        g2.setColor(new Color(50, 110, 200));
        g2.fillRect(left + 110, 28, 14, 4);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Without crisis", left + 130, 34);
    }

    private void drawSeries(Graphics2D g2, int column, Color color, boolean dashed,
                            double min, double max, double range,
                            int left, int top, int plotW, int plotH, int months) {
        g2.setColor(color);
        if (dashed) {
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[] {6f, 5f}, 0f));
        } else {
            g2.setStroke(new BasicStroke(2.5f));
        }
        int lastShown = Math.min(visibleMonths, months);
        int prevX = 0;
        int prevY = 0;
        for (int m = 0; m <= lastShown; m++) {
            int x = left + (int) ((double) m / months * plotW);
            int y = top + (int) ((max - table[m][column]) / range * plotH);
            if (m > 0) {
                g2.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }
        g2.fillOval(prevX - 4, prevY - 4, 8, 8);
        g2.setStroke(new BasicStroke(1f));
    }
}
