package entites.tables_package;


public class TableSeat {
    private final int tableSeatNumber;
    private SeatType type;

    public TableSeat(int tableSeatNumber, SeatType type) {
        if (tableSeatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Seat type cannot be null");
        }
        
        this.tableSeatNumber = tableSeatNumber;
        this.type = type;
    }

    // Getters
    public int getTableSeatNumber() { return tableSeatNumber; }
    public SeatType getType() { return type; }

    public void updateSeatType(SeatType newType) {
        if (newType == null) {
            throw new IllegalArgumentException("Seat type cannot be null");
        }
        this.type = newType;
    }

    public boolean isWheelchairAccessible() {
        return this.type == SeatType.WHEELCHAIR_ACCESSIBLE;
    }

    public boolean isChildFriendly() {
        return this.type == SeatType.HIGH_CHAIR || this.type == SeatType.BOOTH;
    }

    @Override
    public String toString() {
        return "TableSeat{" +
                "number=" + tableSeatNumber +
                ", type=" + type +
                '}';
    }
}
