package newbank.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Database
 */
public class Database {

    private StringBuilder filePath = new StringBuilder();
    private Writer writer;
    private Reader reader;
    private Gson data;

    /**
     * Instantiates a new Database.
     *
     * @param fileName Name of the JSON file to save into the Data folder
     */
    public Database(String fileName) {
        this.filePath.append("Data/");
        this.filePath.append(fileName);
        this.data = new Gson();
        try {
            writer = new FileWriter(filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Re-Instantiates a new Database.
     *
     * @param fileName Name of the JSON file to save into the Data folder
     * @param read     add true to read an old datebase
     */
    public Database(String fileName, Boolean read) {
        this.filePath.append("Data/");
        this.filePath.append(fileName);
        this.data = new Gson();
        try {
            reader = new FileReader(filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the customer class to a JSON file
     *
     * @param customer customer class to write to JSON
     */
    public void writeUser(Customer customer) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> dtos = gson.fromJson(fr, jsontype);
        fr.close();

        // If it was an empty one create initial list
        if (null == dtos) {
            dtos = new ArrayList<>();
        }

        // Add new item to the list
        HashMap<String, Customer> formated = new HashMap<String, Customer>();
        formated.put(customer.getCustomerID().getKey(), customer);
        dtos.add(formated);

        // No append replace the whole file
        FileWriter fw = new FileWriter(filePath.toString());
        gson.toJson(dtos, fw);
        fw.close();
    }

    /**
     * Reads user from a JSON file
     *
     * @param customer customer ID to read from JSON
     * @return Customer
     */
    public Customer readUser(CustomerID customer) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        for (HashMap<String, Customer> user : users) {
            if (user.containsValue(customer.getKey()) || user.containsKey(customer.getKey())) {
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        for (HashMap<String, Customer> user : users) {
            if (user.containsValue(userName) || user.containsKey(userName)) {
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        List<HashMap<String, Customer>> newUserList = users.stream()
                .collect(Collectors.toList());
        Collections.copy(newUserList, users);
        int i = 0;
        for (HashMap<String, Customer> user : users) {
            if (user.containsValue(customerID.getKey()) || user.containsKey(customerID.getKey())) {
                newUserList.remove(i);
            }
            i++;
        }


        FileWriter fw = new FileWriter(filePath.toString());
        gson.toJson(newUserList, fw);
        fw.close();
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>() {
        }.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

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
    public void writeLoan(Map<String, Object> loanInput) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
        FileReader fr = new FileReader(filePath.toString());
        ArrayList<Map<String, Object>> dtos = gson.fromJson(fr, jsontype);
        fr.close();

        // If it was an empty one create initial list
        if (null == dtos) {
            dtos = new ArrayList<>();
        }

        // Add new item to the list
        dtos.add(loanInput);

        // No append replace the whole file
        FileWriter fw = new FileWriter(filePath.toString());
        gson.toJson(dtos, fw);
        fw.close();
    }

    /**
     * Writes the loan to a JSON file
     *
     * @param loanInput customer class offering loan
     */
    public void writeLoan(LoanMarketplace loanInput) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
        FileReader fr = new FileReader(filePath.toString());
        ArrayList<Map<String, Object>> dtos = gson.fromJson(fr, jsontype);
        fr.close();

        // If it was an empty one create initial list
        if (null == dtos) {
            dtos = new ArrayList<>();
        }

        // Add new item to the list
        Map<String, Object> data = new HashMap<>();
        data.put("Customer", loanInput.getCustomer());
        data.put("Loan Amount", loanInput.getLoanAmount());
        data.put("Term", loanInput.getTerm());
        data.put("APR", loanInput.getAPR());
        data.put("Loan Matched", loanInput.getLoanMatched());
        dtos.add(data);

        // No append replace the whole file
        FileWriter fw = new FileWriter(filePath.toString());
        gson.toJson(dtos, fw);
        fw.close();
    }
}

