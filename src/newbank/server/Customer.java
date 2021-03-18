package newbank.server;

import java.util.ArrayList;

/**
 * The type Customer.
 */
public class Customer {
    private String userName;
    private ArrayList<Account> accounts;
    private String password;


    /**
     * Instantiates a new Customer.
     * Set userName, password and initialize accounts.
     */
    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
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
     * Get the password for the user
     */
    public boolean authenticateUser(String password){
        return this.password.equals(password);
    }

    /**
     * Set the password
     */
    public void setPassword(String newPW){
        this.password = newPW;
    }

    /**
     *
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * Get a customers Accounts
     *
     * @return accounts the accounts.
     */
    public ArrayList<Account> getAccounts() {
        return this.accounts;
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
