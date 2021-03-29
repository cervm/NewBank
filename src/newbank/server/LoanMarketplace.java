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
            //TODO: Does this run or needed?
            System.out.println("new loan");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String checkLoanMeetsCriteria() throws Exception {
        StringBuilder exception = new StringBuilder();
        if (Integer.parseInt(this.loanAmount) > 5000){
            String ex = "Loan Amount Exceeds maximum. Please request an amount less than " + "£5000";
            exception.append(ex);
            throw new Exception(ex);
        } else if (Integer.parseInt(this.APR) > 30 || Integer.parseInt(this.APR) < 3){
            String ex = "The maximum APR is 30% the minimum is 3%";
            exception.append(ex);
            throw new Exception(ex);
        } else if (Integer.parseInt(this.term) > 12 || Integer.parseInt(this.term) < 1){
            String ex = "The loan term must be between 1 month and 12 months";
            exception.append(ex);
            throw new Exception(ex);
        } else {
            this.logLoan();
            exception.append("Loan Meets Criteria");
        }
        return exception.toString();
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
