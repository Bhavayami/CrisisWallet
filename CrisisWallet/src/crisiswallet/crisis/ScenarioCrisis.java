package crisiswallet.crisis;

import java.util.HashMap;
import java.util.Map;
import crisiswallet.model.User;

// Level 2 of the multi-level chain: Crisis -> ScenarioCrisis -> CovidCrisis ...
public class ScenarioCrisis extends Crisis {
    private int year;
    private Map<String, Double> percentages;

    // percent[] must follow the order of User.CATEGORIES
    public ScenarioCrisis(String name, String description, int year, double[] percent) {
        super(name, description);           // super constructor call
        this.year = year;
        this.percentages = new HashMap<String, Double>();
        for (int i = 0; i < User.CATEGORIES.length; i++) {
            percentages.put(User.CATEGORIES[i], percent[i]);
        }
    }

    public int getYear() { return year; }

    @Override
    public Map<String, Double> getPercentages() {   // implements the abstract method
        return percentages;
    }

    @Override
    public String describe() {                       // overriding
        return super.describe() + " (" + year + ")";
    }
}
