package newbank.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type LoanMarketplace
 */
public class LoanMarketplace {
    private Customer customer;
    private Double loanAmount;
    private String APR;
    private String term;
    private boolean loanMatched = false;
    private Database loanMarketplace = new Database("loans.json", true);

    /**
     * Instantiates a new loan. checkLoanMeetsCriteria() must be called when creating a loan
     *
     * @param customer the customer offering the loan
     * @param loanAmount The ammount offered to loan
     * @param APR APR (interest) offered on the loan
     * @param term Amount of years offered for the loan
     */
    public LoanMarketplace(Customer customer, Double loanAmount, String APR, String term) {
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.APR = APR;
        this.term = term;
    }

    /**
     * Instantiates a new loan.
     *
     * @return String
     */
    public String checkLoanMeetsCriteria() throws Exception {
        if (loanAmount > 5000){
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

    /**
     * Logs the loan to loans.json
     *
     * @return String
     */
    private String logLoan(){

        Map<String, Object> data = new HashMap<>();
        data.put("Customer", this.customer);
        data.put("Loan Amount", this.loanAmount);
        data.put("Term", this.term);
        data.put("APR", this.APR);
        data.put("Loan Matched", this.loanMatched);
        try {
            loanMarketplace.writeLoan(data);
            return "Loan has been submitted to the Marketplace";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Fail";
    }

}
