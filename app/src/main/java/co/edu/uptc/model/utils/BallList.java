package co.edu.uptc.model.utils;

public class BallList<T> {

    private static class Node <T> {
        T data;
        Node <T> next;
        Node <T> prev;
        Node (T data){
            this.data = data;
        }
    }

    private Node <T> head;
    private Node <T> tail;
    private int size;

    public void addLast(T data) {
        Node<T> node = new Node<>(data);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public void remove (T data) {
        Node <T> cur = head;
        while (cur != null) {
            if (cur.data == data) {
                unlink(cur);
                return;
            }
            cur = cur.next;
        }
    }

    private void unlink (Node<T> node) {
        if(node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if(node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
        size --;
    }

    public void clear () {
        head = tail = null;
        size = 0;
    }

    public int size (){
        return size;
    }

    public java.util.List<T> toList() {
        java.util.List<T> result = new java.util.ArrayList<>(size);
        for (Node<T> n = head; n != null; n = n.next) result.add(n.data);
        return result;
    }

}