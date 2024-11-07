package com.vesodev.model;

import com.vesodev.exception.ItemExpiredException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item extends IdentifiableObject {
    private final String name;
    private final double wholeSalePrice;
    private final LocalDate expirationDate;
    private final ItemType itemType;
    private Double calculatedFinalPrice = null;
    private ItemStatus itemStatus;

    public Item(int id, String name, double wholeSalePrice, LocalDate expirationDate, ItemType itemType) {
        super(id);
        this.name = name;
        this.wholeSalePrice = wholeSalePrice;
        this.expirationDate = expirationDate;
        this.itemType = itemType;
        this.itemStatus = ItemStatus.UNSOLD;
    }

    public String getName() {
        return name;
    }

    public double getWholeSalePrice() {
        return wholeSalePrice;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public double calculateFinalPrice(Store store) throws ItemExpiredException {
        if (this.isExpired()) {
            throw new ItemExpiredException(this);
        }
        if (itemStatus == ItemStatus.SOLD) {
            return calculatedFinalPrice;
        }

        //calculate price markup
        double percentageIncrease = store.getPercentagePriceIncrease(this.getItemType());
        double finalPrice = this.getWholeSalePrice() * (1.0 + percentageIncrease / 100.0);

        //calculate expiration discount
        long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), this.getExpirationDate());
        if (daysToExpire < store.getExpirationThresholdDays()) {
            finalPrice = finalPrice * (store.getExpirationDiscountPercentage() / 100.0);
        }

        return finalPrice;
    }

    public void sellItem(Store store) throws ItemExpiredException {
        calculatedFinalPrice = calculateFinalPrice(store);
        itemStatus = ItemStatus.SOLD;
    }
}
