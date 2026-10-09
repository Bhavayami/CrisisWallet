package crisiswallet.task;

// Callback interface: the animation thread tells the GUI what to do
public interface AnimationListener {
    void onFrame(int month);
    void onFinished();
}
