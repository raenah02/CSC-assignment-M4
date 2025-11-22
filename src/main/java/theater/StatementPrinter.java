package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a textual statement for a customer's invoice.
 */
public class StatementPrinter {
    private static final int TRAGEDY_BASE_AMOUNT = 40000;
    private static final int TRAGEDY_EXTRA_AMOUNT_PER_PERSON = 1000;
    private static final int TRAGEDY_AUDIENCE_THRESHOLD = 30;
    private static final int CURRENCY_CONVERSION_FACTOR = 100;

    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Map<String, Play> getPlays() {
        return plays;
    }

    /**
     * Returns a formatted statement of the invoice.
     *
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is unknown
     */
    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;
        final StringBuilder result = new StringBuilder("Statement for "
                + invoice.getCustomer() + System.lineSeparator());

        for (Performance performance : invoice.getPerformances()) {
            final int amount = getAmount(performance);
            final Play play = getPlay(performance);

            result.append(String.format("  %s: %s (%s seats)%n",
                    play.getName(),
                    usd(amount),
                    performance.getAudience()));

            totalAmount += amount;
            volumeCredits += getVolumeCredits(performance);
        }

        result.append(String.format("Amount owed is %s%n", usd(totalAmount)));
        result.append(String.format("You earned %s credits%n", volumeCredits));

        return result.toString();
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    private int getAmount(Performance performance) {
        final Play play = getPlay(performance);
        int amount;
        switch (play.getType()) {
            case "tragedy":
                amount = TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > TRAGEDY_AUDIENCE_THRESHOLD) {
                    amount += TRAGEDY_EXTRA_AMOUNT_PER_PERSON
                            * (performance.getAudience() - TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                amount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    amount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                amount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return amount;
    }

    private int getVolumeCredits(Performance performance) {
        int result = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    /**
     * Converts an amount in cents to a formatted USD string.
     *
     * @param amount the amount in cents
     * @return the formatted USD string
     */
    private String usd(int amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount / (double) Constants.PERCENT_FACTOR);
    }
}



