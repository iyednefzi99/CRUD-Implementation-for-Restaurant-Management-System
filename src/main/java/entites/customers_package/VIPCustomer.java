package entites.customers_package;

import java.util.Date;


public class VIPCustomer extends Customer {
    private int clientDiscount;

    public VIPCustomer(String id, String name, String phone,
                     String username, String password, String email) {
        super(id, name, phone, username, password, email);
        this.clientDiscount = 10;
    }

    public VIPCustomer(String id, String name, String phone,
                     String username, String password, String email,
                     int clientDiscount) {
        super(id, name, phone, username, password, email);
        setClientDiscount(clientDiscount);
    }

    public VIPCustomer(String id, String name, String phone,
                     String username, String password, String email,
                     Date lastVisited, int clientDiscount) {
        super(id, name, phone, username, password, email, lastVisited);
        setClientDiscount(clientDiscount);
    }

    @Override
    public String getRole() {
        return "VIP Customer";
    }

    public int getClientDiscount() {
        return clientDiscount;
    }

    public void setClientDiscount(int clientDiscount) {
        if (clientDiscount < 0 || clientDiscount > 50) {
            throw new IllegalArgumentException("Discount must be between 0 and 50 percent");
        }
        this.clientDiscount = clientDiscount;
    }

    public double applyDiscount(double amount) {
        return amount * (100 - clientDiscount) / 100.0;
    }
}