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
        Customer bhagy = new Customer("Bhagy", "bhagy");
        bhagy.addAccount(newAccount("Main", 1000.0));
        customers.put(bhagy.getUserName(), bhagy);

        Customer christina = new Customer("Christina", "christina");
        christina.addAccount(newAccount("Savings", 1500.0));
        customers.put(christina.getUserName(), christina);

        Customer john = new Customer("John", "john");
        john.addAccount(newAccount("Checking", 250.0));
        customers.put(john.getUserName(), john);
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
            Customer c = customers.get(userName);
            if (c.authenticateUser(password)){
                return new CustomerID(userName);
            }
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
            String[] splited = request.split("\\s+");
            switch (splited[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "RESETPASSWORD":
                    if(splited.length<3){
                        return "Fail";
                    }
                    return resetPassword(customer, splited[1], splited[2]);
                case "ADDACCOUNT":
                    if(splited.length<2){
                        return "Fail";
                    }
                    return addAccount(customer, splited[1]);
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

    private String resetPassword(CustomerID customer, String newPassword1, String newPassword2){
        if(newPassword1.equals(newPassword2)){
            customers.get(customer.getKey()).setPassword(newPassword1);
            return "Password changed";
        }
        else{
            return "New Password not match.";
        }
    }

    private String addAccount(CustomerID customer, String accountName){
        for(Account acc: customers.get(customer.getKey()).getAccounts()){
            if(acc.getAccountName().equals(accountName)){
                return "Account already exists.";
            }
        }
        Account newAccount = newAccount(accountName, 0.0);
        customers.get(customer.getKey()).addAccount(newAccount);
        return "New Account " + accountName+ " added.";
    }

}
