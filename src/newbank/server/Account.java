package newbank.server;

import java.util.Collection;
import java.util.HashMap;

/**
 * The type Account.
 */
public class Account {

    private final String accountName;
    private final double openingBalance;
    private final int accountNumber;


    /**
     * Instantiates a new Account.
     *
     * @param accountName    the account name
     * @param openingBalance the opening balance
     */
    public Account(String accountName, double openingBalance,
                   HashMap<String, Customer> currentAccounts) {
        this.accountName = accountName;
        this.openingBalance = openingBalance;
        this.accountNumber = this.generateAccountNumber(currentAccounts.values());
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    private int generateAccountNumber(Collection<Customer> customers) {
        //find last account number
        int latestAccountNumber = 1000000; //this is the smallest account number we will accept
        for (Customer customer : customers){
            for (Account account : customer.getAccounts()){
                if (account.getAccountNumber() > latestAccountNumber) latestAccountNumber =
                        account.getAccountNumber();
            }
        }

        //add one to the value
        int newAccountNumber = latestAccountNumber+1;

        //if adding one will result in the increase to 9 digits.
        if (newAccountNumber > 99999999){
            int lastAccountNumber = 10000000;
            for (Customer customer : customers){
                for (Account account : customer.getAccounts()){
                    if (account.getAccountNumber() - lastAccountNumber > 1){
                        newAccountNumber = lastAccountNumber+1;
                    }
                    lastAccountNumber=account.getAccountNumber();
                }
            }
        }

        return newAccountNumber;
    }

    public String toString() {
        return (accountNumber + "-" + accountName + ": " + openingBalance);
    }

}
