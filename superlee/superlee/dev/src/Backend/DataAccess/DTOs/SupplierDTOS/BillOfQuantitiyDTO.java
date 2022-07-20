package Backend.DataAccess.DTOs.SupplierDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class BillOfQuantitiyDTO extends DTO<PK> {

    private final String cn;
    private final String productName;
    private final double discount;
    private final long startRange;


    public BillOfQuantitiyDTO(String cn, String productName, double discount, long startRange) {
        super(new PK(getFields(), cn, productName, startRange));
        this.cn = cn;
        this.productName = productName;
        this.discount = discount;
        this.startRange = startRange;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn", "productName", "startRange"}, BillOfQuantitiyDTO.class);
    }

    public static PK getPK(String cn, String productName, long startRange) {
        return new PK(getFields(), cn, productName, startRange);
    }

    public String getCn() {
        return cn;
    }

    public String getProductName() {
        return productName;
    }

    public double getDiscount() {
        return discount;
    }

    public long getStartRange() {
        return startRange;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, productName, discount, startRange};

    }
}



