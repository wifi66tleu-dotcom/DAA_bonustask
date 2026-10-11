package org.example;
public class HashTable {
    private static class Entry {
        int key;
        Entry next;

        Entry(int key, Entry next) {
            this.key = key; this.next = next;
        }
    }
    private static final double MAX_LOAD = 0.75;
    private Entry[] buckets;
    private int size;
    public HashTable() {
        this(16);
    }
    public HashTable(int capacity) {
        int cap = 16;
        while (cap < capacity) cap <<= 1;
        buckets = new Entry[cap];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    private int indexFor(int key, int length) {
        int h = key * 0x9E3779B1;
        h ^= (h >>> 16);
        return h & (length - 1);
    }
    public boolean insert(int key) {
        int idx = indexFor(key, buckets.length);
        for (Entry e = buckets[idx]; e != null; e = e.next)
            if (e.key == key) return false;
        buckets[idx] = new Entry(key, buckets[idx]);
        size++;
        if (size > buckets.length * MAX_LOAD) resize();
        return true;
    }
    public boolean search(int key) {
        for (Entry e = buckets[indexFor(key, buckets.length)]; e != null; e = e.next)
            if (e.key == key) return true;
        return false;
    }
    public boolean delete(int key) {
        int idx = indexFor(key, buckets.length);
        Entry prev = null, cur = buckets[idx];
        while (cur != null && cur.key != key) { prev = cur; cur = cur.next; }
        if (cur == null) return false;
        if (prev == null) buckets[idx] = cur.next;
        else prev.next = cur.next;
        size--;
        return true;
    }
    public int maxChain() {
        int max = 0;
        for (Entry head : buckets) {
            int len = 0;
            for (Entry e = head; e != null; e = e.next) len++;
            max = Math.max(max, len);
        }
        return max;
    }

    private void resize() {
        Entry[] old = buckets;
        Entry[] fresh = new Entry[old.length * 2];
        for (Entry head : old) {
            for (Entry e = head; e != null; ) {
                Entry next = e.next;
                int idx = indexFor(e.key, fresh.length);
                e.next = fresh[idx];
                fresh[idx] = e;
                e = next;
            }
        }
        buckets = fresh;
    }
}
