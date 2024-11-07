package com.vesodev.model;

import com.vesodev.exception.NoCashierAssignedException;
import com.vesodev.exception.NotEnoughFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CashDeskTest {

    private Store store;
    private CashDesk cashDesk;
    private Cashier cashier;
    private Client client;

    @BeforeEach
    void setUp() {
        store = mock(Store.class);
        cashDesk = new CashDesk(1, store);
        cashier = new Cashier(1, "John Doe", 1200.0);
        client = mock(Client.class);
    }

    @Test
    void testSetAndGetCurrentCashier() {
        cashDesk.setCurrentCashier(cashier);
        assertEquals(cashier, cashDesk.getCurrentCashier());
    }

    @Test
    void testStartSaleWithoutCashier() {
        assertThrows(NoCashierAssignedException.class, cashDesk::startSale);
    }

    @Test
    void testStartSaleWithCashier() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();
        assertNotNull(cashDesk.issueInvoice());
    }

    @Test
    void testEndSaleWithInsufficientFunds() {
        Invoice invoice = cashDesk.issueInvoice();

        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();

        when(client.getAvailableBalance()).thenReturn(50.0);

        when(invoice.getInvoiceTotal()).thenReturn(100.0);

        when(store.generateInvoiceId()).thenReturn(1);

        assertThrows(NotEnoughFundsException.class, () -> cashDesk.endSale(client));
    }

    @Test
    void testEndSaleWithSufficientFunds() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();
        when(client.getAvailableBalance()).thenReturn(200.0);

        Invoice invoice = cashDesk.issueInvoice();
        when(invoice.getInvoiceTotal()).thenReturn(100.0);

        cashDesk.endSale(client);
        assertTrue(cashDesk.getCashDeskIncome() > 0);
    }

    @Test
    void testCancelSale() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();
        Invoice invoice = cashDesk.issueInvoice();
        cashDesk.cancelSale();
        assertNull(cashDesk.issueInvoice());
    }

    @Test
    void testScanItem() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();

        Item item = new Item(123456, "Apple", 1.0, LocalDate.now(), ItemType.FOOD);
        when(store.getItem(ItemType.FOOD, "Apple")).thenReturn(item);

        cashDesk.scanItem(ItemType.FOOD, "Apple");
        assertTrue(cashDesk.issueInvoice().getItems().get(item.getName()).contains(item));
    }

    @Test
    void testGetCashDeskIncome() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();

        Invoice invoice = cashDesk.issueInvoice();
        when(invoice.getInvoiceTotal()).thenReturn(150.0);

        cashDesk.endSale(client);
        assertEquals(150.0, cashDesk.getCashDeskIncome());
    }

    @Test
    void testGetCashDeskProfit() {
        cashDesk.setCurrentCashier(cashier);
        cashDesk.startSale();

        Invoice invoice = cashDesk.issueInvoice();
        when(invoice.getProfit()).thenReturn(50.0);

        cashDesk.endSale(client);
        assertEquals(50.0, cashDesk.getCashDeskProfit());
    }
}
