package dao;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import entites.accounts_package.Account;
import entites.accounts_package.AccountStatus;

public class AccountDAO implements BaseDAO<Account> {
    private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());
    
    @Override
    public Optional<Account> get(String username) {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                    );
                    account.setStatus(AccountStatus.valueOf(rs.getString("status")));
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving account: " + username, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Account account = new Account(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all accounts", e);
        }
        return accounts;
    }

    @Override
    public boolean save(Account account) {
        String sql = "INSERT INTO accounts (username, password, email, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getEmail());
            stmt.setString(4, account.getStatus().name());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving account: " + account.getUsername(), e);
            return false;
        }
    }

    @Override
    public boolean update(Account account) {
        String sql = "UPDATE accounts SET password = ?, email = ?, status = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, account.getPassword());
            stmt.setString(2, account.getEmail());
            stmt.setString(3, account.getStatus().name());
            stmt.setString(4, account.getUsername());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating account: " + account.getUsername(), e);
            return false;
        }
    }

    @Override
    public boolean delete(String username) {
        String sql = "DELETE FROM accounts WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting account: " + username, e);
            return false;
        }
    }
    
    /**
     * Checks if an account with the given username exists in the database
     * @param username The username to check
     * @return true if the account exists, false otherwise
     */
    public boolean exists(String username) {
        String sql = "SELECT 1 FROM accounts WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking account existence for username: " + username, e);
            return false;
        }
    }
    
    /**
     * Changes the password for a specific account
     * @param username The username of the account to update
     * @param newPassword The new password to set
     * @return true if the password was changed successfully, false otherwise
     */
    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Password changed successfully for user: " + username);
                return true;
            }
            logger.log(Level.WARNING, "No account found with username: " + username);
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error changing password for user: " + username, e);
            return false;
        }
    }
    
    /**
     * Updates the account status (ACTIVE, INACTIVE, etc.)
     * @param username The username of the account to update
     * @param newStatus The new status to set
     * @return true if the status was updated successfully, false otherwise
     */
    public boolean updateStatus(String username, AccountStatus newStatus) {
        String sql = "UPDATE accounts SET status = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus.name());
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating status for user: " + username, e);
            return false;
        }
    }
    
    /**
     * Retrieves the email associated with a username
     * @param username The username to look up
     * @return Optional containing the email if found, empty otherwise
     */
    public Optional<String> getEmail(String username) {
        String sql = "SELECT email FROM accounts WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving email for user: " + username, e);
        }
        return Optional.empty();
    }
    
    /**
     * Validates the username and password combination
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        String sql = "SELECT 1 FROM accounts WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error validating credentials for user: " + username, e);
            return false;
        }
    }
    
    /**
     * Counts all accounts in the database
     * @return total number of accounts
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error counting accounts", e);
        }
        return 0;
    }
}