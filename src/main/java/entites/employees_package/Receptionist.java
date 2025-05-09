package entites.employees_package;

import java.time.LocalDate;
import java.time.LocalDateTime;

import entites.customers_package.Customer;
import entites.reservations_package.Reservation;


public class Receptionist extends Employee {
    
    public Receptionist(String id, String name, String phone, 
                      String username, String password, String email) {
        super(id, name, phone, username, password, email);
    }

    public Receptionist(String id, String name, String phone,
                      String username, String password, String email,
                      LocalDate dateJoined) {
        super(id, name, phone, username, password, email, dateJoined);
    }

    @Override
    public String getRole() {
        return "Receptionist";
    }

    public Reservation createReservation(Customer customer, LocalDateTime dateTime,
                                       int partySize, String specialRequests) {
        if (customer == null || dateTime == null || partySize <= 0) {
            throw new IllegalArgumentException("Invalid reservation parameters");
        }
        
        System.out.printf("Creating reservation for %s at %s for %d people%n",
                         customer.getName(), dateTime, partySize);
        return new Reservation(customer, dateTime, partySize, specialRequests, this);
    }

    public boolean cancelReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        
        System.out.println("Canceling reservation #" + reservation.getReservationID());
        return reservation.cancel();
    }

    public boolean modifyReservation(Reservation reservation, LocalDateTime newDateTime,
                                   int newPartySize, String newRequests) {
        if (reservation == null || newDateTime == null || newPartySize <= 0) {
            throw new IllegalArgumentException("Invalid modification parameters");
        }
        
        System.out.println("Modifying reservation #" + reservation.getReservationID());
        return reservation.modify(newDateTime, newPartySize, newRequests);
    }
}