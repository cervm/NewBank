package newbank.server;

import java.sql.Timestamp;

public class Transaction {

    private final Timestamp timestamp;
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
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.accountNameTo = accountNameTo;
        this.accountNameFrom = accountNameFrom;
        this.amountTransferred = amountTransferred;
    }

    public Timestamp getTimeStamp() { return timestamp; }

    public String getAccountNameTo() { return accountNameTo; }

    public String getAccountNameFrom() { return accountNameFrom; }

    public int getAmountTransferred() { return amountTransferred; }

    public String toString(){
        return (timestamp.toString() + " | " + accountNameTo + " | " + accountNameFrom + " | " + amountTransferred);
    }
}
