package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.Console;
import java.util.Arrays;

/**
 * The type New bank client handler.
 */
public class NewBankClientHandler extends Thread {

    private final NewBank bank;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Console console;


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
        console = System.console();
        if (console == null){
            out.println("Try using your computers built in terminal for increased security");
        }
    }

    public void run() {
        // keep getting requests from the client and processing them
        out.println("1. Existing user. 2. New user");
        try{
            String ans = in.readLine();

            if(ans.equals("1")){
            try {
                CustomerID customer;
                while (true) {
                    // ask for user name
                    out.println("Enter Username");
                    String userName = in.readLine();
                    // ask for password
                    String password;
                    try{
                        char [] pass = console.readPassword("Enter Password");
                        password = Arrays.toString(pass);
                    } catch(Exception e){
                        out.println("Enter Password");
                        password = in.readLine();
                    }
                    out.println("Checking Details...");
                    // authenticate user and get customer ID token from bank for use in subsequent requests
                    customer = bank.checkLogInDetails(userName, password);
                    if (customer != null) {
                        break;
                    }
                    out.println("Log In Failed. Please try again:");
                }
                // if the user is authenticated then get requests from the user and process them
                out.println("Log In Successful. What do you want to do?\nType \"HELP\" to " +
                        "discover what you can do");
                while (true) {
                    String request = in.readLine();
                    System.out.println("Request from " + customer.getKey());
                    String response = bank.processRequest(customer, request);
                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
             }
            }
            if(ans.equals("2")){
                out.println("Set up your Username:");
                String userName = in.readLine();
                String passWord;
                while(true){
                    String passWord1;
                    String passWord2;
                    try{
                        char[] pass = console.readPassword("Set up your password");
                        passWord1 = Arrays.toString(pass);
                    } catch(Exception e) {
                        out.println("Set up your password:");
                        passWord1 = in.readLine();
                    }

                    try{
                        char[] pass = console.readPassword("Confirm Password");
                        passWord2 = Arrays.toString(pass);
                    } catch(Exception e){
                        out.println("Confirm your password:");
                        passWord2 = in.readLine();
                    }

                  if(passWord1.equals(passWord2)){
                      passWord = passWord1;
                      break;
                  }
                  else{
                      out.println("Password not match, please try again");
                  }
                }
                out.println("Enter your address:");
                String address = in.readLine();
                out.println("Enter your email:");
                String email = in.readLine();
                bank.newCustomerSignup(userName, passWord,address, email);
                out.println("Sign up successful. Please continue");
                run();
             }
            }
           catch(Exception e){
            e.printStackTrace();
           }
            finally {
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

