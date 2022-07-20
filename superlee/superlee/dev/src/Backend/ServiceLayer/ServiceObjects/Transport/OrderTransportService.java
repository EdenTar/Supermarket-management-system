package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.LogicObjects.Transport.TransportItem;

import java.util.ArrayList;
import java.util.List;

public class OrderTransportService {
    private List<TransportItemService> transportItemList;
    private int orderTransportId;

    @Override
    public String toString() {
        return "order:\n" +
                "items=" + transportItemList +
                ", orderId=" + orderTransportId +
                '\n';
    }

    public String toStringWithoutItems() {
        return "order:\n" +
                "orderId=" + orderTransportId;
    }

    public OrderTransportService(OrderTransport orderTransport) {
        this.transportItemList = convertToServiceItemList(orderTransport.getTransportItemList());
        this.orderTransportId = orderTransport.getOrderTransportId();
    }

    private List<TransportItemService> convertToServiceItemList(List<TransportItem> transportItemList) {
        List<TransportItemService> itemServiceList = new ArrayList<>();
        for (TransportItem transportItem : transportItemList) {
            itemServiceList.add(new TransportItemService(transportItem));
        }
        return itemServiceList;
    }

    public List<TransportItemService> getTransportItemList() {
        return transportItemList;
    }

    public void setTransportItemList(List<TransportItemService> transportItemList) {
        this.transportItemList = transportItemList;
    }

    public int getOrderTransportId() {
        return orderTransportId;
    }

    public void setOrderTransportId(int orderTransportId) {
        this.orderTransportId = orderTransportId;
    }
}
