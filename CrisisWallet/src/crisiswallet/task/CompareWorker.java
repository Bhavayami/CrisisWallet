package crisiswallet.task;

import java.util.List;
import crisiswallet.calculator.CrisisResult;
import crisiswallet.calculator.ImpactCalculator;
import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;

// Thread 2: one thread per crisis (extends Thread). All threads add to one shared list.
public class CompareWorker extends Thread {
    private final User user;
    private final Crisis crisis;
    private final double salaryCut;
    private final List<CrisisResult> results;      // shared synchronized list

    public CompareWorker(User user, Crisis crisis, double salaryCut, List<CrisisResult> results) {
        super("compare-" + crisis.getName());
        this.user = user;
        this.crisis = crisis;
        this.salaryCut = salaryCut;
        this.results = results;
    }

    @Override
    public void run() {
        ImpactCalculator ic = new ImpactCalculator(user, crisis, salaryCut);
        ic.calculate();
        results.add(new CrisisResult(crisis.getName(), ic.getExtraMonthly(),
                ic.getExtraYearly(), ic.getPersonalInflation()));
    }
}
