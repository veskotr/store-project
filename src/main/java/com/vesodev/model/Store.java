package com.vesodev.model;

import com.vesodev.exception.ItemNotAvailableException;
import com.vesodev.exception.ItemNotSupportedException;
import com.vesodev.exception.NoSuchCashDeskException;

import java.util.*;

public class Store extends IdentifiableObject {

    private final Map<ItemType, Double> percentagesMap;

    private final int expirationThresholdDays;

    private final double expirationDiscountPercentage;

    private final String name;

    private final Map<ItemType, Map<String, Queue<Item>>> inventory;

    private final List<Cashier> cashiers;

    private final List<CashDesk> cashDesks;

    private int totalInvoices = 0;

    public Store(int id, String name, Map<ItemType, Double> percentagesMap, int expirationThresholdDays, double expirationDiscountPercentage, int numberOfCashDesks) {
        super(id);
        this.percentagesMap = percentagesMap;
        this.expirationThresholdDays = expirationThresholdDays;
        this.expirationDiscountPercentage = expirationDiscountPercentage;
        this.inventory = new HashMap<>();
        this.cashiers = new LinkedList<>();
        this.cashDesks = new LinkedList<>();
        this.initCashDesks(numberOfCashDesks);
        this.name = name;
    }

    public Double getPercentagePriceIncrease(ItemType itemType) {
        if (!percentagesMap.containsKey(itemType)) {
            throw new ItemNotSupportedException(itemType);
        }
        return percentagesMap.get(itemType);
    }

    public int getExpirationThresholdDays() {
        return expirationThresholdDays;
    }

    public double getExpirationDiscountPercentage() {
        return expirationDiscountPercentage;
    }

    public void addItem(Item item) {
        if (!inventory.containsKey(item.getItemType())) {
            inventory.put(item.getItemType(), new HashMap<>());
        }
        if (!inventory.get(item.getItemType()).containsKey(item.getName())) {
            inventory.get(item.getItemType()).put(item.getName(), new LinkedList<>());
        }
        inventory.get(item.getItemType()).get(item.getName()).add(item);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public Item getItem(ItemType itemType, String itemName) throws ItemNotAvailableException {
        if (!inventory.containsKey(itemType)) {
            throw new ItemNotAvailableException(itemName);
        }
        if (!inventory.get(itemType).containsKey(itemName) || inventory.get(itemType).get(itemName).isEmpty()) {
            throw new ItemNotAvailableException(itemName);
        }
        Item i = inventory.get(itemType).get(itemName).remove();

        return i;
    }

    public List<Item> getItems(ItemType itemType, String itemName, int count) throws ItemNotAvailableException {
        List<Item> items = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            items.add(getItem(itemType, itemName));
        }
        return items;
    }

    public Cashier hireCashier(String cashierName, double cashierSalary) {
        Cashier cashier = new Cashier(cashiers.size(), cashierName, cashierSalary);
        cashiers.add(cashier);
        return cashier;
    }

    public void fireCashier(Cashier cashier) {
        cashiers.remove(cashier);
    }

    private void initCashDesks(int numberOfCashDesks) {
        for (int i = 0; i < numberOfCashDesks; i++) {
            cashDesks.add(new CashDesk(i, this));
        }
    }

    public double getStaffExpenses() {
        double expenses = 0;
        for (Cashier cashier : cashiers) {
            expenses += cashier.getSalary();
        }
        return expenses;
    }

    public String getName() {
        return name;
    }

    public int generateInvoiceId() {
        return ++totalInvoices;
    }

    public CashDesk assignCashierToCashDesk(Cashier cashier, int deskId) {
        if (deskId >= cashDesks.size()) {
            throw new NoSuchCashDeskException();
        }
        CashDesk cashDesk = cashDesks.get(deskId);
        cashier.setCurrentCashDesk(cashDesk);
        cashDesk.setCurrentCashier(cashier);
        return cashDesk;
    }

    public CashDesk getCashDesk(int deskId) {
        return cashDesks.get(deskId);
    }

    public double getTotalIncome() {
        double total = 0;
        for (CashDesk cashDesk : cashDesks) {
            total += cashDesk.getCashDeskIncome();
        }
        return total;
    }

    public double getTotalExpenses() {
        double total = 0;
        for (Item item : inventory.values().stream().flatMap(item -> item.values().stream().flatMap(Collection::stream)).toList()) {
            total += item.getWholeSalePrice();
        }
        for (Cashier cashier : cashiers) {
            total += cashier.getSalary();
        }
        return total;
    }

    public double getTotalProfit() {
        double total = 0;
        for (CashDesk cashDesk : cashDesks) {
            total += cashDesk.getCashDeskProfit();
        }
        return total - getTotalExpenses();
    }

    public List<CashDesk> getCashDesks() {
        return cashDesks;
    }
}
