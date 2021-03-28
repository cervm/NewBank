package newbank.server;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * The type Database
 */
public class Database {

    private StringBuilder filePath = new StringBuilder();
    private Writer writer;
    private JsonReader reader;

    /**
     * Instantiates a new Database.
     *
     * @param fileName Name of the JSON file to save into the Data folder
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
     * @param input Map object to add to the JSON file
     */
    public void writeMapToFile(Map input) {
        try {
            writer = new FileWriter(filePath.toString());
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
}
