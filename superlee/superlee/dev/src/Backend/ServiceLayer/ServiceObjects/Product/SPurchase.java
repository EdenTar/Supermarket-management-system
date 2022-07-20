package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Purchase;

import java.util.Date;

public class SPurchase {

    private double costPrice;
    private double salePrice;
    private double discount;
    private int quantity;
    private String supplier;
    private Date purchaseTime;

    public SPurchase(double costPrice, double salePrice, double discount, int quantity, String supplier, Date purchaseTime) {
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.discount = discount;
        this.quantity = quantity;
        this.supplier = supplier;
        this.purchaseTime = purchaseTime;
    }

    public SPurchase(Purchase purchase){
        this(purchase.getCostPrice(), purchase.getSalePrice(), purchase.getDiscount(), purchase.getQuantity(), purchase.getSupplier(), purchase.getPurchaseTime());
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    @Override
    public String toString() {
        return String.format("[Cost price: %,.2f, Sale price: %,.2f, Discount: %,.2f, Quantity: %d, Supplier: %s, Purchase time: %s]",
                getCostPrice(),getSalePrice(),getDiscount(),getQuantity(),getSupplier(),getPurchaseTime().toString());
    }
}
