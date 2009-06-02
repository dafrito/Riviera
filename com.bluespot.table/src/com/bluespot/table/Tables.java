package com.bluespot.table;

public final class Tables {

    private Tables() {
        // Suppresses default constructor, ensuring non-instantiability.
        throw new Error("This class cannot be instantiated");
    }

    public static <T> void fill(Table<T> table, T value) {
        for(TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
            iter.next();
            iter.put(value);
        }
    }
    
    public static <T> void fill(Table<T> table, T... values) {
        int i = 0;
        for(TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
            iter.next();
            iter.put(values[i++]);
            i %= values.length;
        }
    }
    
    public static void fillCount(Table<Integer> table, final int start) {
        int i = start;
        for(TableIterator<Integer> iter = table.tableIterator(); iter.hasNext();) {
            iter.next();
            iter.put(i++);
        }
    }

}
