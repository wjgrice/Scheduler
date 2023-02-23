package grice.c195.model;

/**
 * This class represents a Customer object with various properties including an ID, name, address,
 * postal code, phone, division, and country.
 */
public class Customer {
    private final int id;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String division;
    private final String phone;
    private final String country;

    /**
     * Constructs a Customer object with the given properties.
     *
     * @param id The customer's ID.
     * @param name The customer's name.
     * @param address The customer's address.
     * @param postalCode The customer's postal code.
     * @param phone The customer's phone number.
     * @param division The customer's division.
     * @param country The customer's country.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, String division, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
        this.division = division;
    }

    /**
     * Gets the customer's ID.
     *
     * @return The customer's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the customer's name.
     *
     * @return The customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the customer's address.
     *
     * @return The customer's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the customer's postal code.
     *
     * @return The customer's postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the customer's phone number.
     *
     * @return The customer's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the customer's division.
     *
     * @return The customer's division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Gets the customer's country.
     *
     * @return The customer's country.
     */
    public String getCountry() {
        return country;
    }
}
