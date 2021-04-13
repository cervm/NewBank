package newbank.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * The type Database
 */
public class Database {

    private final StringBuilder filePath = new StringBuilder();

    /**
     * Instantiates a new Database.
     *
     * @param fileName Name of the JSON file to save into the Data folder
     * @param read     add true to read an old database
     */
    public Database(String fileName, Boolean read) {
        this.filePath.append("Data/");
        this.filePath.append(fileName);
    }

    /**
     * Writes the customer class to a JSON file
     *
     * @param customer customer class to write to JSON
     */
    public void writeUser(Customer customer) throws IOException {
        List<HashMap<String, Customer>> users = readFromJson(new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType());

        // If it was an empty one create initial list
        if (null == users) {
            users = new ArrayList<>();
        }

        // Add new item to the list
        HashMap<String, Customer> formatted = new HashMap<>();
        formatted.put(customer.getCustomerID().getKey(), customer);
        users.add(formatted);

        // No append replace the whole file
        writeToJson(users);
    }

    /**
     * Reads user from a JSON file
     *
     * @param customer customer ID to read from JSON
     * @return Customer
     */
    public Customer readUser(CustomerID customer) throws IOException {
        List<HashMap<String, Customer>> users = readFromJson(new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType());

        for (HashMap<String, Customer> user : users) {
            if (user.containsKey(customer.getKey())) {
                Collection<Customer> userAsCustomer = user.values();
                for (Customer property : userAsCustomer) {
                    return property;
                }
            }
        }
        return null;
    }

    /**
     * Reads user from a JSON file
     *
     * @param userName user name to read from JSON
     * @return Customer
     */
    public Customer readUser(String userName) throws IOException {
        List<HashMap<String, Customer>> users = readFromJson(new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType());

        for (HashMap<String, Customer> user : users) {
            if (user.containsKey(userName)) {
                Collection<Customer> userAsCustomer = user.values();
                for (Customer property : userAsCustomer) {
                    return property;
                }
            }
        }
        return null;
    }

    /**
     * Removes a user from JSON
     *
     * @param customerID customer ID to read from JSON
     */
    public void removeUser(CustomerID customerID) throws IOException {
        List<HashMap<String, Customer>> users = readFromJson(new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType());

        List<HashMap<String, Customer>> newUserList = new ArrayList<>(users);
        Collections.copy(newUserList, users);
        int i = 0;
        for (HashMap<String, Customer> user : users) {
            if (user.containsKey(customerID.getKey())) {
                newUserList.remove(i);
            }
            i++;
        }

        writeToJson(newUserList);
    }

    /**
     * Overwrites a customers data in the JSON file
     *
     * @param customer customer to replace
     */
    public void overwriteCustomer(Customer customer) throws IOException {
        removeUser(customer.getCustomerID());
        writeUser(customer);

    }

