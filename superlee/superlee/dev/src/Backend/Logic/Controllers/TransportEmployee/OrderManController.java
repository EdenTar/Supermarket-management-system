package Backend.Logic.Controllers.TransportEmployee;

import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import java.util.List;

public class OrderManController{

    private final OrderTransportController orderTransportController;


    public OrderManController(OrderTransportController orderTransportController) {
       this.orderTransportController = orderTransportController;
    }

   /* public List<OrderMan> getList()
    {
        return orderManDAO.getRowsFromDB();
    }*/


    public int createTransportRequest(String origin, String destination, List<TransportItem> transportItemList,String supplierId) {
       // if (!isLoggedIn()) throw new RuntimeException("In order to make Transport request you must be logged in.");
        return orderTransportController.addTransportRequest(origin,destination,transportItemList,supplierId);
    }

    public void deleteTransportRequest(int transportRequestId) {
        //if (!isLoggedIn()) throw new RuntimeException("In order to delete Transport request you must be logged in.");
        orderTransportController.deleteTransportRequest(transportRequestId);

    }
    public OrderTransport getOrderTransport(int id){
        //if (!isLoggedIn()) throw new RuntimeException("In order to delete Transport request you must be logged in.");
        return orderTransportController.getOrderTransport(id);
    }

}
