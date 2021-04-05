package newbank.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type LoanMarketplace
 */
public class LoanMarketplace {
    private int loanID = 0;
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
        //TODO: generate a unique loan ID
        this.loanID++;
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.APR = APR;
        this.term = term;
    }

    /**
     * Gets the customer of the loan
     *
     * @return Customer
     */
    public int getLoanID(){
        return this.loanID;
    }

    /**
     * Gets the customer of the loan
     *
     * @return Customer
     */
    public Customer getCustomer(){
        return this.customer;
    }

    /**
     * Gets the loan amount
     *
     * @return Double
     */
    public String getAPR(){
        return this.APR;
    }

    /**
     * Gets the loan amount
     *
     * @return Double
     */
    public String getTerm(){
        return this.term;
    }

    /**
     * Gets if the loan amount is matched
     *
     * @return Boolean
     */
    public Boolean getLoanMatched(){
        return this.loanMatched;
    }

    /**
     * Gets the customer of the loan
     *
     * @return Customer
     */
    public Double getLoanAmount(){
        return this.loanAmount;
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
            return "Loan Meets Criteria";
        }
    }

    /**
     * Loan to string
     *
     * @return String
     */
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(this.loanID+" | ");
        output.append(this.customer.getUserName()+" | ");
        output.append(this.loanAmount+" | ");
        output.append(this.APR+" | ");
        output.append(this.term+" | ");
        return output.toString();
    }
}
