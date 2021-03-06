package newbank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The type Example client.
 */
public class ExampleClient extends Thread {

    private final Socket server;
    private final PrintWriter bankServerOut;
    private final BufferedReader userInput;
    private final Thread bankServerResponseThread;

    /**
     * Instantiates a new Example client.
     *
     * @param ip   the ip
     * @param port the port
     * @throws UnknownHostException the unknown host exception
     * @throws IOException          the io exception
     */
    public ExampleClient(String ip, int port) throws UnknownHostException, IOException {
        server = new Socket(ip, port);
        userInput = new BufferedReader(new InputStreamReader(System.in));
        bankServerOut = new PrintWriter(server.getOutputStream(), true);

        bankServerResponseThread = new Thread() {
            private final BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

            public void run() {
                try {
                    while (true) {
                        String response = bankServerIn.readLine();
                        if (response == null) {
                            break;
                        }
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        bankServerResponseThread.start();
    }


    public void run() {
        while (true) {
            try {
                while (true) {
                    String command = userInput.readLine();
                    bankServerOut.println(command);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws UnknownHostException the unknown host exception
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args)
            throws UnknownHostException, IOException, InterruptedException {
        new ExampleClient("localhost", 14002).start();
    }
}
