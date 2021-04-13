package newbank.server;

import java.util.ArrayList;

/**
 * The type Customer.
 */
public class Customer {
    private final String userName;
    private final ArrayList<Account> accounts;
    private final CustomerID customerID;
    private String password;
    private CustomerInfo accountDetails;
    private String address;
    private String phoneNumber;
    private String fullName;
    private String securityQuestion;
    private ArrayList<Loan> loansTo;
    private ArrayList<Loan> loansFrom;


    /**
     * Instantiates a new Customer.
     * Set userName, password and initialize accounts.
     */
    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.accountDetails = new CustomerInfo("", "", "", "");
        this.customerID = new CustomerID(userName);
    }

    /**
     * Instantiates a new Customer.
     *
     * @param userName       the user name
     * @param password       the password
     * @param accounts       the accounts
     * @param accountDetails the account details
     * @param customerID     the customer id
     */
    public Customer(String userName, String password, ArrayList<Account> accounts, CustomerInfo accountDetails, CustomerID customerID) {
        this.userName = userName;
        this.password = password;
        this.accounts = accounts;
        this.accountDetails = accountDetails;
        this.customerID = customerID;
    }

    /**
     * Accounts to string string.
     *
     * @return the string
     */
    public String accountsToString() {
        StringBuilder s = new StringBuilder();
        double totalBalance = 0;
        for (Account account : accounts) {
            totalBalance += account.getBalance();
            s.append(account).append("\n");
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
     * @param address          Customer address
     * @param phoneNumber      Customer phone number
     * @param fullName         Customers full name
     * @param securityQuestion Customers security question
     */
    public void addAccountInfo(String address, String phoneNumber, String fullName, String securityQuestion) {
        this.accountDetails = new CustomerInfo(address, phoneNumber, fullName, securityQuestion);
    }

    /**
     * Returns a string containing the customers personal details
     *
     * @return Account details
     */
    public CustomerInfo getAccountInfo() {

        return accountDetails;
    }

    /**
     * Sets the address for the user
     *
     * @param address The customers new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the phone number for the user
     *
     * @param phoneNumber The customers new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the Full Name for the user
     *
     * @param fullName The customers new Full Name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets the Security Question for the user
     *
     * @param securityQuestion The customers new security question
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * Gets the customerID
     *
     * @return CustomerID
     */
    public CustomerID getCustomerID() {
        return this.customerID;
    }

    public Double getTotalBalance() {
        Double totalBalance = 0.0;
        for (Account account : this.getAccounts()) {
            totalBalance = totalBalance + account.getBalance();
        }
        return totalBalance;
    }
}
