package entites.utils_package;


public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address(String street, String city, String state, String zipCode) {
        this.streetAddress = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // Getters and setters
    public String getStreet() { return streetAddress; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    public void setStreet(String street) { this.streetAddress = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCountry(String country) { this.country = country; }

    public void displayAddress() {
        System.out.println("Address: " + streetAddress + ", " + city + ", " + state + " " + zipCode);
    }

    @Override
    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + zipCode;
    }
}