package newbank.server;

import java.util.ArrayList;
import java.sql.Timestamp;

/**
 * The type Account.
 */
public class Account {

    private final String accountName;
    private final double openingBalance;
    private final int accountNumber;
    private ArrayList<Transaction> transactions;


    /**
     * Instantiates a new Account.
     *
     * @param accountName    the account name
     * @param openingBalance the opening balance
     * @param accountNumber the accountNumber
     */
    public Account(String accountName, double openingBalance, int accountNumber) {
        this.accountName = accountName;
        this.openingBalance = openingBalance;
        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Returns the accountNumber.
     *
     * @return accountNumber
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns the balance.
     *
     * @return openingBalance
     */
    public double getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @return account name
     */
    public String getAccountName() {
        return this.accountName;
    }



    /**
     * Returns a string containing the account number the account name and the balance.
     *
     * @return A String described above.
     */
    public String toString() {
        return (accountNumber + " - " + accountName + ": " + "£" + openingBalance + "\n");
    }

    /**
     * Returns a nested Array List of strings containing the recent transactions from the account
     *
     * @return transactions
     */
    public ArrayList<Transaction> getTransactions() { return transactions; }

    //TODO: Print recent transactions (should it only return 10?
    public String getRecentTransactionsAsString() {
        String stringOut = null;
        for(Transaction trans : transactions){
            stringOut.concat(trans.toString());
            stringOut.concat("/n");
        }
        return stringOut;
    }

    public void addTransaction (String to, String from, Integer amount){
        transactions.add(new Transaction(to, from, amount));
    }

}
