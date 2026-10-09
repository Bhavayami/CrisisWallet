package crisiswallet.crisis;

public class CovidCrisis extends ScenarioCrisis {
    public CovidCrisis() {
        // Order: Food, Fuel, Transport, Housing, Utilities, Healthcare, Shopping, Entertainment
        super("COVID-19 Pandemic",
              "Lockdowns, supply chain disruption, high medical costs",
              2020,
              new double[] {8, 3, 5, 2, 5, 20, 4, -10});
    }
}
