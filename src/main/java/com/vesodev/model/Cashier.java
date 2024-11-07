package com.vesodev.model;

public class Cashier extends IdentifiableObject {

    private final String name;

    private final double salary;

    private CashDesk currentCashDesk;

    public Cashier(int id, String name, double salary) {
        super(id);
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public CashDesk getCurrentCashDesk() {
        return currentCashDesk;
    }

    public void setCurrentCashDesk(CashDesk currentCashDesk) {
        this.currentCashDesk = currentCashDesk;
    }
}
