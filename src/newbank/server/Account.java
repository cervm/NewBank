package newbank.server;

import java.util.ArrayList;

/**
 * The type Account.
 */
public class Account {

    private final String accountName;
    private final double openingBalance;
    private final int accountNumber;
    private ArrayList<ArrayList<Object>> transactions;


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
        this.transactions = new ArrayList<ArrayList<Object>>();
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

    //TODO: test function

    /**
     * Returns a nested Array List of strings containing the recent transactions from the account
     *
     * @return transactions
     */
    public ArrayList<ArrayList<Object>> getTransactions() { return this.transactions; }

    /**
     * Returns a string containing the account number the account name and the balance.
     *
     * @return A String described above.
     */
    public String toString() {
        return (accountNumber + " - " + accountName + ": " + "£" + openingBalance + "\n");
    }

    //TODO: Print recent transactions (should it only return 10?
    public String getRecentTransactions() {
        String stringOut = null;
        for (ArrayList<Object> transaction : this.transactions) {
            stringOut.concat(transaction.get(0).toString());
            stringOut.concat(" | ");
            stringOut.concat(transaction.get(1).toString());
            stringOut.concat(System.lineSeparator());
        }
        return stringOut;
    }

    public void addTransaction (Account to, Integer amount){
        ArrayList<Object> transaction = new ArrayList();
        transaction.add(to);
        transaction.add(amount);
        this.transactions.add(transaction);
    }

}
