package newbank.server;

import java.util.HashMap;
import java.util.Scanner;

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
        Customer bhagy = new Customer();
        bhagy.addAccount(new Account("Main", 1000.0));
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(new Account("Savings", 1500.0));
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(new Account("Checking", 250.0));
        customers.put("John", john);
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
        String moveRequest = new String();
        String movingTo = new String();
        if (customers.containsKey(customer.getKey())) {
            switch (request) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "MOVE":
                    return move(customer);
                case "from:Main":
                case "from:Savings":
                case "from Checking":
                    return moveTo(customer);
                case "to:Main":
                case "to:Savings":
                case "to:Checking":
                    return moveAmount(customer);
                case "TRANSFER": //test case to test Transfer method below
                    return transfer(request);
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    private String move(CustomerID customer) {
        return "Which account would you like to transfer from?" + " " +
                (customers.get(customer.getKey())).accountsToString();
    }

    private String moveTo(CustomerID customer) {
        return "Which account would you like to transfer to?" + " " +
                (customers.get(customer.getKey())).accountsToString();
    }
    private String moveAmount(CustomerID customer) {
        return "How much would you like to transfer?";
    }
    private String transfer(String request) {
        String result = "";
        Scanner scan = new Scanner(System.in); //scanner used to accomodate for doubles
        System.out.println("Which account would you like to transfer from?");
        String fromAccount = scan.next();
        System.out.println("Which account would you like to transfer to?");
        String toAccount = scan.next();
        System.out.println("How much would you like to transfer?");
        Double moveAmount = scan.nextDouble();

        if (moveAmount > 0) {
            result = "success";
            return result;
        }
        return "FAIL";
    }
}