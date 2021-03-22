package newbank.server;

import java.util.Date;

public class Transaction implements Comparable<Transaction>{

    private final Date timestamp;
    private final String accountNameTo;
    private final String accountNameFrom;
    private final int amountTransferred;


    /**
     * Instantiates a Transaction.
     *
     * @param accountNameTo    the account name sent to
     * @param accountNameFrom the account name sent to
     * @param amountTransferred the Amount Transferred
     */
    public Transaction(String accountNameTo, String accountNameFrom, int amountTransferred) {
        this.timestamp = new Date();
        this.accountNameTo = accountNameTo;
        this.accountNameFrom = accountNameFrom;
        this.amountTransferred = amountTransferred;
    }

    public Date getTimeStamp() { return timestamp; }

    public String getAccountNameTo() { return accountNameTo; }

    public String getAccountNameFrom() { return accountNameFrom; }

    public int getAmountTransferred() { return amountTransferred; }

    public String toString(){
        return (timestamp.toString() + " | " + accountNameTo + " | " + accountNameFrom + " | " + amountTransferred);
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (getTimeStamp() == null || transaction.getTimeStamp() == null) {
            return 0;
        }
        return getTimeStamp().compareTo(transaction.getTimeStamp());
    }
}
