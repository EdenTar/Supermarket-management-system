package Backend.DataAccess.DTOs.StockDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class ProductsDTO extends DTO<PK> {

    private final long id;
    private final String name;
    private final String manufacturer;
    private final double price;
    private final long category_ID;
    private final long demand_per_day;
    private final long item_id_runner;

    public ProductsDTO(long id, String name, String manufacturer, double price, long category_ID, long demandPerDay, long itemIdRunner) {
        super(new PK(getFields(), id));
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category_ID = category_ID;
        this.demand_per_day = demandPerDay;
        this.item_id_runner = itemIdRunner;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, ProductsDTO.class);
    }

    public static PK getPK(long id) {
        return new PK(getFields(), id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public long getCategoryId() {
        return category_ID;
    }

    public long getDemandPerDay() {
        return demand_per_day;
    }

    public long getItemIdRunner() {
        return item_id_runner;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{id, name, manufacturer, price, category_ID, demand_per_day, item_id_runner};
    }
}



