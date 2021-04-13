package newbank.server;

public class Loan {
    private static int count = 0;
    private final String loanID;
    private final CustomerID loanToCustomerID;
    private final CustomerID loanFromCustomerID;
    private final double loanAmount;
    private final String apr;
    private final String loanTerm;
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
