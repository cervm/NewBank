package newbank.server;

public class Loan {
    private static int count = 0;
    private String loanID;
    private CustomerID loanToCustomerID;
    private CustomerID loanFromCustomerID;
    private double loanAmount;
    private String apr;
    private String loanTerm;
    private int i = 0;

    public Loan(CustomerID toLoanCustermerID, CustomerID fromLoanCustomerID, double lAmount, String APR, String lTerm) {
        count++;
        loanID = "CL" + count;
        this.loanToCustomerID = toLoanCustermerID;
        this.loanFromCustomerID = fromLoanCustomerID;
        this.loanAmount = lAmount;
        this.apr = APR;
        this.loanTerm = lTerm;
    }

    public String getLoanID() {
        return loanID;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public String getApr() {
        return apr;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public CustomerID getLoanFromCustomerID() {
        return loanFromCustomerID;
    }

    public CustomerID getLoanToCustomerID() {
        return loanToCustomerID;
    }
}
