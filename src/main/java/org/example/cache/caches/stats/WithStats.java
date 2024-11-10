package org.example.cache.caches.stats;

public interface WithStats {
    String RESET = "\u001B[0m";
    String BLUE = "\u001B[34m";

    int hitCount();

    int missCount();

    int evictionCount();

    double hitRate();

    int capacity();

    int size();

    void resetStats();

    default void printStats() {
        String hits = String.format("%5d", hitCount());
        String misses = String.format("%5d", missCount());
        String hitRate = String.format("%5.2f", hitRate());  // assuming hitRate is a double
        String evictions = String.format("%5d", evictionCount());
        String capacity = String.format("%5d", capacity());
        String size = String.format("%5d", size());

        String horizontalLine = BLUE + "+-----------+------+" + RESET;
        String verticalLine = BLUE + "|" + RESET;

        System.out.println(horizontalLine);
        System.out.println(verticalLine + " Hits:      " + hits + " " + verticalLine);
        System.out.println(verticalLine + " Misses:    " + misses + " " + verticalLine);
        System.out.println(verticalLine + " HitRate:   " + hitRate + " " + verticalLine);
        System.out.println(verticalLine + " Evictions: " + evictions + " " + verticalLine);
        System.out.println(verticalLine + " Capacity:  " + capacity + " " + verticalLine);
        System.out.println(verticalLine + " Size:      " + size + " " + verticalLine);
        System.out.println(horizontalLine);
    }

}
