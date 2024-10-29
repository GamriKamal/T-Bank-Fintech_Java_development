package t_bank.mr_irmag;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomLinkedList<E> implements CustomIterator<E> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomLinkedList.class);

    private Node<E> head;
    private Node<E> currentNode;
    private int size = 0;

    public CustomLinkedList() {
        this.head = null;
        this.currentNode = head;
    }

    public void add(E data) {
        if (data == null) {
            throw new NullPointerException("Data is null");
        }

        Node<E> newNode = new Node<>(data);

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
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node<E> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.data;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node<E> currentNode = head;
        Node<E> previousNode = null;

        if (index == 0) {
            head = head.next;
        } else {
            for (int i = 0; i < index; i++) {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }

            if (previousNode != null) {
                previousNode.next = currentNode.next;
            }
        }

        size--;
        logger.debug("Removed {} from the list by index {}", currentNode.data, index);
    }

    public boolean addAll(Collection<? extends E> collection) {
        if (collection == null) {
            throw new NullPointerException("Collection is null");
        }

        if (collection.isEmpty()) {
            return false;
        }

        Node<E> currentNode = head;
        if (head == null) {
            head = new Node<>(null);
            currentNode = head;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
        }

        for (E element : collection) {
            Node<E> newNode = new Node<>(element);
            currentNode.next = newNode;
            currentNode = newNode;
            size++;
        }

        if (head.data == null) {
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
        Iterator<E> iterator = new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E data = current.data;
                current = current.next;
                return data;
            }
        };

        int characteristics = 0;
        Spliterator<E> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
        return StreamSupport.stream(spliterator, false);
    }


    @Override
    public boolean hasNext() {
        if (currentNode == null) {
            currentNode = head;
            return false;
        }
        return true;
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

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }

}
