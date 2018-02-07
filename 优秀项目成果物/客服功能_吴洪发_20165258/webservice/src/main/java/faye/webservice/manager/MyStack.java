package faye.webservice.manager;

public class MyStack {
    private int size = 0;
    private static class Node{
        Manager manager ;
        Node next;
    }
    private Node head = new Node();
    public void add(Manager manager){
        Node node = new Node();
        Node p = head.next;
        head.next = node;
        node.manager = manager;
        node.next = p;
        size++;
    }
    public Manager remove(Manager manager){
        Node p = head;
        while (p.next!=null){
            if (p.next.manager.equals(manager)){
                Node mid = p.next;
                p.next = p.next.next;
                size--;
                return mid.manager;
            }
            p = p.next;
        }

        return null;

    }
    public Manager peek(){
        if (head.next==null)
            return null;
        return head.next.manager;
    }
    public void adjust(){
        Node pIndex = head;
        //冒泡排序
        while(pIndex!=null){
            Node p1 = head;
            while (p1!=null){
                Node first = p1.next;
                if (first==null){
                    break;
                }
                Node second = first.next;
                if (second==null){
                    break;
                }
                if (first.manager.getAmount()>second.manager.getAmount()){
                    Node mid = second.next;
                    p1.next = second;
                    second.next = first;
                    first.next = mid;
                }
                p1 = p1.next;
            }
            pIndex = pIndex.next;
        }

    }

    public int getSize() {
        return size;
    }
}
