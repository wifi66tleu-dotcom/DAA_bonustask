package org.example;
import java.util.Random;
public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000};
    private static final int RUNS = 20;

    public static void main(String[] args) {
        System.out.println("--- Best-case ---");
        runAll(false);
        System.out.println();
        System.out.println("--- Worst-case  ---");
        runAll(true);
    }

    private static void runAll(boolean sorted) {
        System.out.printf("%-8s %-11s %12s %12s %12s %8s%n",
                "n", "structure", "insert(ms)", "search(ms)", "delete(ms)", "extra");
        for (int n : SIZES) {
            int[] keys = generateKeys(n, sorted);
            warmUp(keys);

            double bi = 0, bs = 0, bd = 0, hi = 0, hs = 0, hd = 0;
            int height = 0, chain = 0;

            for (int r = 0; r < RUNS; r++) {
                BST bst = new BST();
                long t = System.nanoTime();
                for (int k : keys) bst.insert(k);
                bi += System.nanoTime() - t;
                height = bst.height();

                t = System.nanoTime();
                for (int k : keys) bst.search(k);
                bs += System.nanoTime() - t;

                t = System.nanoTime();
                for (int k : keys) bst.delete(k);
                bd += System.nanoTime() - t;

                HashTable ht = new HashTable();
                t = System.nanoTime();
                for (int k : keys) ht.insert(k);
                hi += System.nanoTime() - t;
                chain = ht.maxChain();

                t = System.nanoTime();
                for (int k : keys) ht.search(k);
                hs += System.nanoTime() - t;

                t = System.nanoTime();
                for (int k : keys) ht.delete(k);
                hd += System.nanoTime() - t;
            }
            print(n, "BST", bi, bs, bd, "h=" + height);
            print(n, "HashTable", hi, hs, hd, "chain=" + chain);
        }
    }

    private static void print(int n, String name, double ins, double sea, double del, String extra) {
        double toMs = 1_000_000.0 * RUNS;
        System.out.printf("%-8d %-11s %12.4f %12.4f %12.4f %8s%n",
                n, name, ins / toMs, sea / toMs, del / toMs, extra);
    }

    private static int[] generateKeys(int n, boolean sorted) {
        int[] a = new int[n];
        if (sorted) {
            for (int i = 0; i < n; i++) a[i] = i;
            return a;
        }
        Random rnd = new Random(42);
        java.util.HashSet<Integer> used = new java.util.HashSet<>();
        int i = 0;
        while (i < n) {
            int v = rnd.nextInt(1_000_000);
            if (used.add(v)) a[i++] = v;
        }
        return a;
    }
    private static void warmUp(int[] keys) {
        for (int r = 0; r < 5; r++) {
            BST b = new BST();
            HashTable h = new HashTable();
            for (int k : keys) { b.insert(k); h.insert(k); }
            for (int k : keys) { b.search(k); h.search(k); }
            for (int k : keys) { b.delete(k); h.delete(k); }
        }
    }
}
