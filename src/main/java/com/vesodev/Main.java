package com.vesodev;

import com.vesodev.exception.NotEnoughFundsException;
import com.vesodev.model.*;

import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<ItemType, Double> percentageMap = Map.of(ItemType.FOOD, 50.0, ItemType.NON_FOOD, 5.0);
        Store store = new Store(1, "Test Store", percentageMap, 10, 2.0, 5);

        Client client = new Client(Double.MAX_VALUE);

        Cashier c = store.hireCashier("Cashier", 2000);

        CashDesk cashDesk = store.assignCashierToCashDesk(c, 0);

        for (int i = 0; i < 4000; i++) {
            store.addItem(new Item(i, "test", 10, LocalDate.now().plusDays(40), ItemType.FOOD));
        }


        cashDesk.startSale();
        for (int i = 0; i < 4000; i++) {
            try {
                cashDesk.scanItem(ItemType.FOOD, "test");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        Invoice i = null;
        try {
            i = cashDesk.endSale(client);
        } catch (NotEnoughFundsException e) {
            System.err.println(e.getMessage());
            cashDesk.cancelSale();
        }
        System.out.println(i);
        System.out.println("Income: " + store.getTotalIncome());
        System.out.println("Expenses: " + store.getTotalExpenses());
        System.out.println("Profit: " + store.getTotalProfit());
    }
}