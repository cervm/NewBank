package newbank.server;

import java.util.HashMap;

/**
 * The type New bank.
 */
public class NewBank {

    private static final NewBank bank = new NewBank();
    private final HashMap<String, Customer> customers;
    private int nextAvailableAccountNumber = 10000000;

    private NewBank() {
        customers = new HashMap<>();
        addTestData();
    }

    /**
     * Gets bank.
     *
     * @return the bank
     */
    public static NewBank getBank() {
        return bank;
    }

    /**
     * Adds the testing data to the customer HashMap
     *
     */
    private void addTestData() {
        this.newCustomer("Bhagy", 1000.0);

        this.newCustomer("Christina", 1500.0);

        this.newCustomer("John", 250.0);
        this.newAccount("John", "Savings", 1000.0);

    }

    /**
     * Check log in details customer id.
     *
     * @param userName the user name
     * @param password the password
     * @return the customer id
     */
    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        if (customers.containsKey(userName)) {
            return new CustomerID(userName);
        }
        return null;
    }

    /**
     * Process request string.
     *
     * @param customer the customer
     * @param request  the request
     * @return the string
     */
// commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, String request) {
        if (customers.containsKey(customer.getKey())) {
            switch (request) {
                //FR1.2
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    //FR1.2

    /**
     * Returns an authenticated users accounts upon them entering SHOWMYACCOUNTS into the console.
     *
     * @param customer
     * @return A string of the customers accounts.
     */
    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    /**
     * Creates a new Customer with a Checking account.
     *
     * @param name
     * @param startingBalance
     */
    private void newCustomer(String name, double startingBalance) {
        customers.put(name, new Customer());
        this.newAccount(name, "Checking", startingBalance);
    }

    /**
     * Adds a new type of account to a customer.
     *
     * @param customer
     * @param accountType
     * @param balance
     */
    private void newAccount(String customer, String accountType, double balance){
        if (customers.get(customer) == null) {
            System.out.println("No Customer Exists");
            return;
        }
        customers.get(customer).addAccount(new Account(accountType, balance, nextAvailableAccountNumber));
        nextAvailableAccountNumber++;
    }



}
