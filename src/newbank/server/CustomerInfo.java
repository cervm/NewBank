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
    /**
     * Parses account details as a string
     *
     * @return account details as a string "<Address> | <Phone Number> | <Full Name> | <Security Question>"
     */
    public String toString() {
        return "Address" + " | " + address + " | " + "\n" +
                "Phone Number" + " | " + phoneNumber + " | " + "\n"+
                "Full Name" + " | " + fullName + " | " + "\n" +
                "Security Question" + " | " + securityQuestion + "|";
    }
}
