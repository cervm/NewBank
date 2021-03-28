package newbank.server;

import java.util.HashMap;
import java.util.Map;

public class LoanMarketplace {
    private Customer customer;
    private String loanAmount;
    private String APR;
    private String term;

    public LoanMarketplace(Customer customer, String loanAmount, String APR, String term) {
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.APR = APR;
        this.term = term;

        if (this.checkLoanMeetsCriteria()) {
            this.logLoan();
        }
    }

    private boolean checkLoanMeetsCriteria(){
        if (!(Integer.parseInt(this.loanAmount) <= 5000)){
            System.out.println("Loan Amount Exceeds maximum. Please request an amount less than " +
                    "£5000");
            return false;
        }

        if (Integer.parseInt(this.APR) > 30 || Integer.parseInt(this.APR) < 3){
            System.out.println("The maximum APR is 30% the minimum is 3%");
            return false;
        }

        if (Integer.parseInt(this.term) > 12 || Integer.parseInt(this.term) < 1){
            System.out.println("The loan term must be between 1 month and 12 months");
            return false;
        }

        return true;
    }

    private void logLoan(){

        Database database = new Database("Marketplace.json");
        Map<String, Object> data = new HashMap<>();
        data.put("Customer", this.customer);
        data.put("Loan Amount", this.loanAmount);
        data.put("Term", this.term);
        data.put("APR", this.APR);
        database.writeMapToFile(data);
    }

}
