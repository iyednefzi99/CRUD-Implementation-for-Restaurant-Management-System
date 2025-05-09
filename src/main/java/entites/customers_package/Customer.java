package entites.customers_package;


import java.util.Date;

import entites.accounts_package.*;

public  class Customer {
    private final String id;
    private String name;
    private String phone;
    private final Account account;
    protected Date lastVisited;

    public Customer(String id, String name, String phone,
                   String username, String password, String email) {
      
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.account = new Account(username, password, email);
        this.lastVisited = new Date();
    }

    public Customer(String id, String name, String phone,
                   String username, String password, String email,
                   Date lastVisited) {
        this(id, name, phone, username, password, email);
        setLastVisited(lastVisited);
    }

  

    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Account getAccount() { return account; }
    public Date getLastVisited() { return lastVisited; }

    // Setters
    public void setName(String name) {
      
        this.name = name;
    }

    public void setPhone(String phone) {
    
        this.phone = phone;
    }

    public void setLastVisited(Date lastVisited) {
    
        this.lastVisited = lastVisited;
    }

    public void updateLastVisited() {
        this.lastVisited = new Date();
    }

    public  String getRole() {
		return "Customer";
	}

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", accountUsername='" + account.getUsername() + '\'' +
                ", lastVisited=" + lastVisited +
                '}';
    }
}