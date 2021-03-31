package newbank.server;

import java.util.HashMap;
import java.util.Map;

public class LoanMarketplace {
    private Customer customer;
    private String loanAmount;
    private String APR;
    private String term;
    private boolean loanMatched = false;

    public LoanMarketplace(Customer customer, String loanAmount, String APR, String term) {
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.APR = APR;
        this.term = term;

        try {
            this.checkLoanMeetsCriteria();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String checkLoanMeetsCriteria() throws Exception {
        if (Integer.parseInt(this.loanAmount) > 5000){
            throw new Exception("Loan Amount Exceeds maximum. Please request an amount less than " + "£5000");
        } else if (Integer.parseInt(this.APR) > 30 || Integer.parseInt(this.APR) < 3){
            throw new Exception("The maximum APR is 30% the minimum is 3%");
        } else if (Integer.parseInt(this.term) > 12 || Integer.parseInt(this.term) < 1){
            throw new Exception("The loan term must be between 1 month and 12 months");
        } else {
            this.logLoan();
            return "Loan Meets Criteria";
        }
    }

    private String logLoan(){

        Database database = new Database("Marketplace.json");
        Map<String, Object> data = new HashMap<>();
        data.put("Customer", this.customer);
        data.put("Loan Amount", this.loanAmount);
        data.put("Term", this.term);
        data.put("APR", this.APR);
        data.put("Loan Matched", this.loanMatched);
        database.writeMapToFile(data);

        return "Loan has been submitted to the Marketplace";
    }

}
