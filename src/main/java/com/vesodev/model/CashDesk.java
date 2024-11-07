package com.vesodev.model;

import com.vesodev.exception.NoCashierAssignedException;
import com.vesodev.exception.NotEnoughFundsException;

import java.util.LinkedList;
import java.util.List;

public class CashDesk extends IdentifiableObject {

    private Cashier currentCashier;

    private final List<Invoice> issuedInvoices;

    private final Store store;

    private Invoice currentInvoice;

    public CashDesk(int id, Store store) {
        super(id);
        this.store = store;
        issuedInvoices = new LinkedList<>();
    }

    public Cashier getCurrentCashier() {
        return currentCashier;
    }

    public void setCurrentCashier(Cashier currentCashier) {
        this.currentCashier = currentCashier;
    }

    public double getCashDeskIncome() {
        double cashDeskIncome = 0;
        for (Invoice invoice : issuedInvoices) {
            cashDeskIncome += invoice.getInvoiceTotal();
        }
        return cashDeskIncome;
    }

    public double getCashDeskProfit() {
        double cashDeskProfit = 0;
        for (Invoice invoice : issuedInvoices) {
            cashDeskProfit += invoice.getProfit();
        }
        return cashDeskProfit;
    }

    public Invoice issueInvoice() {
        return new Invoice(store.generateInvoiceId(), currentCashier.getName(), store);
    }

    public void startSale() {
        if (currentCashier == null) {
            throw new NoCashierAssignedException();
        }
        currentInvoice = new Invoice(store.generateInvoiceId(), currentCashier.getName(), store);
    }

    public Invoice endSale(Client client) throws NotEnoughFundsException {
        if (client.getAvailableBalance() < currentInvoice.getInvoiceTotal()) {
            throw new NotEnoughFundsException();
        }
        currentInvoice.commitSale();
        issuedInvoices.add(currentInvoice);
        return currentInvoice;
    }

    public void cancelSale() {
        currentInvoice.returnItems();
        currentInvoice = null;
    }

    public void scanItem(ItemType itemType, String itemName) {
        Item item = store.getItem(itemType, itemName);
        currentInvoice.addItem(item);

    }

}
