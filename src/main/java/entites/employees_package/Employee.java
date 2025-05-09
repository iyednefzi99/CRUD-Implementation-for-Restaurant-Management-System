package entites.employees_package;


import java.time.LocalDate;

import entites.accounts_package.*;

public abstract class Employee {
    private final String id;
    private String name;
    private String phone;
    private final Account account;
    private LocalDate dateJoined;

    public Employee(String id, String name, String phone,
                   String username, String password, String email) {
       
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.account = new Account(username, password, email);
        this.dateJoined = LocalDate.now();
    }

    public Employee(String id, String name, String phone,
                   String username, String password, String email,
                   LocalDate dateJoined) {
        this(id, name, phone, username, password, email);
        setDateJoined(dateJoined);
    }

   

  
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Account getAccount() { return account; }
    public LocalDate getDateJoined() { return dateJoined; }

    // Setters
    public void setName(String name) {
     
        this.name = name;
    }

    public void setPhone(String phone) {
      
        this.phone = phone;
    }

    public void setDateJoined(LocalDate dateJoined) {
        if (dateJoined == null) throw new IllegalArgumentException("Join date cannot be null");
        if (dateJoined.isAfter(LocalDate.now())) throw new IllegalArgumentException("Join date cannot be in the future");
        this.dateJoined = dateJoined;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", accountUsername='" + account.getUsername() + '\'' +
                ", dateJoined=" + dateJoined +
                '}';
    }
}
