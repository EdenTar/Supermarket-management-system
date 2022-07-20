package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class ProductOrderDTO extends DTO<PK> {

    private final long orderId;
    private final String catalogNum;
    private final String productName;
    private final long quantity;
    private final double totalPriceWithoutDiscount;
    private final double discount;
    private final double finalPrice;

    public ProductOrderDTO(long orderId, String catalogNum, String productName, long quantity, double totalPriceWithoutDiscount, double finalPrice, double discount) {
        super(new PK(getFields(), orderId, productName));
        this.orderId = orderId;
        this.catalogNum = catalogNum;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
        this.finalPrice = finalPrice;
        this.discount = discount;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"orderId", "productName"}, ProductOrderDTO.class);
    }

    public static PK getPK(long orderId, String productName) {
        return new PK(getFields(), orderId, productName);
    }

    public double getDiscount() {
        return discount;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    public String getProductName() {
        return productName;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{orderId, catalogNum, productName, quantity, totalPriceWithoutDiscount, discount, finalPrice};
    }
}



