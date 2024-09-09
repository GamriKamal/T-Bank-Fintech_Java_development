package t_bank.mr_irmag;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Process integer list: \n");
        CustomLinkedList<Integer> integerList = new CustomLinkedList<>();
        processCustomLinkedList(integerList, List.of(1, 2, 3, 4, 5), Stream.of(10, 20, 30, 40, 50));

        logger.info("\n");
        logger.info("Process string list: \n");
        CustomLinkedList<String> stringList = new CustomLinkedList<>();
        processCustomLinkedList(stringList, List.of("apple", "banana", "cherry", "date"), Stream.of("a", "b", "c"));
    }

    private static <T> void processCustomLinkedList(CustomLinkedList<T> list, List<T> initialElements, Stream<T> streamElements) {
        initialElements.forEach(element -> {
            try {
                list.add(element);
            } catch (NullPointerException e) {
                logger.error("Cannot add null element: {}", e.getMessage());
            }
        });

        logListContent(list);

        try {
            list.remove(0);
            list.remove(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.error("Error removing element: {}", e.getMessage());
        }

        logger.info("Does list contain '{}'? {}", initialElements.get(0), list.contains(initialElements.get(0)));

        logger.info("Add all elements from list: {}", list.addAll(initialElements));

        processStreamWithReduce(list, streamElements);

        logListContent(list);
    }

    private static <T> void processStreamWithReduce(CustomLinkedList<T> list, Stream<T> stream) {
        stream.reduce(list, (l, element) -> {
            l.add(element);
            return l;
        }, (l1, l2) -> {
            l2.forEach(l1::add);
            return l1;
        });

        StringBuilder builder = new StringBuilder();
        list.forEach(element -> builder.append(element).append(" "));
        logger.info("Stream elements added to list: {}", builder);
    }

    private static <T> void logListContent(CustomLinkedList<T> list) {
        StringBuilder builder = new StringBuilder();
        list.forEach(element -> builder.append(element).append(" "));
        logger.info("List content: {}", builder);
    }
}