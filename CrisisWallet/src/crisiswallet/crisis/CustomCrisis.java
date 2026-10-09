package crisiswallet.crisis;

import java.util.HashMap;
import java.util.Map;

// Crisis whose percentages are typed in by the user
public class CustomCrisis extends Crisis {
    private Map<String, Double> percentages = new HashMap<String, Double>();

    public CustomCrisis() {
        super("Custom Crisis", "Percentages entered by the user");
    }

    public void setPercent(String category, double value) {
        percentages.put(category, value);
    }

    @Override
    public Map<String, Double> getPercentages() {
        return percentages;
    }
}
