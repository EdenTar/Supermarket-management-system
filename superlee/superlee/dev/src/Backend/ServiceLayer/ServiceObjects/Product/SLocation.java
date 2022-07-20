package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Location;

public class SLocation {

    private String branch;
    private String place;
    private int shelf;

    public SLocation(String branch, String place, int shelf) {
        this.branch = branch;
        this.place = place;
        this.shelf = shelf;
    }

    public SLocation(Location location){
        this(location.getBranch(), location.getPlace().name(),location.getShelf());
    }

    public String getBranch() {
        return branch;
    }

    public String getPlace() {
        return place;
    }

    public int getShelf() {
        return shelf;
    }

    @Override
    public String toString(){
        return String.format("[Branch: %s, Place: %s, Shelf: %d]",getBranch(), getPlace(),getShelf());
    }
}
