package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entites.customers_package.Customer;
import entites.employees_package.Employee;
import entites.reservations_package.Reservation;
import entites.reservations_package.ReservationStatus;

public class ReservationDAO implements BaseDAO<Reservation> {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public Optional<Reservation> get(String id) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Optional<Customer> customerOpt = customerDAO.get(rs.getString("customer_id"));
                    Optional<Employee> employeeOpt = employeeDAO.get(rs.getString("employee_id"));
                    
                    if (customerOpt.isEmpty() || employeeOpt.isEmpty()) {
                        return Optional.empty();
                    }
                    
                    Reservation reservation = new Reservation(
                        customerOpt.get(),
                        rs.getTimestamp("reservation_time").toLocalDateTime(),
                        rs.getInt("party_size"),
                        rs.getString("special_requests"),
                        employeeOpt.get()
                    );
                    
                    reservation.setReservationID(rs.getString("reservation_id"));
                    reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                    
                    return Optional.of(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Optional<Customer> customerOpt = customerDAO.get(rs.getString("customer_id"));
                Optional<Employee> employeeOpt = employeeDAO.get(rs.getString("employee_id"));
                
                if (customerOpt.isEmpty() || employeeOpt.isEmpty()) {
                    continue;
                }
                
                Reservation reservation = new Reservation(
                    customerOpt.get(),
                    rs.getTimestamp("reservation_time").toLocalDateTime(),
                    rs.getInt("party_size"),
                    rs.getString("special_requests"),
                    employeeOpt.get()
                );
                
                reservation.setReservationID(rs.getString("reservation_id"));
                reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public boolean save(Reservation reservation) {
        String sql = "INSERT INTO reservations (reservation_id, customer_id, employee_id, " +
                     "reservation_time, party_size, special_requests, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservation.getReservationID());
            stmt.setString(2, reservation.getCustomer().getId());
            stmt.setString(3, reservation.getCreatedBy().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getDateTime()));
            stmt.setInt(5, reservation.getPartySize());
            stmt.setString(6, reservation.getSpecialRequests());
            stmt.setString(7, reservation.getStatus().name());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id = ?, employee_id = ?, " +
                     "reservation_time = ?, party_size = ?, special_requests = ?, status = ? " +
                     "WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservation.getCustomer().getId());
            stmt.setString(2, reservation.getCreatedBy().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime()));
            stmt.setInt(4, reservation.getPartySize());
            stmt.setString(5, reservation.getSpecialRequests());
            stmt.setString(6, reservation.getStatus().name());
            stmt.setString(7, reservation.getReservationID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}