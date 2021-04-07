package newbank.server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final int MAXIMUM_ACCOUNT_NUMBER = 99999999;
    private Database users = new Database("Users.json");
    private Customer currentUser;

    private NewBank() throws Exception {
        //TODO: remove traces of the customers in here and hava single customer
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

    //TODO: not needed any more
    /**
     * Adds the testing data to the customer HashMap
     */
    private void addTestData() throws Exception {
        Customer bhagy = new Customer("Bhagy", "bhagy");
        bhagy.addAccount(newAccount("Main", 1000.0));
        bhagy.addAccount(newAccount("Savings", 1000.0));
        bhagy.addAccountInfo("100 Test Road, W1 4HJ", "+4471234 663300", "Bhagyashree Patil", "What was your first pet's name?");
        customers.put(bhagy.getUserName(), bhagy);
        users.writeUser(bhagy);


        Customer christina = new Customer("Christina", "christina");
        christina.addAccount(newAccount("Savings", 1500.0));
        customers.put(christina.getUserName(), christina);
        users.writeUser(christina);

        Customer john = new Customer("John", "john");
        john.addAccount(newAccount("Checking", 250.0));
        john.addAccount(newAccount("Savings", 10000));
        john.getAccount("Checking").addTransaction("Checking", "Test", 100);
        john.getAccount("Checking").addTransaction("Savings", "Test", 1000);
        customers.put(john.getUserName(), john);
        users.writeUser(john);
        //users.writeMapToFile(customers);
    }

    /**
     * Check log in details customer id.
     *
     * @param userName the user name
     * @param password the password
     * @return the customer id
     */
    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        try {
            Customer cUser = users.readUser(userName);
            if (cUser.getUserName().equals(userName)) {
                Customer c = customers.get(userName);
                if (cUser.getPassword().equals(password)) {
                    currentUser = cUser;
                    return cUser.getCustomerID();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    return showMyAccounts();
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
                case "TRANSFER":
                    if (tokens.length != 4) {
                        break;
                    }
                    double amountToTransfer;
                    int accountNumber;
                    try {
                        amountToTransfer = Double.parseDouble(tokens[1]);
                        accountNumber = Integer.parseInt(tokens[3]);
                    } catch (NumberFormatException e) {
                        break;
                    }
                    return transfer(amountToTransfer, tokens[2], accountNumber);
                case "PAY":
                    if (tokens.length != 3) {
                        break;
                    }
                    double amountToPay;
                    try {
                        amountToPay = Double.parseDouble(tokens[2]);
                    } catch (NumberFormatException e) {
                        break;
                    }
                    return pay(customer, tokens[1], amountToPay);
                case "SHOWACCOUNT":
                    if (tokens.length < 2) {
                        break;
                    }
                    return showAccount(customer, tokens[1]);
                case "SHOWTRANSACTIONS":
                    return showTransactions(customer);
                case "REQUESTLOAN":
                    if (tokens.length < 4){
                        break;
                    }
                    return requestLoan(customer, tokens[1], tokens[2], tokens[3]);
                case "HELP":
                    return help();
                case "SHOWACCOUNTINFO":
                    return accountInfo(customer);
                case "EDITADDRESS":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editAddress(customer, tokens[1], tokens[2]);
                case "EDITPHONENUMBER":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editPhoneNumber(customer, tokens[1], tokens[2]);
                case "EDITFULLNAME":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editFullName(customer, tokens[1], tokens[2]);
                case "EDITSECURITYQUESTION":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editSecurityQuestion(customer, tokens[1], tokens[2]);

                default:
                    break;
            }
        }
        return "FAIL";
    }

    private String requestLoan(CustomerID customer, String loanAmount, String APR, String term)  {
        StringBuilder output = new StringBuilder();
        try{
            //TODO:Create loan market place outside of function
            double loanAmt = Double.parseDouble(loanAmount);
            LoanMarketplace loanMarketplace = new LoanMarketplace(currentUser, loanAmt, APR, term);
            //TODO: METHOD IS NOW CALLED TWICE LOOK AT REMOVING THE OTHER ONE
            loanMarketplace.checkLoanMeetsCriteria();
            output.append("\nLoan Submitted to Marketplace");
        } catch (Exception e) {
            output.append(e.getMessage());
            e.printStackTrace();
        }

        return output.toString();
    }

    //FR1.2

    /**
     * Returns an authenticated users accounts upon them entering SHOWMYACCOUNTS into the console.
     *
     * @return A string of the customers accounts.
     */
    private String showMyAccounts() {
        return currentUser.accountsToString();
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
            currentUser.setPassword(newPassword1);
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        for (Account acc : currentUser.getAccounts()) {
            if (acc.getAccountName().equals(accountName)) {
                return "Account already exists.";
            }
        }
        Account newAccount;
        try {
            newAccount = newAccount(accountName, 0.0);
            currentUser.addAccount(newAccount);
            users.overwriteCustomer(currentUser);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "New Account " + accountName + " added.";
    }

    /**
     * Moves money between the customer's accounts from one account to the other
     *
     * @param customer Name of the account
     * @param amount   The Amount to transfer
     * @param from     The account to transfer FROM
     * @param to       The account to transfer TO
     * @return A string to print to the user
     */
    private String move(CustomerID customer, double amount, String from, String to) {
        Account fromAccount = currentUser.getAccount(from);
        Account toAccount = currentUser.getAccount(to);
        if (transfer(amount, fromAccount, toAccount)) {
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        }
        return "FAIL";
    }

    /**
     * Transfers an amount of money from the selected customer's account to any account in the bank
     *
     * @param amount   The Amount to transfer
     * @param from     The account name to transfer FROM
     * @param to       The account number to transfer TO
     * @return A string to print to the user
     */
    private String transfer(double amount, String from, int to) {
        try {
            Customer customerTo = users.customerByAccNum(to);

            Account fromAccount = currentUser.getAccount(from);
            Account toAccount = customerTo.getAccount(to);
            if (transfer(amount, fromAccount, toAccount)) {
                try {
                    users.overwriteCustomer(currentUser);
                    users.overwriteCustomer(customerTo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "SUCCESS";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAIL";
    }

    /**
     * Transfers an amount of money from one account to the other
     *
     * @param amount      The amount to transfer
     * @param fromAccount The account to transfer FROM
     * @param toAccount   The account to transfer TO
     * @return {@code true} if the transfer has been successful, {@code false} otherwise
     */
    private boolean transfer(double amount, Account fromAccount, Account toAccount) {
        if (toAccount == null || fromAccount == null) {
            return false;
        }
        try {
            fromAccount.withdraw(amount);
        } catch (Exception e) {
            return false;
        }
        try {
            toAccount.deposit(amount);
        } catch (Exception e) {
            // Revert the withdrawal
            fromAccount.deposit(amount);
            return false;
        }
        String from = Integer.toString(fromAccount.getAccountNumber());
        String to = Integer.toString(toAccount.getAccountNumber());
        toAccount.addTransaction(to, from, amount);
        fromAccount.addTransaction(to, from, amount);
        return true;
    }

    /**
     * Sends an amount of money to another person
     *
     * @param customer Name of the account
     * @param userName The beneficiary user name
     * @param amount   The amount to pay
     * @return A string to print to the user
     */
    private String pay(CustomerID customer, String userName, double amount) {
        Account fromAccount = getCustomer(customer).getAccount();
        Customer beneficiary = getCustomer(userName);
        if (beneficiary == null) {
            return "FAIL";
        }
        Account toAccount = beneficiary.getAccount();
        if (transfer(amount, fromAccount, toAccount)) {
            return "SUCCESS";
        }
        return "FAIL";
    }

    /**
     * Returns the customer based on the given ID
     *
     * @param customer The ID of the customer
     * @return The customer matching the ID
     */
    private Customer getCustomer(CustomerID customer) {
        return customers.get(customer.getKey());
    }

    /**
     * Returns the customer based on the user name
     *
     * @param userName The user name of the customer
     * @return The customer matching the user name
     */
    private Customer getCustomer(String userName) {
        return customers.get(userName);
    }

    /**
     * Returns the account based on the given number
     *
     * @param accountNumber The account number
     * @return The account matching the number
     */
    //private Account getAccountByNumber(int accountNumber) {
        //return currentUser.getAccount(accountNumber);
    //}

    /**
     * Shows the transactions to and from the input account name
     *
     * @param customer    Name of the account
     * @param accountName The name of the account to search
     * @return A list of all transactions
     */
    private String showAccount(CustomerID customer, String accountName) {
        StringBuilder stringOut = new StringBuilder();
        Account currentAccount = getCustomer(customer).getAccount(accountName);
        stringOut.append("Account name | ").append(currentAccount.getAccountName()).append("\n");
        stringOut.append("Account number | ").append(currentAccount.getAccountNumber()).append("\n");
        stringOut.append("Account total | ").append(currentAccount.getBalance()).append("\n");
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
        for (Account a : getCustomer(customer).getAccounts()) {
            transactions.addAll(a.getTransactions());
        }
        Collections.sort(transactions);
        for (Transaction trans : transactions) {
            stringOut.append(trans.toString());
            stringOut.append("\n");
        }
        return stringOut.toString();
    }

    //TODO: Please add posible inputs accepted into here
    private String help() {
        return """
                Type in one of the following commands
                SHOWMYACCOUNTS = To return a list of your accounts
                RESETPASSWORD <new password> <new password> = To reset your password. Password must be entered twice to check they match
                ADDACCOUNT <account name> = To create a new account
                MOVE <Amount> <From> <To> = To move an amount of money from one account to another
                TRANSFER <Amount> <From> <To> = To transfer an amount of money from the selected customer's account to any account in the bank
                PAY <Person/Company> <Amount> = To pay an amount of money to another person or company
                SHOWACCOUNT <Account Name> = To return the details and transactions to and from an account
                SHOWTRANSACTIONS = To return a list of all your transactions to and from all of your accounts
                SHOWACCOUNTINFO = To return a list of your personal details 
                EDITADDRESS <password> <new address> = To update your address
                EDITPHONENUMBER <password> <new phone number> = To update your phone number
                EDITFULLNAME <password> <new full name> = To update your Full Name
                EDITSECURITYQUESTION <password> <new security question> = To update your security question      
                """;
    }

    /**
     * Shows the users personal details
     *
     * @param customer    Name of the account
     * @return Customers personal details
     */
    private String accountInfo(CustomerID customer) {
        StringBuilder stringOut = new StringBuilder();
        stringOut.append(getCustomer(customer).getAccountInfo());
        return stringOut.toString();
    }
    /**
     * Edits the users address.
     *
     * @param customer     Name of the account
     * @param password Customers password
     * @param newAddress New address entry
     * @return A string to print to the user
     */
    private String editAddress(CustomerID customer, String password, String newAddress) {
        if (password.equals(getCustomer(customer).getPassword())) {
            getCustomer(customer).setAddress(newAddress);
            return "Address updated";
        } else {
            return "Password does not match.";
        }
    }
    private String editPhoneNumber(CustomerID customer, String password, String newPhoneNumber) {
        if (password.equals(getCustomer(customer).getPassword())) {
            getCustomer(customer).setPhoneNumber(newPhoneNumber);
            return "Phone Number updated";
        } else {
            return "Password does not match.";
        }
    }
    private String editFullName(CustomerID customer, String password, String newFullName) {
        if (password.equals(getCustomer(customer).getPassword())) {
            getCustomer(customer).setPhoneNumber(newFullName);
            return "Full Name updated";
        } else {
            return "Password does not match.";
        }
    }
    private String editSecurityQuestion(CustomerID customer, String password, String newSecurityQuestion) {
        if (password.equals(getCustomer(customer).getPassword())) {
            getCustomer(customer).setPhoneNumber(newSecurityQuestion);
            return "Full Name updated";
        } else {
            return "Password does not match.";
        }
    }
}
