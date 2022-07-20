package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Item;

import java.util.Date;

public class SItem {


    private int id;
    private SLocation location;
    private Date expired;
    private boolean isFlaw;

    public SItem(int id, SLocation location, Date expired, boolean isFlaw) {
        this.id = id;
        this.location = location;
        this.expired = expired;
        this.isFlaw = isFlaw;
    }

    public SItem(Item item){
        this(item.getId(), new SLocation(item.getLocation()), item.getExpired(), item.isFlaw());
    }

    public int getId() {
        return id;
    }

    public SLocation getLocation() {
        return location;
    }

    public Date getExpired() {
        return expired;
    }

    public boolean isFlaw() {
        return isFlaw;
    }

    @Override
    public String toString(){
        return String.format("[ID: %d, Location: %s, Expired: %s, Is flaw: %s]", getId(), getLocation().toString(), getExpired().toString(), isFlaw());
    }
}
