package newbank.server;

import java.util.HashMap;

/**
 * The type New bank.
 */
public class NewBank {

    private static NewBank bank = null;

    static {
        try {
            bank = new NewBank();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final HashMap<String, Customer> customers;
    private int nextAvailableAccountNumber = 10000000;

    private NewBank() throws Exception {
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
    private void addTestData() throws Exception {
        Customer bhagy = new Customer();
        bhagy.addAccount(newAccount("Main", 1000.0));
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(newAccount("Savings", 1500.0));
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(newAccount("Checking", 250.0));
        customers.put("John", john);
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
     * Generates a new account.
     *
     * @param accountType
     * @param balance
     */
    private Account newAccount(String accountType, double balance) throws Exception {
        if (nextAvailableAccountNumber > 99999999) {
            throw new Exception("No available account numbers");
        }

        return new Account(accountType, balance, nextAvailableAccountNumber++);
    }



}
