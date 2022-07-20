package Backend.DataAccess.DTOs.StockDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class DiscountsDTO extends DTO<PK> {

    private final String id;
    private final Long category_ID;

    private final Long product_ID;
    private final double discount;
    private final String date_from;
    private final String date_to;


    public DiscountsDTO(String id, Long categoryId, Long productId, double discount, String dateFrom, String dateTo) {
        super(new PK(getFields(), id));
        this.id = id;
        this.category_ID = categoryId;
        this.product_ID = productId;
        this.discount = discount;
        this.date_from = dateFrom;
        this.date_to = dateTo;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, DiscountsDTO.class);
    }

    public static PK getPK(String id) {
        return new PK(getFields(), id);
    }

    public String getId() {
        return id;
    }

    public Long getCategoryId() {
        return category_ID;
    }

    public Long getProductId() {
        return product_ID;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDateFrom() {
        return date_from;
    }

    public String getDateTo() {
        return date_to;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{id, category_ID, product_ID, discount, date_from, date_to};
    }
}



