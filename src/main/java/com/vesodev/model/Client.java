package com.vesodev.model;

public class Client {
    private final double availableBalance;

    public Client(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
