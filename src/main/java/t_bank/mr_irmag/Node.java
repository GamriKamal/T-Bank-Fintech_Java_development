package t_bank.mr_irmag;

import lombok.NonNull;

public class Node<T> {
    @NonNull
    T data;
    Node<T> next;

    public Node(@NonNull T data) {
        this.data = data;
        this.next = null;
    }
}
