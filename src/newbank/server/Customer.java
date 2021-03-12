package newbank.server;

import java.util.ArrayList;

/**
 * The type Customer.
 */
public class Customer {

    private ArrayList<Account> accounts;

    /**
     * Instantiates a new Customer.
     */
    public Customer() {
        accounts = new ArrayList<>();
    }

    /**
     * Accounts to string string.
     *
     * @return the string
     */
    //FR1.2
    public String accountsToString() {
        StringBuilder s = new StringBuilder();
        double totalBalance = 0;
        for (Account account : accounts) {
            totalBalance += account.getOpeningBalance();
            s.append(account.toString()).append("\n");
        }
        s.append("\nYour Balance across all your accounts is: £").append(totalBalance);
        return s.toString();
    }

    /**
     * Add account.
     *
     * @param account the account
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Get a customers Accounts
     *
     * @return accounts the accounts.
     */
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
