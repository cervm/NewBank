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
    public String accountsToString() {
        String s = "";
        for (Account a : accounts) {
            s += a.toString();
        }
        return s;
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
