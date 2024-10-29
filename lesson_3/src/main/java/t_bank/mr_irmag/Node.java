package t_bank.mr_irmag;

import lombok.NonNull;

public class Node<E> {
    @NonNull
    E data;
    Node<E> next;

    public Node(@NonNull E data) {
        this.data = data;
        this.next = null;
    }
}
