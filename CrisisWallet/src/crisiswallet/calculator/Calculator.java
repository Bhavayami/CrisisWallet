package crisiswallet.calculator;

import crisiswallet.crisis.Crisis;
import crisiswallet.model.User;
import crisiswallet.util.Displayable;
import crisiswallet.util.Reportable;

// abstract class that implements TWO interfaces (multiple inheritance using interfaces)
public abstract class Calculator implements Displayable, Reportable {
    protected User user;
    protected Crisis crisis;
    protected double salaryCutPercent;

    public Calculator(User user, Crisis crisis, double salaryCutPercent) {
        this.user = user;
        this.crisis = crisis;
        this.salaryCutPercent = salaryCutPercent;
    }

    public abstract void calculate();

    protected double computeCrisisSalary() {
        return user.getSalary() * (1 - salaryCutPercent / 100);
    }
}
