package Backend.Logic.LogicObjects.Product;

import Backend.Logic.LogicObjects.Transport.TransportItem;
import Obj.Pair;

import java.util.LinkedList;
import java.util.List;

public class Delivery {
    private int id;
    private List<TransportItem> transportItems;

    public Delivery(int id, List<TransportItem> transportItems){
        this.id = id;
        this.transportItems = transportItems;
    }

    public int getId() {
        return id;
    }

    public List<TransportItem> getTransportItems() {
        return transportItems;
    }
}
