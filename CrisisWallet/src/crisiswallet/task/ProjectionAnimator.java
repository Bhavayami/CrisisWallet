package crisiswallet.task;

import javax.swing.SwingUtilities;

// Thread 1: draws the savings line month by month (implements Runnable)
public class ProjectionAnimator implements Runnable {
    private final int totalMonths;
    private final AnimationListener listener;
    private volatile boolean stopped = false;

    public ProjectionAnimator(int totalMonths, AnimationListener listener) {
        this.totalMonths = totalMonths;
        this.listener = listener;
    }

    public void stop() {
        stopped = true;
    }

    @Override
    public void run() {
        try {
            for (int m = 0; m <= totalMonths && !stopped; m++) {
                final int month = m;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (!stopped) {
                            listener.onFrame(month);
                        }
                    }
                });
                Thread.sleep(150);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        if (!stopped) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.onFinished();
                }
            });
        }
    }
}
