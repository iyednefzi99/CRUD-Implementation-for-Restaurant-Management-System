package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entites.accounts_package.Account;
import entites.employees_package.Employee;
import entites.employees_package.Manager;
import entites.employees_package.Receptionist;
import entites.employees_package.Waiter;

public class EmployeeDAO implements BaseDAO<Employee> {
    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    public Optional<Employee> get(String id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("account_username");
                    Optional<Account> accountOpt = accountDAO.get(username);
                    if (accountOpt.isEmpty()) {
                        return Optional.empty();
                    }
                    
                    Account account = accountOpt.get();
                    String role = rs.getString("role");
                    
                    Employee employee;
                    switch (role) {
                        case "WAITER":
                            employee = new Waiter(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("phone"),
                                account.getUsername(),
                                account.getPassword(),
                                account.getEmail(),
                                rs.getDate("date_joined").toLocalDate()
                            );
                            break;
                        case "MANAGER":
                            employee = new Manager(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("phone"),
                                account.getUsername(),
                                account.getPassword(),
                                account.getEmail(),
                                rs.getDate("date_joined").toLocalDate()
                            );
                            break;
                        case "RECEPTIONIST":
                            employee = new Receptionist(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("phone"),
                                account.getUsername(),
                                account.getPassword(),
                                account.getEmail(),
                                rs.getDate("date_joined").toLocalDate()
                            );
                            break;
                        default:
                            return Optional.empty();
                    }
                    
                    return Optional.of(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String username = rs.getString("account_username");
                Optional<Account> accountOpt = accountDAO.get(username);
                if (accountOpt.isEmpty()) continue;
                
                Account account = accountOpt.get();
                String role = rs.getString("role");
                
                Employee employee;
                switch (role) {
                    case "WAITER":
                        employee = new Waiter(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            account.getUsername(),
                            account.getPassword(),
                            account.getEmail(),
                            rs.getDate("date_joined").toLocalDate()
                        );
                        break;
                    case "MANAGER":
                        employee = new Manager(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            account.getUsername(),
                            account.getPassword(),
                            account.getEmail(),
                            rs.getDate("date_joined").toLocalDate()
                        );
                        break;
                    case "RECEPTIONIST":
                        employee = new Receptionist(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            account.getUsername(),
                            account.getPassword(),
                            account.getEmail(),
                            rs.getDate("date_joined").toLocalDate()
                        );
                        break;
                    default:
                        continue;
                }
                
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public boolean save(Employee employee) {
        // First save the account
        Account account = employee.getAccount();
        if (!accountDAO.save(account)) {
            return false;
        }
        
        String role;
        if (employee instanceof Waiter) {
            role = "WAITER";
        } else if (employee instanceof Manager) {
            role = "MANAGER";
        } else if (employee instanceof Receptionist) {
            role = "RECEPTIONIST";
        } else {
            return false;
        }
        
        String sql = "INSERT INTO employees (id, name, phone, account_username, date_joined, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, account.getUsername());
            stmt.setDate(5, Date.valueOf(employee.getDateJoined()));
            stmt.setString(6, role);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Employee employee) {
        // First update the account
        Account account = employee.getAccount();
        if (!accountDAO.update(account)) {
            return false;
        }
        
        String role;
        if (employee instanceof Waiter) {
            role = "WAITER";
        } else if (employee instanceof Manager) {
            role = "MANAGER";
        } else if (employee instanceof Receptionist) {
            role = "RECEPTIONIST";
        } else {
            return false;
        }
        
        String sql = "UPDATE employees SET name = ?, phone = ?, date_joined = ?, role = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());
            stmt.setDate(3, Date.valueOf(employee.getDateJoined()));
            stmt.setString(4, role);
            stmt.setString(5, employee.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        // First get the employee to delete their account too
        Optional<Employee> employeeOpt = get(id);
        if (employeeOpt.isEmpty()) {
            return false;
        }
        
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            boolean deleted = stmt.executeUpdate() > 0;
            
            if (deleted) {
                // Delete the associated account
                return accountDAO.delete(employeeOpt.get().getAccount().getUsername());
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}