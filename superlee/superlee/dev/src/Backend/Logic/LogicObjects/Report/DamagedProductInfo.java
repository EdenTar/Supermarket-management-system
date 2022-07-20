package Backend.Logic.LogicObjects.Report;

import Backend.Logic.LogicObjects.Product.Location;
import Backend.Logic.LogicObjects.Product.Product;

import java.util.List;

public class DamagedProductInfo extends ProductInfo{
    private List<Location> locations;
    private int quantity;
    private int expiredQuantity;
    private int flawedQuantity;


    public DamagedProductInfo(Product product) throws Exception {
        super(product);
        quantity = product.getQuantity();
        expiredQuantity = product.getExpiredQuantity();
        flawedQuantity = product.getFlawedQuantity();
        locations = product.getLocationOfDamagedItems();
    }

    public List<Location> getLocations() {
        return locations;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getExpiredQuantity() {
        return expiredQuantity;
    }

    public int getFlawedQuantity() {
        return flawedQuantity;
    }
}
