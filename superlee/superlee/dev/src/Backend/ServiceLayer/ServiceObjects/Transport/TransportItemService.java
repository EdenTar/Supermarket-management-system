package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.LogicObjects.Transport.TransportItem;

public class TransportItemService {
    private final String itemName;
    private final int quantity;

    @Override
    public String toString() {
        return "item:\n " +
                "itemId=" + itemName +
                ", quantity=" + quantity +
                '\n';
    }


    public TransportItemService(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public TransportItemService(TransportItem transportItem) {
        this.itemName = transportItem.getName();
        this.quantity = transportItem.getQuantity();
    }

    public String getItemName() {
        return itemName;
    }


    public int getQuantity() {
        return quantity;
    }
}
