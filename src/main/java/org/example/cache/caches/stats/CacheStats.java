package org.example.cache.caches.stats;

import java.util.Objects;

public final class CacheStats {

    private int hits;
    private int misses;
    private int evictions;
    private int size;
    private int capacity;

    public CacheStats(int hits, int misses, int evictions, int size, int capacity) {
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.size = size;
        this.capacity = capacity;
    }

    public CacheStats(int capacity) {
        this.hits = 0;
        this.misses = 0;
        this.evictions = 0;
        this.size = 0;
        this.capacity = capacity;
    }


    public void incrementHits() {
        hits++;
    }

    public void incrementMisses() {
        misses++;
    }

    public void incrementEvictions() {
        evictions++;
    }

    public void incrementSize() {
        size++;
    }

    public void decrementSize() {
        size--;
    }

    public int hits() {
        return hits;
    }

    public int misses() {
        return misses;
    }

    public int evictions() {
        return evictions;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CacheStats) obj;
        return this.hits == that.hits &&
            this.misses == that.misses &&
            this.evictions == that.evictions &&
            this.size == that.size &&
            this.capacity == that.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hits, misses, evictions, size, capacity);
    }

    @Override
    public String toString() {
        return "CacheStats[" +
            "hits=" + hits + ", " +
            "misses=" + misses + ", " +
            "evictions=" + evictions + ", " +
            "size=" + size + ", " +
            "capacity=" + capacity + ']';
    }


}
