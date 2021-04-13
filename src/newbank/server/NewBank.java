package newbank.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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


    private int nextAvailableAccountNumber = 10000000;
    private static final int MAXIMUM_ACCOUNT_NUMBER = 99999999;
    private Database users = new Database("users.json", true);
    private Database loanMarketplace = new Database("loans.json", true);
    private Database confirmedLoans = new Database("confirmedLoans.json", true);
    private Customer currentUser;

    private NewBank() throws Exception {
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
     * New user sign up
     */
    public String newCustomerSignup(String userName, String password, String address, String email) throws Exception {
        StringBuilder output = new StringBuilder();

        if (passwordComplexity(password).equals("Success")) {
            Customer newCustomer = new Customer(userName, password);
            newCustomer.addAccount(newAccount("Current Account", 0.0));
            users.writeUser(newCustomer);
            output.append("Welcome to New Bank" + newCustomer.getUserName());
        } else {
            output.append(printPasswordComplexityRules());
        }

        return output.toString();

    }

    /**
     * Checks the password complexity meets the required standard
     *
     * @param password
     * @return String - Success if meets requirements or the rule it fails on
     */
    public String passwordComplexity(String password) {
        StringBuilder output = new StringBuilder();
        boolean hasNumber = false;
        boolean hasLetter = false;
        boolean isGreater = false;

        for (char character : password.toCharArray()) {
            if (Character.isAlphabetic(character)) {
                hasLetter = true;
            }
            if (Character.isDigit(character)) {
                hasNumber = true;
            }
        }

        if (password.length() > 8) {
            isGreater = true;
        }

        if (!hasNumber) {
            output.append("Fail: Please enter a password with at least 1 number\n");
        } else if (!hasLetter) {
            output.append("Fail: Please enter a password with at least 1 letter\n");
        } else if (!isGreater) {
            output.append("Fail: Please enter a password longer than 8 characters\n");
        } else {
            output.append("Success");
        }

        return output.toString();
    }

    /**
     * To hash the user password for database storage.
     *
     * @param password
     * @return
     */
    private String hashPassword(String password) {
        return password;
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
            //TODO: handle user not found
            Customer cUser = users.readUser(userName);
            if (cUser.getUserName().equals(userName)) {
                if (cUser.getPassword().equals(password)) {
                    currentUser = cUser;
                    return currentUser.getCustomerID();
                }
            }
            return null;
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
        String currentUID = currentUser.getCustomerID().getKey();
        String custoID = customer.getKey();
        if (currentUID.equals(custoID)) {
            String[] tokens = request.split("\\s+");
            switch (tokens[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts();
                case "RESETPASSWORD":
                    if (tokens.length < 3) {
                        break;
                    }
                    return resetPassword(tokens[1], tokens[2]);
                case "ADDACCOUNT":
                    if (tokens.length < 2) {
                        break;
                    }
                    return addAccount(tokens[1]);
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
                    return move(amount, tokens[2], tokens[3]);
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
                    return pay(tokens[1], amountToPay);
                case "SHOWACCOUNT":
                    if (tokens.length < 2) {
                        break;
                    }
                    return showAccount(tokens[1]);
                case "SHOWTRANSACTIONS":
                    return showTransactions();
                case "REQUESTLOAN":
                    if (tokens.length < 4) {
                        break;
                    }
                    return requestLoan(customer, tokens[1], tokens[2], tokens[3]);
                case "HELP":
                    return help();
                case "SHOWACCOUNTINFO":
                    return accountInfo();
                case "EDITADDRESS":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editAddress(tokens[1], tokens[2]);
                case "EDITPHONENUMBER":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editPhoneNumber(tokens[1], tokens[2]);
                case "EDITFULLNAME":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editFullName(tokens[1], tokens[2]);
                case "EDITSECURITYQUESTION":
                    if (tokens.length < 3) {
                        break;
                    }
                    return editSecurityQuestion(tokens[1], tokens[2]);
                case "LOANMARKETPLACE":
                    return printLoans();
                case "PICKLOAN":
                    if (tokens.length < 1) {
                        break;
                    }
                    return pickLoan(tokens[1]);
                default:
                    break;
            }
        }
        return "FAIL";
    }

    /**
     * Creates a loanMarket places and writes it to loans.json
     *
     * @return success or error message
     */
    private String requestLoan(CustomerID customer, String loanAmount, String APR, String term) {
        StringBuilder output = new StringBuilder();
        try {
            LoanMarketplace loan = new LoanMarketplace(currentUser, Double.parseDouble(loanAmount), APR, term);
            output.append(loan.checkLoanMeetsCriteria() + "\n");
            loanMarketplace.writeLoan(loan);
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
     * @param newPassword1 New password
     * @param newPassword2 New password to check matches first
     * @return A string to print to the user
     */
    private String resetPassword(String newPassword1, String newPassword2) {
        if (passwordComplexity(newPassword1).equals("Success")) {
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
        } else {
            return printPasswordComplexityRules();
        }
    }

    /**
     * Prints the password complexity rules
     *
     * @return A string to print to the user
     */
    private String printPasswordComplexityRules() {
        StringBuilder output = new StringBuilder();

        output.append("A password length must be greater then 8 characters\n");
        output.append("A password must contain at least 1 character\n");
        output.append("A password must contain at least 1 number\n");

        return output.toString();
    }

    /**
     * Adds a new account to the user
     *
     * @param accountName Name of the new account
     * @return A string to print to the user
     */
    private String addAccount(String accountName) {
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
     * @param amount The Amount to transfer
     * @param from   The account to transfer FROM
     * @param to     The account to transfer TO
     * @return A string to print to the user
     */
    private String move(double amount, String from, String to) {
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
     * @param amount The Amount to transfer
     * @param from   The account name to transfer FROM
     * @param to     The account number to transfer TO
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
        if (toAccount == null || fromAccount == null || toAccount.getAccountName().equals(fromAccount.getAccountName())) {
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
        try {
            users.overwriteCustomer(currentUser);
            users.overwriteCustomer(users.customerByAccNum(toAccount.getAccountNumber()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Sends an amount of money to another person
     *
     * @param userName The beneficiary user name
     * @param amount   The amount to pay
     * @return A string to print to the user
     */
    private String pay(String userName, double amount) {
        try {
            Account fromAccount = currentUser.getAccount();
            Customer beneficiary = users.readUser(userName);
            Account toAccount = beneficiary.getAccount();

            if (beneficiary == null || toAccount.getAccountName().equals(fromAccount.getAccountName()) ||
                    (!beneficiary.getUserName().equals(userName))) {
                return "FAIL";
            }

            if (transfer(amount, fromAccount, toAccount)) {
                users.overwriteCustomer(currentUser);
                users.overwriteCustomer(beneficiary);
                return "SUCCESS";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "FAIL";
    }

    /**
     * Shows the transactions to and from the input account name
     *
     * @param accountName The name of the account to search
     * @return A list of all transactions
     */
    private String showAccount(String accountName) {
        StringBuilder stringOut = new StringBuilder();
        Account currentAccount = currentUser.getAccount(accountName);
        stringOut.append("Account name | ").append(currentAccount.getAccountName()).append("\n");
        stringOut.append("Account number | ").append(currentAccount.getAccountNumber()).append("\n");
        stringOut.append("Account total | ").append(currentAccount.getBalance()).append("\n");
        stringOut.append(currentAccount.getRecentTransactionsAsString());
        return stringOut.toString();
    }

    /**
     * Shows the transactions to and from all of the users accounts in time order
     *
     * @return A list of all transactions
     */
    private String showTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        StringBuilder stringOut = new StringBuilder();
        for (Account a : currentUser.getAccounts()) {
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
                REQUESTLOAN <VALUE> <APR> <TERM> = Request a loan and have it submitted to the marketplace pending approval/funding.
                LOANMARKETPLACE = Prints out all requested loans
                PICKLOAN <LOAN ID> = Allows you to pick a loan to fulfill the request
                """;
    }

    /**
     * Shows the users personal details
     *
     * @return Customers personal details
     */
    private String accountInfo() {
        StringBuilder stringOut = new StringBuilder();
        stringOut.append(currentUser.getAccountInfo());
        return stringOut.toString();
    }

    /**
     * Edits the users address.
     *
     * @param password   Customers password
     * @param newAddress New address entry
     * @return A string to print to the user
     */
    private String editAddress(String password, String newAddress) {
        if (password.equals(currentUser.getPassword())) {
            currentUser.setAddress(newAddress);
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Address updated";
        } else {
            return "Password does not match.";
        }
    }

    /**
     * Edits the users phone number.
     *
     * @param password       Customers password
     * @param newPhoneNumber New address entry
     * @return A string to print to the user
     */
    private String editPhoneNumber(String password, String newPhoneNumber) {
        if (password.equals(currentUser.getPassword())) {
            currentUser.setPhoneNumber(newPhoneNumber);
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Phone Number updated";
        } else {
            return "Password does not match.";
        }
    }

    /**
     * Edits the users full Name.
     *
     * @param password    Customers password
     * @param newFullName New address entry
     * @return A string to print to the user
     */
    private String editFullName(String password, String newFullName) {
        if (password.equals(currentUser.getPassword())) {
            currentUser.setPhoneNumber(newFullName);
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Full Name updated";
        } else {
            return "Password does not match.";
        }
    }

    /**
     * Edits the users security question.
     *
     * @param password            Customers password
     * @param newSecurityQuestion New address entry
     * @return A string to print to the user
     */
    private String editSecurityQuestion(String password, String newSecurityQuestion) {
        if (password.equals(currentUser.getPassword())) {
            currentUser.setPhoneNumber(newSecurityQuestion);
            try {
                users.overwriteCustomer(currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Security Question updated";
        } else {
            return "Password does not match.";
        }
    }

    /**
     * Prints the loans from loan marketplace.
     *
     * @return A string to print to the user
     */
    private String printLoans() {
        StringBuilder output = new StringBuilder();
        output.append("Loan ID | Lender | Max Amount | APR | Lend Term (Months)\n");
        ArrayList<LoanMarketplace> loans = new ArrayList<LoanMarketplace>();
        try {
            loans.addAll(loanMarketplace.readLoans());
        } catch (IOException e) {
            e.printStackTrace();
            return "Fail";
        }
        for (LoanMarketplace loan : loans) {
            output.append(loan);
            output.append("\n");
        }
        return output.toString();
    }

    /**
     * Picks a loan based on the loan id and moves it to confirmed loans
     *
     * @param loanNumber the loan number picked by the user
     * @return A string to print to the user
     */
    private String pickLoan(String loanNumber) {
        Double totalBalance = currentUser.getTotalBalance();

        try {
            for (LoanMarketplace loan : loanMarketplace.readLoans()) {
                if (loan.getLoanID() == Double.parseDouble(loanNumber)) {
                    if (loan.getLoanAmount() <= totalBalance) {
                        loanMarketplace.moveLoanToConfirmed(Double.parseDouble(loanNumber), currentUser.getCustomerID());
                        transfer(loan.getLoanAmount(),
                                currentUser.getAccount(),
                                users.readUser(loan.getCustomer().getCustomerID()).getAccount());
                        return "Loan is a success";
                    } else {
                        return "Insufficient funds: Can not loan more than 50% of your total balance";
                    }
                }
            }
            return "Loan not found. Please try another number";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAIL";
    }
}
