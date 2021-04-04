package newbank.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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
     * Instantiates a new Database.
     *
     * @param fileName Name of the JSON file to save into the Data folder
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

    public void writeUser(Customer customer) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>(){}.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> dtos = gson.fromJson(fr, jsontype);
        fr.close();

        // If it was an empty one create initial list
        if(null==dtos) {
            dtos = new ArrayList<>();
        }

        // Add new item to the list
        HashMap<String, Customer> formated = new HashMap<String, Customer>();
        formated.put(customer.getCustomerID().getKey(), customer);
        dtos.add(formated);

        // No append replace the whole file
        FileWriter fw  = new FileWriter(filePath.toString());
        gson.toJson(dtos, fw);
        fw.close();
    }

    public Customer readUser(CustomerID customer) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>(){}.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        for (HashMap<String, Customer> user : users) {
            if(user.containsValue(customer.getKey()) || user.containsKey(customer.getKey())){
                Collection<Customer> userAsCustomer =  user.values();
                for (Customer property : userAsCustomer){
                    return property;
                }
            }
        }
        return null;
    }

    public Customer readUser(String userName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>(){}.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        for (HashMap<String, Customer> user : users) {
            if(user.containsValue(userName) || user.containsKey(userName)){
                Collection<Customer> userAsCustomer =  user.values();
                for (Customer property : userAsCustomer){
                    return property;
                }
            }
        }
        return null;
    }

    public void removeUser(CustomerID customerID) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>(){}.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        List<HashMap<String, Customer>> newUserList = users.stream()
                .collect(Collectors.toList());
        Collections.copy(newUserList, users);
        int i = 0;
        for (HashMap<String, Customer> user : users) {
            if(user.containsValue(customerID.getKey()) || user.containsKey(customerID.getKey())){
                newUserList.remove(i);
            }
            i++;
        }


        FileWriter fw  = new FileWriter(filePath.toString());
        gson.toJson(newUserList, fw);
        fw.close();
    }

    public void overwriteCustomer(Customer customer) throws IOException {
        removeUser(customer.getCustomerID());
        writeUser(customer);

    }

    public Customer customerByAccNum(int accountNumber) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type jsontype = new TypeToken<List<HashMap<String, Customer>>>(){}.getType();
        FileReader fr = new FileReader(filePath.toString());
        List<HashMap<String, Customer>> users = gson.fromJson(fr, jsontype);
        fr.close();

        for (HashMap<String, Customer> user : users) {
                Collection<Customer> userAsCustomer =  user.values();
                for (Customer property : userAsCustomer){
                    for (Account account : property.getAccounts()) {
                        if (account.getAccountNumber() == accountNumber) {
                            return property;
                        }
                    }
                }
        }
        return null;
    }
}

