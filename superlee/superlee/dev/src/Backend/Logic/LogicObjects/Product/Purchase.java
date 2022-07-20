package Backend.Logic.LogicObjects.Product;


import java.util.Date;

public class Purchase {


    private String productId;
    private double costPrice;
    private double salePrice;
    private double discount;
    private int quantity;
    private String supplier;
    private Date purchaseTime;

    public Purchase(String productId, double costPrice, double salePrice, double discount, int quantity, String supplier, Date purchaseTime) throws Exception {
        checkValidity(costPrice <= 0, "cost price can't be negative");
        this.costPrice = costPrice;
        checkValidity(salePrice <= 0, "sale price can't be negative");
        this.salePrice = salePrice;
        checkValidity(discount < 0 || discount > 100 , "discount can't be negative or over 100%");
        this.discount = discount;
        checkValidity(quantity <= 0, "quantity can't be negative");
        this.quantity = quantity;
        this.supplier = supplier;
        this.purchaseTime = purchaseTime;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
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

    private void checkValidity(boolean exp, String errorMessage) throws Exception {
        if(exp){
            throw new Exception(errorMessage);
        }
    }
}
