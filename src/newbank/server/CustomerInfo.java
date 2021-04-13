package newbank.server;

public class CustomerInfo {

    private final String address;
    private final String phoneNumber;
    private final String fullName;
    private final String securityQuestion;

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
     * @return account details as a string {@literal "<Address> | <Phone Number> | <Full Name> | <Security Question>"}
     */
    public String toString() {
        return "Address" + " | " + address + " | " + "\n" +
                "Phone Number" + " | " + phoneNumber + " | " + "\n" +
                "Full Name" + " | " + fullName + " | " + "\n" +
                "Security Question" + " | " + securityQuestion + "|";
    }
}
