package newbank.server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


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
    private static final int MAXIMUM_ACCOUNT_NUMBER = 99999999;

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
     */
    private void addTestData() throws Exception {
        Customer bhagy = new Customer("Bhagy", "bhagy");
        bhagy.addAccount(newAccount("Main", 1000.0));
        bhagy.addAccount(newAccount("Savings", 1000.0));
        customers.put(bhagy.getUserName(), bhagy);

        Customer christina = new Customer("Christina", "christina");
        christina.addAccount(newAccount("Savings", 1500.0));
        customers.put(christina.getUserName(), christina);

        Customer john = new Customer("John", "john");
        john.addAccount(newAccount("Checking", 250.0));
        john.addAccount(newAccount("Savings", 10000));
        john.getCustomerAccountByName("Checking").addTransaction("Checking", "Test", 100);
        john.getCustomerAccountByName("Checking").addTransaction("Savings", "Test", 1000);
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
            if (c.authenticateUser(password)) {
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
            String[] tokens = request.split("\\s+");
            switch (tokens[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "RESETPASSWORD":
                    if (tokens.length < 3) {
                        break;
                    }
                    return resetPassword(customer, tokens[1], tokens[2]);
                case "ADDACCOUNT":
                    if (tokens.length < 2) {
                        break;
                    }
                    return addAccount(customer, tokens[1]);
                case "MOVE":
                    if (tokens.length != 4) {
                        break;
                    }
                    double amount;
                    try {
                        amount = Double.parseDouble(tokens[1]);
                    } catch (NumberFormatException e) {
                        break;
                    }
                    return move(customer, amount, tokens[2], tokens[3]);
                case "Test":
                    return testJSON();
                case "SHOWACCOUNT":
                    if (tokens.length < 2) {
                        break;
                    }
                    return showAccount(customer, tokens[1]);
                case "SHOWTRANSACTIONS":
                    return showTransactions(customer);
                case "HELP":
                    return help();

                default:
                    break;
            }
        }
        return "FAIL";
    }

    //FR1.2

    /**
     * Returns an authenticated users accounts upon them entering SHOWMYACCOUNTS into the console.
     *
     * @param customer current customer
     * @return A string of the customers accounts.
     */
    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    /**
     * Generates a new account.
     *
     * @param accountType Name of the account
     * @param balance     initial balance
     * @return The new account
     */
    private Account newAccount(String accountType, double balance) throws Exception {
        if (nextAvailableAccountNumber > MAXIMUM_ACCOUNT_NUMBER) {
            throw new Exception("No available account numbers");
        }

        return new Account(accountType, balance, nextAvailableAccountNumber++);
    }

    /**
     * Resets the users password.
     *
     * @param customer     Name of the account
     * @param newPassword1 New password
     * @param newPassword2 New password to check matches first
     * @return A string to print to the user
     */
    private String resetPassword(CustomerID customer, String newPassword1, String newPassword2) {
        if (newPassword1.equals(newPassword2)) {
            customers.get(customer.getKey()).setPassword(newPassword1);
            return "Password changed";
        } else {
            return "New Password not match.";
        }
    }

    /**
     * Adds a new account to the user
     *
     * @param customer    Name of the account
     * @param accountName Name of the new account
     * @return A string to print to the user
     */
    private String addAccount(CustomerID customer, String accountName) {
        for (Account acc : customers.get(customer.getKey()).getAccounts()) {
            if (acc.getAccountName().equals(accountName)) {
                return "Account already exists.";
            }
        }
        Account newAccount;
        try {
            newAccount = newAccount(accountName, 0.0);
        } catch (Exception e) {
            return e.getMessage();
        }
        customers.get(customer.getKey()).addAccount(newAccount);
        return "New Account " + accountName + " added.";
    }

    /**
     * Moves money from one account to the other
     *
     * @param customer Name of the account
     * @param amount   The Amount to transfer
     * @param from     The account to transfer FROM
     * @param to       The account to transfer TO
     * @return A string to print to the user
     */
    private String move(CustomerID customer, double amount, String from, String to) {
        Account fromAccount = customers.get(customer.getKey()).getCustomerAccountByName(from);
        Account toAccount = customers.get(customer.getKey()).getCustomerAccountByName(to);
        if (toAccount == null || fromAccount == null) {
            return "Fail";
        }
        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
            toAccount.addTransaction(to, from, amount);
            fromAccount.addTransaction(to, from, amount);
            return "Success";
        }
        return "Fail";
    }


    /**
     * Shows the transactions to and from the input account name
     *
     * @param customer    Name of the account
     * @param accountName The name of the account to search
     * @return A list of all transactions
     */
    private String showAccount(CustomerID customer, String accountName) {
        StringBuilder stringOut = new StringBuilder();
        Account currentAccount = customers.get(customer.getKey()).getCustomerAccountByName(accountName);
        stringOut.append("Account name | ").append(currentAccount.getAccountName()).append("\n");
        stringOut.append("Account number | ").append(currentAccount.getAccountNumber()).append("\n");
        stringOut.append("Account total | ").append(currentAccount.getOpeningBalance()).append("\n");
        stringOut.append(currentAccount.getRecentTransactionsAsString());
        return stringOut.toString();
    }

    /**
     * Shows the transactions to and from all of the users accounts in time order
     *
     * @param customer Name of the account
     * @return A list of all transactions
     */
    private String showTransactions(CustomerID customer) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        StringBuilder stringOut = new StringBuilder();
        for (Account a : customers.get(customer.getKey()).getAccounts()) {
            transactions.addAll(a.getTransactions());
        }
        Collections.sort(transactions);
        for (Transaction trans : transactions) {
            stringOut.append(trans.toString());
            stringOut.append("\n");
        }
        return stringOut.toString();
    }


    private String help() {
        StringBuilder helpString = new StringBuilder();
        helpString.append("Type in one of the following commands\n");
        helpString.append("SHOWMYACCOUNTS = To return a list of your accounts" + "\n");
        helpString.append("RESETPASSWORD <new password> <new password> = To reset your password. Password must be entered twice to check they match" + "\n");
        helpString.append("ADDACCOUNT <account name> = To create a new account" + "\n");
        helpString.append("MOVE <Amount> <From> <To> = To move an amount of money from one account to another" + "\n");
        helpString.append("SHOWACCOUNT <Account Name> = To return the details and transactions to and from an account" + "\n");
        helpString.append("SHOWTRANSACTIONS = To return a list of all your transactions to and from all of your accounts" + "\n");
        return helpString.toString();
    }

    private String testJSON() {
        Database data = new Database("User.json");


        Map<String, Object> inputTest = new HashMap<>();
        inputTest.put("name", "John Deo");
        inputTest.put("email", "john.doe@example.com");
        inputTest.put("roles", new String[]{"Member", "Admin"});
        inputTest.put("admin", true);

        data.writeMapToFile(inputTest);
        return data.readFromFile().toString();
    }

}
