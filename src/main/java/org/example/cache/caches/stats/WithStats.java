package org.example.cache.caches.stats;

public interface WithStats {
    String RESET = "\u001B[0m";
    String BLUE = "\u001B[34m";

    CacheStats getCacheStats();

    default int hitCount() {
        return getCacheStats().hits();
    }

    default int missCount() {
        return getCacheStats().misses();
    }

    default int evictionCount() {
        return getCacheStats().evictions();
    }

    default double hitRate() {
        int gets = getCacheStats().hits() + getCacheStats().misses();
        if (gets == 0) {
            return 0;
        } else {
            return (double) getCacheStats().hits() / (gets);
        }
    }

    default int capacity() {
        return getCacheStats().capacity();
    }

    default int size() {
        return getCacheStats().size();
    }

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
