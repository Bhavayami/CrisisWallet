package crisiswallet.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

// One clickable box on the home screen (drawn with Graphics2D, like the chart panels).
public class MenuCard extends JPanel {
    private final int number;
    private final String title;
    private final String description;
    private final Color accent;
    private final Runnable action;

    private boolean unlocked;
    private boolean hover = false;
    private boolean pressed = false;

    public MenuCard(int number, String title, String description, Color accent,
                    Runnable action, boolean unlocked) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.accent = accent;
        this.action = action;
        setOpaque(false);
        setPreferredSize(new Dimension(260, 200));
        setUnlocked(unlocked);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean click = pressed && hover;
                pressed = false;
                repaint();
                if (click && MenuCard.this.unlocked) {
                    MenuCard.this.action.run();
                }
            }
        });
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        setCursor(Cursor.getPredefinedCursor(unlocked ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        setToolTipText(unlocked ? null : "Run a simulation in 1. Setup to unlock this section");
        repaint();
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    // mix a colour with white; amount 0 = original, 1 = white
    private static Color lighten(Color c, double amount) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * amount);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * amount);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * amount);
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth() - 8;
        int h = getHeight() - 8;
        int x = 2;
        int y = pressed && unlocked ? 4 : 2;
        Color main = unlocked ? accent : new Color(170, 170, 170);

        // soft shadow
        g.setColor(new Color(0, 0, 0, 35));
        g.fill(new RoundRectangle2D.Double(x + 3, y + 4, w, h, 22, 22));

        // card body
        RoundRectangle2D body = new RoundRectangle2D.Double(x, y, w, h, 22, 22);
        Color fill = !unlocked ? new Color(238, 238, 238)
                : hover ? lighten(accent, 0.90) : Color.WHITE;
        g.setColor(fill);
        g.fill(body);

        // coloured band at the top (clipped to the rounded corners)
        g.setClip(body);
        g.setColor(main);
        g.fillRect(x, y, w, 10);
        g.setClip(null);

        // border
        g.setColor(main);
        g.setStroke(new BasicStroke(hover && unlocked ? 3f : 2f));
        g.draw(body);

        // number badge
        int badge = 44;
        int bx = x + 20;
        int by = y + 28;
        g.setColor(main);
        g.fillOval(bx, by, badge, badge);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        String num = String.valueOf(number);
        g.drawString(num, bx + (badge - fm.stringWidth(num)) / 2,
                by + (badge + fm.getAscent() - fm.getDescent()) / 2);

        // title
        int ty = by + badge + 30;
        g.setColor(unlocked ? new Color(35, 35, 35) : new Color(130, 130, 130));
        int size = 18;
        g.setFont(new Font("SansSerif", Font.BOLD, size));
        while (size > 12 && g.getFontMetrics().stringWidth(title) > w - 40) {
            size--;                                     // shrink long titles to fit the box
            g.setFont(new Font("SansSerif", Font.BOLD, size));
        }
        g.drawString(title, x + 20, ty);

        // description (word wrapped, stops before the bottom line)
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(unlocked ? new Color(90, 90, 90) : new Color(150, 150, 150));
        fm = g.getFontMetrics();
        int lineY = ty + 22;
        for (String line : wrap(description, fm, w - 40)) {
            if (lineY > y + h - 38) {
                break;
            }
            g.drawString(line, x + 20, lineY);
            lineY += fm.getHeight();
        }

        // bottom status line
        g.setFont(new Font("SansSerif", unlocked ? Font.BOLD : Font.ITALIC, 13));
        g.setColor(unlocked ? accent : new Color(140, 140, 140));
        g.drawString(unlocked ? "Click to open  >" : "Locked - run Setup first", x + 20, y + h - 16);
        g.dispose();
    }

    // splits text into lines that fit the given pixel width
    private static java.util.List<String> wrap(String text, FontMetrics fm, int width) {
        java.util.List<String> lines = new java.util.ArrayList<String>();
        StringBuilder current = new StringBuilder();
        for (String word : text.split(" ")) {
            String test = current.length() == 0 ? word : current + " " + word;
            if (fm.stringWidth(test) > width && current.length() > 0) {
                lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(test);
            }
        }
        if (current.length() > 0) {
            lines.add(current.toString());
        }
        return lines;
    }
}
