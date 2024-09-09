package t_bank.mr_irmag;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomLinkedList<E> implements Iterable<E> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomLinkedList.class);

    Node<E> head;

    private int size = 0;

    CustomLinkedList() {
        this.head = null;
    }


    public void add(E data) {
        Node<E> newNode = new Node<>(data);

        if (data == null) {
            throw new NullPointerException("Data is null");
        }

        if (head == null) {
            head = newNode;
        } else {
            Node<E> currentNode = head;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }

        size++;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node<E> currentNode = head;
        while (currentNode.next != null && index > 0) {
            currentNode = currentNode.next;
            index--;
        }

        return currentNode.data;
    }

    public void remove(int index) {
        if (head == null) {
            throw new NullPointerException("head is null");
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> currentNode = head;
        Node<E> previousNode = null;

        if (index == 0) {
            head = head.next;
        } else {
            while (currentNode.next != null && index > 0) {
                previousNode = currentNode;
                currentNode = currentNode.next;
                index--;
            }

            previousNode.next = currentNode.next;
        }

        size--;
        logger.debug("Removed {}", currentNode.data, " from the list by index {}", index);
    }

    public boolean addAll(Collection<? extends E> collection) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }

        boolean headIsNull = false;
        if (head == null) {
            headIsNull = true;
            head = (Node<E>) new Node<>(1);
        }

        Node<E> currentNode = head;
        while (currentNode != null && currentNode.next != null) {
            currentNode = currentNode.next;
        }

        for (E element : collection) {
            Node<E> newNode = new Node<>(element);
            if (currentNode == null) {
                currentNode = newNode;
                currentNode.next = currentNode;
            } else {
                currentNode.next = newNode;
                currentNode = currentNode.next;
            }
            size++;
        }

        if (headIsNull) {
            head = head.next;
        }

        return true;
    }

    public boolean contains(E data) {

        Node<E> currentNode = head;
        while (currentNode != null) {
            if (data.equals(currentNode.data)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Stream<E> stream() {
        Iterator<E> iterator = iterator();
        int characteristics = 0;
        Spliterator<E> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E data = currentNode.data;
                currentNode = currentNode.next;
                return data;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Iterable.super.spliterator();
    }
}
