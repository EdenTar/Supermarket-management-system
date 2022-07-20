package PresentationLayer.EmployeTransportFrontend.Objects;

import Backend.ServiceLayer.ServiceObjects.Transport.TransportItemService;

public class PresentationItem {
    private String manufacture;
    private int quantity;

    public PresentationItem(String manufacture, int quantity) {
        this.manufacture = manufacture;
        this.quantity = quantity;
    }

    public PresentationItem(TransportItemService transportItem){
        this.manufacture = transportItem.getItemName();
        this.quantity = transportItem.getQuantity();;
    }



    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
