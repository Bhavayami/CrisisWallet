package crisiswallet.calculator;

// One row of the comparison table. Comparable -> can be sorted with Collections.sort
public class CrisisResult implements Comparable<CrisisResult> {
    private final String name;
    private final double extraMonthly;
    private final double extraYearly;
    private final double inflation;

    public CrisisResult(String name, double extraMonthly, double extraYearly, double inflation) {
        this.name = name;
        this.extraMonthly = extraMonthly;
        this.extraYearly = extraYearly;
        this.inflation = inflation;
    }

    public String getName()         { return name; }
    public double getExtraMonthly() { return extraMonthly; }
    public double getExtraYearly()  { return extraYearly; }
    public double getInflation()    { return inflation; }

    @Override
    public int compareTo(CrisisResult other) {
        // biggest extra cost first
        return Double.compare(other.extraMonthly, this.extraMonthly);
    }
}
