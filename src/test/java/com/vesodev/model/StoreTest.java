package com.vesodev.model;

import com.vesodev.exception.ItemNotAvailableException;
import com.vesodev.exception.ItemNotSupportedException;
import com.vesodev.exception.NoSuchCashDeskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    private Store store;
    private Map<ItemType, Double> percentagesMap;

    @BeforeEach
    void setUp() {
        percentagesMap = Map.of(ItemType.FOOD, 0.10);
        store = new Store(1, "Test Store", percentagesMap, 30, 0.15, 3);
    }

    @Test
    void testGetPercentagePriceIncrease() {
        assertEquals(0.10, store.getPercentagePriceIncrease(ItemType.FOOD));
        assertThrows(ItemNotSupportedException.class, () -> store.getPercentagePriceIncrease(ItemType.NON_FOOD));
    }

    @Test
    void testAddAndGetItem() throws ItemNotAvailableException {
        Item item = new Item(123456, "Apple", 1.0, LocalDate.now(), ItemType.FOOD);
        store.addItem(item);

        Item retrievedItem = store.getItem(ItemType.FOOD, "Apple");
        assertEquals(item, retrievedItem);

        assertThrows(ItemNotAvailableException.class, () -> store.getItem(ItemType.FOOD, "Apple"));
    }

    @Test
    void testAddAndGetItems() throws ItemNotAvailableException {
        Item item1 = new Item(123456, "Apple", 1.0, LocalDate.now(), ItemType.FOOD);
        Item item2 = new Item(123456, "Apple", 1.0, LocalDate.now(), ItemType.FOOD);
        store.addItems(List.of(item1, item2));

        List<Item> items = store.getItems(ItemType.FOOD, "Apple", 2);
        assertEquals(2, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
    }

    @Test
    void testHireAndFireCashier() {
        Cashier cashier = store.hireCashier("Alice", 1000.0);
        assertEquals("Alice", cashier.getName());
        assertEquals(1000.0, cashier.getSalary());

        store.fireCashier(cashier);
        assertFalse(store.getCashDesks().stream().anyMatch(desk -> desk.getCurrentCashier() == cashier));
    }

    @Test
    void testAssignCashierToCashDesk() {
        Cashier cashier = store.hireCashier("Bob", 1200.0);
        CashDesk assignedDesk = store.assignCashierToCashDesk(cashier, 0);

        assertEquals(assignedDesk, cashier.getCurrentCashDesk());
        assertEquals(cashier, assignedDesk.getCurrentCashier());
        assertThrows(NoSuchCashDeskException.class, () -> store.assignCashierToCashDesk(cashier, 5));
    }

    @Test
    void testGetTotalExpenses() {
        Cashier cashier = store.hireCashier("Charlie", 1500.0);
        store.assignCashierToCashDesk(cashier, 0);
        Item item = new Item(123456, "Apple", 1.0, LocalDate.now(), ItemType.FOOD);
        store.addItem(item);

        assertEquals(1501, store.getTotalExpenses());  // 500 for item + 1500 for cashier salary
    }

    @Test
    void testGenerateInvoiceId() {
        int invoiceId1 = store.generateInvoiceId();
        int invoiceId2 = store.generateInvoiceId();
        assertEquals(invoiceId1 + 1, invoiceId2);
    }
}
