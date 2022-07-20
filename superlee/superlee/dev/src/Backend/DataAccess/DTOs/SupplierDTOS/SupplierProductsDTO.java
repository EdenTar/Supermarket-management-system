package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class SupplierProductsDTO extends DTO<PK> {

    private final String cn;
    private final String productName;
    private final double price;
    private final String catalogNum;

    public SupplierProductsDTO(String cn, String productName, double price, String catalogNum) {
        super(new PK(getFields(), cn, productName));
        this.cn = cn;
        this.productName = productName;
        this.price = price;
        this.catalogNum = catalogNum;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn", "productName"}, SupplierProductsDTO.class);
    }

    public static PK getPK(String cn, String productName) {
        return new PK(getFields(), cn, productName);
    }

    public String getCn() {
        return cn;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, productName, price, catalogNum};
    }
}



