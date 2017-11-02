/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

/**
 *
 * @author Lucas
 */
public class ListDoubleLinked<T> {
      private class Node<T> {

        public T element;
        private Node<T> next;
        private Node<T> prev;

        public Node(T e) {
            element = e;
            next = null;
            prev = null;
        }
    }
    private Node<T> header;
    private Node<T> trailer;
    private int count;

    public ListDoubleLinked() {
        header = new Node<T>(null);
        trailer = new Node<T>(null);
        header.next = trailer;
        trailer.prev = header;
        count = 0;
    }
    
    public void add(T element) {
        Node<T> n = new Node<>(element);
        Node<T> last = trailer.prev;
        n.next = trailer;
        n.prev = last;
        last.next = n;
        trailer.prev = n;
        count++;
    }
    
    public T get(int pos){
        Node<T> nodo = header.next;
        for(int i = 0; i < size(); i++){
            if(pos == i)
                return nodo.element;
            nodo = nodo.next;
        }
        return nodo.element;
    }
    
    public void addAll(ListDoubleLinked<T> lista){
        for(int i = 0; i < lista.size(); i++)
            add(lista.get(i));                         
    }
    
    public int size()
    {
        return count;
    }
    
    
        
    public String toString() {
        String str = "";
        Node aux = header.next;
        for (int i = 0; i < count; i++) {
            str = str + aux.element + "\n";
            aux = aux.next;
        }
        return str;
    }

}
