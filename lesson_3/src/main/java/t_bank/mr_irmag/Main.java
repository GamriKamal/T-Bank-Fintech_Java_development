package t_bank.mr_irmag;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        list.add("Date");

        var iterator = list.stream().iterator();

        logger.info("Iterating over the list:");
        while (iterator.hasNext()) {
            logger.info(iterator.next());
        }

        iterator = list.stream().iterator();
        logger.info("Using forEachRemaining:");
        iterator.forEachRemaining(item -> logger.info(item));
    }
}
