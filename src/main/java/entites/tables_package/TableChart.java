package entites.tables_package;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TableChart {
    private final String tableChartID;
    private final List<Table> tables;

    public TableChart() {
        this.tableChartID = "TC-" + UUID.randomUUID().toString().substring(0, 8);
        this.tables = new ArrayList<>();
    }

    public TableChart(String tableChartID) {
        if (tableChartID == null || tableChartID.trim().isEmpty()) {
            throw new IllegalArgumentException("TableChart ID cannot be null or empty");
        }
        this.tableChartID = tableChartID;
        this.tables = new ArrayList<>();
    }

    // Getters
    public String getTableChartID() { return tableChartID; }
    public List<Table> getTables() { return new ArrayList<>(tables); }

    // Table management
    public void addTable(Table table) {
        if (table == null) throw new IllegalArgumentException("Table cannot be null");
        if (tables.stream().anyMatch(t -> t.getTableID().equals(table.getTableID()))) {
            throw new IllegalStateException("Table with ID " + table.getTableID() + " already exists");
        }
        tables.add(table);
    }

    public boolean removeTable(String tableID) {
        return tables.removeIf(table -> table.getTableID().equals(tableID));
    }

    public Optional<Table> findTable(String tableID) {
        return tables.stream()
                .filter(table -> table.getTableID().equals(tableID))
                .findFirst();
    }

    public Optional<Table> findAvailableTable(int partySize) {
        return tables.stream()
                .filter(Table::isAvailable)
                .filter(table -> table.getMaxCapacity() >= partySize)
                .findFirst();
    }

    public List<Table> getAllAvailableTables() {
        return tables.stream()
                .filter(Table::isAvailable)
                .toList();
    }

    public List<Table> getTablesByStatus(TableStatus status) {
        return tables.stream()
                .filter(table -> table.getStatus() == status)
                .toList();
    }

    public int getTotalCapacity() {
        return tables.stream()
                .mapToInt(Table::getMaxCapacity)
                .sum();
    }

    public int getAvailableSeats() {
        return tables.stream()
                .filter(Table::isAvailable)
                .mapToInt(Table::getMaxCapacity)
                .sum();
    }

    @Override
    public String toString() {
        return "TableChart{" +
                "tableChartID='" + tableChartID + '\'' +
                ", totalTables=" + tables.size() +
                ", availableTables=" + getTablesByStatus(TableStatus.FREE).size() +
                ", totalCapacity=" + getTotalCapacity() +
                '}';
    }
}