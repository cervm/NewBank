package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The type New bank client handler.
 */
public class NewBankClientHandler extends Thread {

    private final NewBank bank;
    private final BufferedReader in;
    private final PrintWriter out;


    /**
     * Instantiates a new New bank client handler.
     *
     * @param s the s
     * @throws IOException the io exception
     */
    public NewBankClientHandler(Socket s) throws IOException {
        bank = NewBank.getBank();
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    public void run() {
        // keep getting requests from the client and processing them
        try {
            CustomerID customer;
            while (true) {
                // ask for user name
                out.println("Enter Username");
                String userName = in.readLine();
                // ask for password
                out.println("Enter Password");
                String password = in.readLine();
                out.println("Checking Details...");
                // authenticate user and get customer ID token from bank for use in subsequent requests
                customer = bank.checkLogInDetails(userName, password);
                if (customer != null) {
                    break;
                }
                out.println("Log In Failed. Please try again:");
            }
            // if the user is authenticated then get requests from the user and process them
            out.println("Log In Successful. What do you want to do?");
            while (true) {
                String request = in.readLine();
                out.println("Request from " + customer.getKey());
                String response = bank.processRequest(customer, request);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}