    /**
     * Finds the customer an account belongs to
     *
     * @param accountNumber Account number to search for
     * @return Customer
     */
    public Customer customerByAccNum(int accountNumber) throws IOException {
        List<HashMap<String, Customer>> users = readFromJson(new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType());

        for (HashMap<String, Customer> user : users) {
            Collection<Customer> userAsCustomer = user.values();
            for (Customer property : userAsCustomer) {
                for (Account account : property.getAccounts()) {
                    if (account.getAccountNumber() == accountNumber) {
                        return property;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Writes the loan to a JSON file
     *
     * @param loanInput customer class offering loan
     */
    public void writeLoan(LoanMarketplace loanInput) throws IOException {
        ArrayList<Map<String, Object>> loans = readFromJson(new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        // If it was an empty one create initial list
        if (null == loans) {
            loans = new ArrayList<>();
        }

        // Add new item to the list
        Map<String, Object> data = new HashMap<>();
        data.put("Loan ID", loanInput.getLoanID());
        data.put("Customer", loanInput.getCustomer());
        data.put("Loan Amount", loanInput.getLoanAmount());
        data.put("Term", loanInput.getTerm());
        data.put("APR", loanInput.getAPR());
        data.put("Loan Matched", loanInput.getLoanMatched());
        loans.add(data);

        // No append replace the whole file
        writeToJson(loans);
    }

    /**
     * Reads loans from a JSON file
     *
     * @return LoanMarketplace
     */
    public ArrayList<LoanMarketplace> readLoans() throws IOException {
        ArrayList<Map<String, Object>> loans = readFromJson(new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        ArrayList<LoanMarketplace> loanOutput = new ArrayList<>();
        for (Map<String, Object> loan : loans) {
            Map<String, Object> customerObj = (Map<String, Object>) loan.get("Customer");
            Database users = new Database("users.json", true);
            Customer customer = users.readUser(customerObj.get("userName").toString());
            Double loanAmount = (Double) loan.get("Loan Amount");
            String apr = (String) loan.get("APR");
            String term = (String) loan.get("Term");
            Double loanID = (Double) loan.get("Loan ID");
            loanOutput.add(new LoanMarketplace(customer, loanAmount, apr, term, loanID));
        }
        return loanOutput;
    }

    /**
     * Deletes a loan from the loan marketplace
     *
     * @param loanID Account number to search for
     */
    public void deleteLoan(double loanID) throws IOException {
        ArrayList<Map<String, Object>> loans = readFromJson(new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        // If it was an empty one create initial list
        if (null == loans) {
            loans = new ArrayList<>();
        }

        int i = 0;
        // Add new item to the list
        for (Map<String, Object> loan : loans) {
            if (loan.get("Loan ID").equals(loanID)) {
                loans.remove(i);
                break;
            }
            i++;
        }

        // No append replace the whole file
        writeToJson(loans);
    }

    /**
     * Writes a loan to confirmed loans
     *
     * @param confirmedLoan the confirmed loan as type loan
     */
    public void writeConfirmedLoan(Loan confirmedLoan) throws IOException {
        ArrayList<Loan> loans = readFromJson(new TypeToken<ArrayList<Loan>>() {
        }.getType());

        // If it was an empty one create initial list
        if (null == loans) {
            loans = new ArrayList<>();
        }

        // Add new item to the list
        loans.add(confirmedLoan);

        // No append replace the whole file
        writeToJson(loans);
    }

    /**
     * Moves a loan from loan marketplace to confirmed loans
     *
     * @param loanID         loanID picked by the user
     * @param fromCustomerID the CustomerID of the person submitting request
     * @return Loan
     */
    public Loan moveLoanToConfirmed(Double loanID, CustomerID fromCustomerID) throws IOException {
        Loan confirmedLoan = null;
        Database loanMarketplace = new Database("loans.json", true);
        Database confirmedLoans = new Database("confirmedLoans.json", true);

        ArrayList<Map<String, Object>> loans = readFromJson(new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType());

        for (Map<String, Object> loan : loans) {
            if (loan.get("Loan ID").equals(loanID)) {
                Map<String, Object> customerObj = (Map<String, Object>) loan.get("Customer");
                Database users = new Database("users.json", true);
                Customer customer = users.readUser(customerObj.get("userName").toString());
                CustomerID customerID = customer.getCustomerID();
                Double loanAmount = (Double) loan.get("Loan Amount");
                String apr = (String) loan.get("APR");
                String term = (String) loan.get("Term");

                loanMarketplace.deleteLoan(loanID);
                confirmedLoan = new Loan(customerID, fromCustomerID, loanAmount, apr, term);
                confirmedLoans.writeConfirmedLoan(confirmedLoan);
            }
        }
        return confirmedLoan;
    }

    /**
     * @param typeOfT type of T
     * @param <T>     T to return
     * @return result
     * @throws IOException on file not found or JSON read exception
     */
    private <T> T readFromJson(Type typeOfT) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = new FileReader(filePath.toString());
        T result = gson.fromJson(reader, typeOfT);
        reader.close();
        return result;
    }

    /**
     * @param data data to write
     * @throws IOException on file not found or JSON write exception
     */
    private void writeToJson(Object data) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(filePath.toString());
        gson.toJson(data, writer);
        writer.close();
    }
}
