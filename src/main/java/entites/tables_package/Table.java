package entites.tables_package;

import java.util.UUID;

public class Table {
    private String tableID;
    private TableStatus status;
    private int maxCapacity;
    private int locationIdentifier;

    public Table( int maxCapacity, int locationIdentifier) {
        this.tableID = "TBL-" + UUID.randomUUID().toString().substring(0, 8);
        this.maxCapacity = maxCapacity;
        this.locationIdentifier = locationIdentifier;
        this.status = TableStatus.FREE;
    }

    // Getters
    public String getTableID() { return tableID; }
    public TableStatus getStatus() { return status; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getLocationIdentifier() { return locationIdentifier; }

    // Setters
    public void setStatus(TableStatus status) { this.status = status; }

    public boolean isAvailable() {
        return status == TableStatus.FREE;
    }

    public boolean isOccupied() {
        return status == TableStatus.OCCUPIED;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableID='" + tableID + '\'' +
                ", status=" + status +
                ", maxCapacity=" + maxCapacity +
                ", locationIdentifier=" + locationIdentifier +
                '}';
    }

	public void setTableID(String id) {
	      if (id == null || id.isBlank()) {
	            throw new IllegalArgumentException("tableID cannot be null or empty");
	        }
	        this.tableID = id;
	    }

		
	}

