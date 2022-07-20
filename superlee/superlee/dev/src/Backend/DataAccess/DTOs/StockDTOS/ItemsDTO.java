package Backend.DataAccess.DTOs.StockDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class ItemsDTO extends DTO<PK> {

    private final long id;
    private final long product_ID;
    private final String is_flaw;
    private final String expired;

    public ItemsDTO(long id, long productId, String isFlaw, String expired) {
        super(new PK(getFields(), id, productId));
        this.id = id;
        this.product_ID = productId;
        this.is_flaw = isFlaw;
        this.expired = expired;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id", "product_ID"}, ItemsDTO.class);
    }

    public static PK getPK(long id, long product_ID) {
        return new PK(getFields(), id, product_ID);
    }

    public Object[] getValues() {
        return new Object[]{id, product_ID, is_flaw, expired};
    }


    public long getId() {
        return id;
    }

    public long getProductId() {
        return product_ID;
    }

    public String getIsFlaw() {
        return is_flaw;
    }

    public String getExpired() {
        return expired;
    }

}



