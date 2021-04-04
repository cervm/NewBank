package newbank.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * The type Database
 */
public class Database {

    private StringBuilder filePath = new StringBuilder();
    private Writer writer;
    private JsonReader reader;
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
     * Writes a Map object to JSON
     *
     * @param input Map object to add to the JSON file
     */
    public void writeMapToFile(Map input) {
        try {
            writer = new FileWriter(filePath.toString(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Gson().toJson(input, writer);

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read from JSON
     *
     * @return data
     */
    public Map readFromFile() {
        try {
            reader = new JsonReader(new FileReader(filePath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map data = new Gson().fromJson(reader, Map.class);

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
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
}

