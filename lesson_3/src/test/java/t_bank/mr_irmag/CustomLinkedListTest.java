package t_bank.mr_irmag;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomLinkedListTest {

    private CustomLinkedList<String> customLinkedList;
    private Logger logger;

    @BeforeEach
    void setUp() {
        customLinkedList = new CustomLinkedList<>();
        logger = mock(Logger.class);
    }

    @Test
    void testAddElement() {
        customLinkedList.add("Element1");
        assertEquals(1, customLinkedList.size());
        assertEquals("Element1", customLinkedList.get(0));
    }

    @Test
    void testAddNullThrowsException() {
        assertThrows(NullPointerException.class, () -> customLinkedList.add(null));
    }

    @Test
    void testGetElementByIndex() {
        customLinkedList.add("Element1");
        customLinkedList.add("Element2");

        assertEquals("Element1", customLinkedList.get(0));
        assertEquals("Element2", customLinkedList.get(1));
    }

    @Test
    void testRemoveElement() {
        customLinkedList.add("Element1");
        customLinkedList.add("Element2");

        customLinkedList.remove(1);
        assertEquals(1, customLinkedList.size());
        assertEquals("Element1", customLinkedList.get(0));
    }

    @Test
    void testAddAll() {
        List<String> elements = Arrays.asList("Element1", "Element2", "Element3");

        assertTrue(customLinkedList.addAll(elements));
        assertEquals(3, customLinkedList.size());
        assertEquals("Element1", customLinkedList.get(0));
        assertEquals("Element2", customLinkedList.get(1));
        assertEquals("Element3", customLinkedList.get(2));
    }

    @Test
    void testContainsElement() {
        customLinkedList.add("Element1");

        assertTrue(customLinkedList.contains("Element1"));
        assertFalse(customLinkedList.contains("Element2"));
    }

    @Test
    void testIsEmpty() {
        assertTrue(customLinkedList.isEmpty());

        customLinkedList.add("Element1");
        assertFalse(customLinkedList.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, customLinkedList.size());

        customLinkedList.add("Element1");
        assertEquals(1, customLinkedList.size());
    }

    @Test
    void testStream() {
        customLinkedList.add("Element1");
        customLinkedList.add("Element2");

        assertEquals(2, customLinkedList.stream().count());
    }
}