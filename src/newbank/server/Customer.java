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
        for (Account account : accounts) {
            s.append(account.toString());
        }
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
}
