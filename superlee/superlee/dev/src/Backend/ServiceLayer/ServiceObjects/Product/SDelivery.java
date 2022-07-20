package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Delivery;
import Backend.ServiceLayer.ServiceObjects.Transport.TransportItemService;

import java.util.List;
import java.util.stream.Collectors;

public class SDelivery {
    private int id;
    private List<TransportItemService> transportItemServices;
    public SDelivery(Delivery delivery){
        this.id = delivery.getId();
        this.transportItemServices = delivery.getTransportItems().stream().map(TransportItemService::new).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public List<TransportItemService> getTransportItemServices() {
        return transportItemServices;
    }
}
