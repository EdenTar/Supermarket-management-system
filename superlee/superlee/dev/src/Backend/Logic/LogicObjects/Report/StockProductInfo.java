package Backend.Logic.LogicObjects.Report;

import Backend.Logic.LogicObjects.Product.Location;
import Backend.Logic.LogicObjects.Product.Product;

import java.util.List;

public class StockProductInfo extends ProductInfo {
    private int quantity;
    private int storeQuantity;
    private int warehouseQuantity;
    private List<Location> locations;
    private boolean isMissing;

    public StockProductInfo(Product product) throws Exception {
        super(product);
        this.quantity = product.getQuantity();
        this.storeQuantity = product.getStoreQuantity();
        this.warehouseQuantity = product.getWarehouseQuantity();
        this.locations = product.getLocations();
        this.isMissing = product.isMissing();

    }

    public boolean isMissing() {return isMissing;}

    public int getQuantity() {
        return quantity;
    }

    public int getStoreQuantity() {
        return storeQuantity;
    }

    public int getWarehouseQuantity() {
        return warehouseQuantity;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
