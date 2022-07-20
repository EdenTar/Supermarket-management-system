package Backend.Logic.LogicObjects.Product;


import Backend.DataAccess.DAOs.StockDAOs.ItemsDAO;
import Backend.DataAccess.DAOs.StockDAOs.LocationsDAO;

import Backend.DataAccess.DTOs.StockDTOS.LocationsDTO;

import java.time.Instant;
import java.util.Date;

public class Item {

    private int id;

    private String product_number;
    private Location location;
    private Date expired;
    private boolean isFlaw;

    private ItemsDAO itemsDAO = new ItemsDAO();

    private LocationsDAO locationsDAO = new LocationsDAO();

    public Item(String product_number, int id, Location location, Date expired) {
        this.product_number = product_number;
        this.id = id;
        this.location = location;
        this.expired = expired;
        this.isFlaw = false;
    }

    public Item(String product_number, int id, Location location, Date expired, boolean isFlaw) {
        this.product_number = product_number;
        this.id = id;
        this.location = location;
        this.expired = expired;
        this.isFlaw = isFlaw;
    }

    public String getProduct_number() {
        return product_number;
    }

    public int getId(){return id;}

    public Location getLocation() {
        location = locationsDAO.getRow(LocationsDTO.getPK(id, Long.parseLong(product_number)));
        return location;
    }

    public void setLocation(Location location) {
        locationsDAO.deleteRow(getLocation());
        this.location = location;
        locationsDAO.insert(location);
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
        itemsDAO.update(this);
    }

    public boolean isExpired(){
        return Date.from(Instant.now()).after(expired);
    }

    public boolean isFlaw() {
        return isFlaw;
    }

    public void setFlaw(boolean flaw) {
        isFlaw = flaw;
        itemsDAO.update(this);
    }
}
