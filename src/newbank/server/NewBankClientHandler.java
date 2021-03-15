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

    private NewBank bank;
    private static BufferedReader in;
    private static PrintWriter out;


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
            // ask for user name
            out.println("Enter Username");
            String userName = in.readLine();
            // ask for password
            out.println("Enter Password");
            String password = in.readLine();
            out.println("Checking Details...");
            // authenticate user and get customer ID token from bank for use in subsequent requests
            CustomerID customer = bank.checkLogInDetails(userName, password);
            // if the user is authenticated then get requests from the user and process them
            if (customer != null) {
                out.println("Log In Successful. What do you want to do?");
                while (true) {
                    String request = in.readLine();
                    System.out.println("Request from " + customer.getKey());
                    String responce = bank.processRequest(customer, request);
                    out.println(responce);
                }
            } else {
                out.println("Log In Failed");
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

    public static String transfer(String request) {
        try {
            out.println("Which account would you like to transfer from?");
            String fromAccount = in.readLine();
            out.println("Which account would you like to transfer to?");
            String toAccount = in.readLine();
            out.println("How much would you like to transfer?");
            String moveAmount = in.readLine();
            double d = Double.parseDouble(moveAmount);
            out.println(d);
            Double acc = Account.getOpeningBalance();
            if (d> 0 && d <= acc) {
                return "success!";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    }

