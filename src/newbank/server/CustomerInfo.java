package newbank.server;

public class CustomerInfo {

    private String address;
    private String phoneNumber;
    private String fullName;
    private String securityQuestion;

    /**
     * Instantiates Customer Details.
     * Set address, phone number, full name, security question.
     */

    public CustomerInfo(String address, String phoneNumber, String fullName, String securityQuestion) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.securityQuestion = securityQuestion;
    }
}
