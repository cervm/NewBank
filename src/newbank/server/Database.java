package newbank.server;


import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * The type Database
 */
public class Database {

    //private String filePath = "./Data/";
    private StringBuilder filePath = new StringBuilder();
    private Writer writer;

    /**
     * Instantiates a new Database.
     *
     * @param fileName    Name of the JSON file to save into the Data folder
     */
    public Database(String fileName) {
        this.filePath.append("Data/");
        this.filePath.append(fileName);
        try {
            writer = new FileWriter(filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a Map object to JSON
     *
     * @param input       Map object to add to the JSON file
     */
    public void writeMapToFile(Map input){
        new Gson().toJson(input, this.writer);
    }
}
