package newbank.server;

import java.util.HashMap;

/**
 * The type New bank.
 */
public class NewBank {

    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;

    private NewBank() {
        customers = new HashMap<>();
        addTestData();
    }

    private void addTestData() {
        Customer bhagy = new Customer("Bhagy", "bhagy");
        bhagy.addAccount(new Account("Main", 1000.0));
        customers.put(bhagy.getUserName(), bhagy);

        Customer christina = new Customer("Christina", "christina");
        christina.addAccount(new Account("Savings", 1500.0));
        customers.put(christina.getUserName(), christina);

        Customer john = new Customer("John", "john");
        john.addAccount(new Account("Checking", 250.0));
        customers.put(john.getUserName(), john);
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
                    return resetPassword(customer, splited[1], splited[2]);
                case "ADDACCOUNT":
                    return addAccount(customer, splited[1]);
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
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
        Account newAccount = new Account(accountName, 0.0);
        customers.get(customer.getKey()).addAccount(newAccount);
        return "New Account " + accountName+ " added.";
    }

}
