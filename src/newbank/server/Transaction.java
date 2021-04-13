package newbank.server;

import java.util.Date;

public class Transaction implements Comparable<Transaction> {

    private final Date timestamp;
    private final String accountNameTo;
    private final String accountNameFrom;
    private final double amountTransferred;


    /**
     * Instantiates a Transaction.
     *
     * @param accountNameTo     the account name sent to
     * @param accountNameFrom   the account name sent to
     * @param amountTransferred the Amount Transferred
     */
    public Transaction(String accountNameTo, String accountNameFrom, double amountTransferred) {
        this.timestamp = new Date();
        this.accountNameTo = accountNameTo;
        this.accountNameFrom = accountNameFrom;
        this.amountTransferred = amountTransferred;
    }

    /**
     * Returns the timestamp from a transaction
     *
     * @return Date and time of transaction
     */
    public Date getTimeStamp() {
        return timestamp;
    }

    /**
     * Returns the account transferred to
     *
     * @return account name to
     */
    public String getAccountNameTo() {
        return accountNameTo;
    }

    /**
     * Returns the account transferred from
     *
     * @return account name from
     */
    public String getAccountNameFrom() {
        return accountNameFrom;
    }

    /**
     * Returns the account transferred
     *
     * @return amount transferred
     */
    public double getAmountTransferred() {
        return amountTransferred;
    }

    /**
     * Parses the transactions as a string
     *
     * @return Transaction as a string {@literal "<Date> | <To> | <From> | <Amount>"}
     */
    public String toString() {
        return (timestamp + " | " + accountNameTo + " | " + accountNameFrom + " | " + amountTransferred);
    }

    /**
     * Method to compare transactions
     *
     * @return 0 false 1 true
     */
    @Override
    public int compareTo(Transaction transaction) {
        if (getTimeStamp() == null || transaction.getTimeStamp() == null) {
            return 0;
        }
        return getTimeStamp().compareTo(transaction.getTimeStamp());
    }
}
