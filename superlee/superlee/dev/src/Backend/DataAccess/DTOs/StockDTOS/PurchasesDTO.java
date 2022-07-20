package Backend.DataAccess.DTOs.StockDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class PurchasesDTO extends DTO<PK> {

    private final long product_ID;
    private final double cost_price;
    private final double sale_price;
    private final double discount;
    private final long quantity;
    private final String supplier;
    private final String purchase_time;

    public PurchasesDTO(long productId, double costPrice, double salePrice, double discount, long quantity, String supplier, String purchaseTime) {
        super(new PK(getFields(), productId, purchaseTime));
        this.product_ID = productId;
        this.cost_price = costPrice;
        this.sale_price = salePrice;
        this.discount = discount;
        this.quantity = quantity;
        this.supplier = supplier;
        this.purchase_time = purchaseTime;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"product_ID", "purchase_time"}, PurchasesDTO.class);
    }

    public static PK getPK(long product_ID, String purchase_time) {
        return new PK(getFields(), product_ID, purchase_time);
    }

    public long getProductId() {
        return product_ID;
    }

    public double getCostPrice() {
        return cost_price;
    }

    public double getSalePrice() {
        return sale_price;
    }

    public double getDiscount() {
        return discount;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getPurchaseTime() {
        return purchase_time;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{product_ID, cost_price, sale_price, discount, quantity, supplier, purchase_time};
    }
}



