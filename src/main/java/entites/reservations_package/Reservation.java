package entites.reservations_package;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import entites.customers_package.Customer;
import entites.employees_package.Employee;


public class Reservation {
    private  String reservationID;
    private final Customer customer;
    private LocalDateTime dateTime;
    private int partySize;
    private String specialRequests;
    private final Employee createdBy;
    private ReservationStatus status;

    public Reservation(Customer customer, LocalDateTime dateTime,
                     int partySize, String specialRequests, Employee createdBy) {
        this.reservationID = "RES-" + UUID.randomUUID().toString().substring(0, 8);
        this.customer = Objects.requireNonNull(customer, "Customer cannot be null");
        this.dateTime = Objects.requireNonNull(dateTime, "DateTime cannot be null");
        this.partySize = validatePartySize(partySize);
        this.specialRequests = specialRequests != null ? specialRequests : "";
        this.createdBy = Objects.requireNonNull(createdBy, "Employee cannot be null");
        this.status = ReservationStatus.CONFIRMED;
    }

    private int validatePartySize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Party size must be positive");
        }
        return size;
    }

    public boolean modify(LocalDateTime newDateTime, int newPartySize, String newRequests) {
        if (status != ReservationStatus.CONFIRMED) {
            return false;
        }
        
        this.dateTime = Objects.requireNonNull(newDateTime);
        this.partySize = validatePartySize(newPartySize);
        this.specialRequests = newRequests != null ? newRequests : "";
        return true;
    }

    public boolean cancel() {
        if (status == ReservationStatus.CANCELED || status == ReservationStatus.COMPLETED) {
            return false;
        }
        this.status = ReservationStatus.CANCELED;
        return true;
    }

    // Getters
    public String getReservationID() { return reservationID; }
    public Customer getCustomer() { return customer; }
    public LocalDateTime getDateTime() { return dateTime; }
    public int getPartySize() { return partySize; }
    public String getSpecialRequests() { return specialRequests; }
    public Employee getCreatedBy() { return createdBy; }
    public ReservationStatus getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("Reservation[%s] for %s at %s (%d people)",
                           reservationID, customer.getName(), dateTime, partySize);
    }

	public void setReservationID(String id) {
	      if (id == null || id.isBlank()) {
	            throw new IllegalArgumentException("ReservationID cannot be null or empty");
	        }
	        this.reservationID = id;
	    }

	public void setStatus(ReservationStatus valueOf) {
		// TODO Auto-generated method stub
		this.status=valueOf;
		
	}

		
	}
