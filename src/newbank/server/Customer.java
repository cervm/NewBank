package newbank.server;

import java.util.ArrayList;

/**
 * The type Customer.
 */
public class Customer {
    private final String userName;
    private final ArrayList<Account> accounts;
    private String password;
    private ArrayList<CustomerInfo> accountDetails;


    /**
     * Instantiates a new Customer.
     * Set userName, password and initialize accounts.
     */
    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.accountDetails = new ArrayList<>();
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
            totalBalance += account.getBalance();
            s.append(account.toString()).append("\n");
        }
        s.append("\nYour Balance across all your accounts is: £").append(totalBalance);
        return s.toString();
    }

    /**
     * Get the password for the user
     */
    public boolean authenticateUser(String password) {
        return this.password.equals(password);
    }

    /**
     * Set the password
     */
    public void setPassword(String newPW) {
        this.password = newPW;
    }

    /**
     *
     */
    public String getUserName() {
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
     * Returns the account based on the given name
     *
     * @param accountName the name of the account
     * @return Account
     */
    public Account getAccount(String accountName) {
        for (Account acc : accounts) {
            if (acc.getAccountName().equals(accountName)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Returns the account based on the given number
     *
     * @param accountNumber the number of the account
     * @return Account
     */
    public Account getAccount(int accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Returns the first available account
     *
     * @return Account
     */
    public Account getAccount() {
        try {
            return accounts.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Adds a string containing the customers personal details
     *
     * @param address     Customer address
     * @param phoneNumber   Customer phone number
     * @param fullName Customers full name
     * @param securityQuestion Customers security question
     */
    public void addAccountInfo(String address, String phoneNumber, String fullName, String securityQuestion) {
        accountDetails.add(new CustomerInfo(address, phoneNumber, fullName, securityQuestion));
    }
}
