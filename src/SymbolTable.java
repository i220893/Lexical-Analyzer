import java.util.*;

/**
 * SymbolTable Class
 * Stores information about identifiers encountered during scanning
 */
public class SymbolTable {
    
    /**
     * Inner class to store symbol information
     */
    private class SymbolInfo {
        String name;
        String type;
        int firstLine;
        int firstColumn;
        int frequency;
        
        SymbolInfo(String name, String type, int line, int column) {
            this.name = name;
            this.type = type;
            this.firstLine = line;
            this.firstColumn = column;
            this.frequency = 1;
        }
    }
    
    private Map<String, SymbolInfo> table;
    
    /**
     * Constructor
     */
    public SymbolTable() {
        this.table = new HashMap<>();
    }
    
    /**
     * Add or update a symbol in the table
     * @param name Identifier name
     * @param type Identifier type
     * @param line Line number of occurrence
     * @param column Column number of occurrence
     */
    public void addSymbol(String name, String type, int line, int column) {
        if (table.containsKey(name)) {
            // Increment frequency if already exists
            table.get(name).frequency++;
        } else {
            // Add new symbol
            table.put(name, new SymbolInfo(name, type, line, column));
        }
    }
    
    /**
     * Check if symbol exists
     * @param name Identifier name
     * @return true if exists, false otherwise
     */
    public boolean contains(String name) {
        return table.containsKey(name);
    }
    
    /**
     * Display the symbol table
     */
    public void display() {
        System.out.println("\n=== SYMBOL TABLE ===");
        System.out.println(String.format("%-20s %-15s %-15s %-10s", 
                                        "Name", "Type", "First Occurrence", "Frequency"));
        System.out.println("-".repeat(70));
        
        for (SymbolInfo info : table.values()) {
            String occurrence = String.format("Line:%d, Col:%d", info.firstLine, info.firstColumn);
            System.out.println(String.format("%-20s %-15s %-15s %-10d", 
                                            info.name, info.type, occurrence, info.frequency));
        }
    }
    
    /**
     * Get total number of unique symbols
     */
    public int size() {
        return table.size();
    }
}
