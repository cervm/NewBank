package newbank.server;

/**
 * The type Account.
 */
public class Account {

    private String accountName;
    private static double openingBalance;

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

    public String toString() {
        return (accountName + ": " + openingBalance);
    }

    public static double getOpeningBalance() {
        return openingBalance;
    }
}
