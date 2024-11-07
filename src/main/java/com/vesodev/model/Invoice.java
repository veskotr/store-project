package com.vesodev.model;

import com.vesodev.util.DateUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Invoice extends IdentifiableObject {

    private final LocalDateTime issuedDateTime;

    private final Map<String, List<Item>> items;

    private final String issuerName;

    private final Store store;

    Invoice(int id, String issuerName, Store store) {
        super(id);
        this.issuerName = issuerName;
        this.issuedDateTime = LocalDateTime.now();
        this.items = new HashMap<>();
        this.store = store;
    }

    public double getInvoiceTotal() {
        double total = 0;
        for (List<Item> itemList : items.values()) {
            for (Item item : itemList) {
                total += item.calculateFinalPrice(store);
            }
        }
        return total;
    }

    public double getProfit() {
        double profit = 0;
        for (List<Item> itemList : items.values()) {
            for (Item item : itemList) {
                profit += item.calculateFinalPrice(store) - item.getWholeSalePrice();
            }
        }
        return profit;
    }

    public void writeInvoiceToFile() throws IOException {
        File file = new File("./" + getId() + issuedDateTime.toString());
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Could not create file " + file.getAbsolutePath());
            }
            PrintWriter writer = new PrintWriter(file);
            writer.println(this);
        }
    }

    public void addItem(Item item) {
        if (!items.containsKey(item.getName())) {
            items.put(item.getName(), new LinkedList<>());
        }
        items.get(item.getName()).add(item);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Invoice Number: ");
        builder.append(super.getId());
        builder.append("\nIssue date: ");
        builder.append(issuedDateTime.format(DateUtils.getDateTimeFormater()));
        builder.append("\nIssuer name: ");
        builder.append(issuerName);
        builder.append("\nItems: \n");
        for (String key : items.keySet()) {
            builder.append(key);
            builder.append(" ").append(items.get(key).get(0).calculateFinalPrice(store)).append(" ");
            builder.append(" x").append(items.get(key).size());
            builder.append(" ").append(items.get(key).stream().map(item -> item.calculateFinalPrice(store)).reduce(0.0, Double::sum));
            builder.append("\n");
        }
        builder.append("\nStore: ");
        builder.append(store.getName());
        builder.append("\nTotal: ");
        builder.append(String.format("%.2f", getInvoiceTotal()));
        return builder.toString();
    }

    public void commitSale() {
        items.values().forEach(itemsList -> itemsList.forEach(item -> item.sellItem(store)));
    }

    public void returnItems() {
        store.addItems(items.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }

    public Map<String, List<Item>> getItems() {
        return items;
    }
}
