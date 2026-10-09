package crisiswallet.crisis;

// final class: cannot be extended any further
public final class LpgShortageCrisis extends ScenarioCrisis {
    public LpgShortageCrisis() {
        super("LPG Cooking Gas Shortage",
              "Cylinder shortage, queues and black-market prices",
              2026,
              new double[] {12, 5, 6, 2, 25, 2, 3, 0});
    }

    @Override
    public String describe() {
        return super.describe() + " [LPG cost counted under Utilities]";
    }
}
