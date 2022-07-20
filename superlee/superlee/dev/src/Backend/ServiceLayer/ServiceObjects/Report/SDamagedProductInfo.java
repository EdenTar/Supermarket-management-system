package Backend.ServiceLayer.ServiceObjects.Report;

import Backend.Logic.LogicObjects.Report.DamagedProductInfo;
import Backend.ServiceLayer.ServiceObjects.Product.SLocation;

import java.util.List;
import java.util.stream.Collectors;

public class SDamagedProductInfo extends SProductInfo{

    private List<SLocation> locations;
    private int quantity;
    private int expiredQuantity;
    private int flawedQuantity;


    public SDamagedProductInfo(String productNumber, String name, String manufacturer, List<SLocation> locations, int quantity, int expiredQuantity, int flawedQuantity) {
        super(productNumber, name, manufacturer);
        this.locations = locations;
        this.quantity = quantity;
        this.expiredQuantity = expiredQuantity;
        this.flawedQuantity = flawedQuantity;
    }

    public SDamagedProductInfo(DamagedProductInfo productInfo) {
        this(productInfo.getProductNumber(), productInfo.getName(), productInfo.getManufacturer(),
                productInfo.getLocations().stream().map(l -> new SLocation(l)).collect(Collectors.toList()), productInfo.getQuantity(), productInfo.getExpiredQuantity(), productInfo.getFlawedQuantity());
    }

    public List<SLocation> getLocations() {
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

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %d, %d, %d]\n\n[Locations: %s]",
                getProductNumber(), getName(), getManufacturer(), getQuantity(), getExpiredQuantity(), getFlawedQuantity(), printList(getLocations()));
    }

    public String flawedToString(){
        return (flawedQuantity != 0) ? String.format("[%s, %s, %s, %d, %d]\n\n[Locations: %s]",
                getProductNumber(), getName(), getManufacturer(), getQuantity(), getFlawedQuantity(), printList(getLocations())) : "";
    }

    public String expiredToString(){
        return (expiredQuantity != 0) ? String.format("[%s, %s, %s, %d, %d]\n\n[Locations: %s]",
                getProductNumber(), getName(), getManufacturer(), getQuantity(), getExpiredQuantity(), printList(getLocations())) : "";
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

