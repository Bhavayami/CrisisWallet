package crisiswallet.util;

import java.util.Locale;

// final class with a private constructor and only static methods
public final class MoneyFormat {

    private MoneyFormat() { }

    public static String rs(double value) {
        return String.format(Locale.US, "Rs. %,.2f", value);
    }

    public static String signedRs(double value) {
        return String.format(Locale.US, "%+,.2f", value);
    }

    public static String percent(double value) {
        return String.format(Locale.US, "%.2f%%", value);
    }
}
