package entites.employees_package;

import java.time.LocalDate;



public class Manager extends Employee {
    public Manager(String id, String name, String phone,
                 String username, String password, String email) {
        super(id, name, phone, username, password, email);
    }

    public Manager(String id, String name, String phone,
                 String username, String password, String email,
                 LocalDate dateJoined) {
        super(id, name, phone, username, password, email, dateJoined);
    }

    @Override
    public String getRole() {
        return "Manager";
    }

   // public void manageEmployee(Employee employee) {
     //   System.out.println("Managing employee: " + employee.getName());
       //}

 /*   public void manageMenu(Menu menu) {
        System.out.println("Managing menu: " + menu.getTitle());
    }

    public void manageSectionMenu(MenuSection section) {
        System.out.println("Managing section menu: " + section.getTitle());
    }

    public void manageMenuItems(MenuItem item) {
        System.out.println("Managing menu item: " + item.getTitle());
    }*/
}