package Backend.ServiceLayer.ServiceObjects.Report;

import Backend.Logic.LogicObjects.Report.StockProductInfo;
import Backend.ServiceLayer.ServiceObjects.Product.SLocation;

import java.util.List;
import java.util.stream.Collectors;

public class SStockProductInfo extends SProductInfo{


    private int quantity;
    private int storeQuantity;
    private int warehouseQuantity;
    private List<SLocation> locations;
    private boolean isMissing;


    public SStockProductInfo(String productNumber, String name, String manufacturer, List<SLocation> locations, int quantity, int storeQuantity, int warehouseQuantity, boolean isMissing) {
        super(productNumber, name, manufacturer);
        this.locations = locations;
        this.quantity = quantity;
        this.storeQuantity = storeQuantity;
        this.warehouseQuantity = warehouseQuantity;
        this.isMissing = isMissing;
    }

    public SStockProductInfo(StockProductInfo productInfo) {
        this(productInfo.getProductNumber(), productInfo.getName(), productInfo.getManufacturer(),
                productInfo.getLocations().stream().map(l -> new SLocation(l)).collect(Collectors.toList()),
                productInfo.getQuantity(), productInfo.getStoreQuantity(), productInfo.getWarehouseQuantity(), productInfo.isMissing());
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStoreQuantity() {
        return storeQuantity;
    }

    public int getWarehouseQuantity() {
        return warehouseQuantity;
    }

    public List<SLocation> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %d, %d, %d]\n\n[Locations: %s]",
                getProductNumber(), getName(), getManufacturer(), getQuantity(), getStoreQuantity(), getWarehouseQuantity(), printList(getLocations()));
    }

    private  <T> String printList(List<T> lst){
        String res = "<";
        int i = 0;
        for(T t : lst) {
            res += t.toString() + ((i++ == lst.size()-1) ? "" : ",");
        }
        return res + ">";
    }
}
