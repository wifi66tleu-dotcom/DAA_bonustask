package org.example;
public class BST {
    private static class Node {
        int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }
    private Node root;
    private int size;
    private boolean added;
    private boolean removed;

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return root == null;
    }
    public boolean insert(int key) {
        added = false;
        root = add(root, key);
        if (added) size++;
        return added;
    }

    private Node add(Node node, int key) {
        if (node == null) {
            added = true;
            return new Node(key);
        }
        if (key < node.key) node.left = add(node.left, key);
        else if (key > node.key) node.right = add(node.right, key);
        return node;
    }
    public boolean search(int key) {
        Node node = root;
        while (node != null) {
            if (key < node.key) node = node.left;
            else if (key > node.key) node = node.right;
            else return true;
        }
        return false;
    }
    public boolean delete(int key) {
        removed = false;
        root = remove(root, key);
        if (removed) size--;
        return removed;
    }

    private Node remove(Node node, int key) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else {
            removed = true;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node pred = node.left;
            while (pred.right != null) pred = pred.right;
            node.key = pred.key;
            boolean keep = removed;
            node.left = remove(node.left, pred.key);
            removed = keep;
        }
        return node;
    }
    public int height() {
        return heightOf(root);
    }
    private int heightOf(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(heightOf(node.left), heightOf(node.right));
    }
}