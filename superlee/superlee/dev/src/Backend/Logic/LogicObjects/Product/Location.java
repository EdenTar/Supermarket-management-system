package Backend.Logic.LogicObjects.Product;


import Backend.DataAccess.DAOs.StockDAOs.LocationsDAO;

public class Location {

    public enum StoreOrStorage{
        STORE,
        STORAGE
    }

    private String product_number;

    private int item_id;
    private String branch;
    private StoreOrStorage place;
    private int shelf;

    private LocationsDAO locationsDAO = new LocationsDAO();

    public Location(String product_number, int item_id, String branch, StoreOrStorage place, int shelf) {
        this.product_number = product_number;
        this.item_id = item_id;
        this.branch = branch;
        this.place = place;
        this.shelf = shelf;
    }


    public String getProduct_number() {
        return product_number;
    }

    public int getItem_id() {
        return item_id;
    }



    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
        locationsDAO.update(this);
    }

    public StoreOrStorage getPlace() {
        return place;
    }

    public void setPlace(StoreOrStorage place) {
        this.place = place;
        locationsDAO.update(this);
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;locationsDAO.update(this);
    }

    public boolean equals(Location location){
        return branch.equals(location.branch) && place.equals(location.place) && (shelf == location.shelf);
    }
}
