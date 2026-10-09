package crisiswallet.crisis;

import java.util.Map;
import crisiswallet.util.Displayable;

public abstract class Crisis implements Displayable {
    protected String name;                  // protected: visible to subclasses
    protected String description;

    public Crisis(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // abstract: every crisis must supply its own percentage changes
    public abstract Map<String, Double> getPercentages();

    // final: subclasses cannot change this
    public final String getName() {
        return name;
    }

    public double getPercent(String category) {
        Double value = getPercentages().get(category);
        return value == null ? 0 : value;
    }

    public String describe() {
        return name + ": " + description;
    }

    @Override
    public String getSummary() {
        return describe();
    }

    @Override
    public String toString() {              // overriding Object class method
        return name;
    }
}
