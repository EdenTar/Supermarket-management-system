package Backend.DataAccess.DTOs.StockDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class LocationsDTO extends DTO<PK> {
    private final long item_ID;
    private final long product_ID;
    private final String branch;
    private final String place;
    private final long shelf;

    public LocationsDTO(long itemId, long productId, String branch, String place, long shelf) {
        super(new PK(getFields(), itemId, productId));
        this.item_ID = itemId;
        this.product_ID = productId;
        this.branch = branch;
        this.place = place;
        this.shelf = shelf;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"item_ID", "product_ID"}, LocationsDTO.class);
    }

    public static PK getPK(long item_ID, long product_ID) {
        return new PK(getFields(), item_ID, product_ID);
    }


    public long getItemId() {
        return item_ID;
    }

    public long getProductId() {
        return product_ID;
    }

    public String getBranch() {
        return branch;
    }

    public String getPlace() {
        return place;
    }

    public long getShelf() {
        return shelf;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{item_ID, product_ID, branch, place, shelf};
    }
}



