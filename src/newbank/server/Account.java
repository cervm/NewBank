package newbank.server;

/**
 * The type Account.
 */
public class Account {

    private String accountName;
    private double openingBalance;

    /**
     * Instantiates a new Account.
     *
     * @param accountName    the account name
     * @param openingBalance the opening balance
     */
    public Account(String accountName, double openingBalance) {
        this.accountName = accountName;
        this.openingBalance = openingBalance;
    }

    /**
     *
     * @return account name
     */
    public String getAccountName(){
        return this.accountName;
    }

    public String toString() {
        return (accountName + ": " + openingBalance);
    }

}
