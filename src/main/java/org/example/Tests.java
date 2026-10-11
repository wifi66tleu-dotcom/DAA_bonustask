package org.example;
import java.util.Random;
import java.util.TreeSet;
public class Tests {
    static int failed = 0;
    static void check(boolean ok, String name) {
        if (!ok) { failed++; System.out.println("FAIL: " + name); }
    }

    public static void main(String[] args) {
        BST bst = new BST();
        HashTable ht = new HashTable();
        int[] keys = {55, 25, 75, 10, 40, 65, 90};
        for (int k : keys) check(bst.insert(k) && ht.insert(k), "insert " + k);
        check(!bst.insert(55) && !ht.insert(55), "duplicate rejected");
        for (int k : keys) check(bst.search(k) && ht.search(k), "search " + k);
        check(!bst.search(1) && !ht.search(1), "missing key");
        for (int k : new int[]{10, 25, 55}) check(bst.delete(k) && ht.delete(k), "delete " + k);
        check(!bst.delete(10) && !ht.delete(10), "delete twice");
        for (int k : new int[]{40, 65, 75, 90}) check(bst.search(k) && ht.search(k), "kept " + k);
        check(bst.size() == 4 && ht.size() == 4, "size after deletes");
        BST b = new BST();
        HashTable h = new HashTable();
        TreeSet<Integer> ref = new TreeSet<>();
        Random rnd = new Random(1);
        for (int i = 0; i < 20_000; i++) {
            int k = rnd.nextInt(500);
            boolean exp;
            switch (rnd.nextInt(3)) {
                case 0 -> { exp = ref.add(k);      check(b.insert(k) == exp && h.insert(k) == exp, "random insert " + k); }
                case 1 -> { exp = ref.contains(k); check(b.search(k) == exp && h.search(k) == exp, "random search " + k); }
                default -> { exp = ref.remove(k);  check(b.delete(k) == exp && h.delete(k) == exp, "random delete " + k); }
            }
        }
        check(b.size() == ref.size() && h.size() == ref.size(), "random: final sizes");

        System.out.println(failed == 0 ? "All tests passed" : failed + " test(s) failed");
    }
}

