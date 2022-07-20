package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.TransportDAOs.OrderTransportDAO;
import Backend.DataAccess.DTOs.TransportDTOS.OrderTransportDTO;
import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.Logic.Points.TransportMap;
import Backend.Logic.Points.Zone;
import Backend.Logic.LogicLambdas.AddExist;
import Backend.Logic.Exceptions.Transport.TransportException;

import java.util.*;

public class OrderTransportController {
    int currentOrderTransportId;

    private TransportMap transportMap;
    private OrderTransportDAO orderDao;

    public OrderTransportController(TransportMap map) {
        this.transportMap = map;
        this.orderDao=new OrderTransportDAO();
        this.currentOrderTransportId =uploadCurrentOrderId()+1;


    }


    public AddExist getAddExistLambda() {
        return this::addExistTransportRequest;
    }

    public void addExistTransportRequest(OrderTransport orderTransport) {
        orderTransport.specialSetOrderTransportId(currentOrderTransportId++);
        for(TransportItem t:orderTransport.getTransportItemList())
            t.setOrderTransportId(orderTransport.getOrderTransportId());
        orderDao.insert(orderTransport);
    }

    public int addTransportRequest(String origin, String destination, List<TransportItem> transportItems,String supplierId) {

        OrderTransport orderTransport=new OrderTransport(transportItems, currentOrderTransportId++,
                transportMap.getPoint(origin), transportMap.getPoint(destination),new Date(),supplierId );
        orderDao.insert(orderTransport);
        orderTransport.saveItems();
        return orderTransport.getOrderTransportId();
    }
    public void addTransportRequestT(String origin, String destination, List<TransportItem> transportItems,String supplierId) {//For test Only!!!!!!

        OrderTransport orderTransport=new OrderTransport(transportItems, currentOrderTransportId,
                transportMap.getPoint(origin), transportMap.getPoint(destination),new Date(),supplierId );
        orderDao.insert(orderTransport);
        orderTransport.saveItems();
    }

    public void deleteTransportRequest(int transportRequestId) {
            OrderTransport orderTransport= orderDao.getRow(OrderTransportDTO.getPK(transportRequestId));
            if (orderTransport==null) throw new TransportException("There is no transport Request with this name");
            orderDao.deleteRow(orderTransport);
            orderTransport.delete();
    }

    public List<OrderTransport> showAllTransportRequests() {
        List<OrderTransport> list=orderDao.getRowsFromDB();
        if (list.isEmpty()) throw new RuntimeException("No available orders to show");
        list.sort(Comparator.comparing(OrderTransport::getCreationDate));
        return list;
    }

    public List<OrderTransport> getTransportRequests(List<Integer> orderId) {


        List<OrderTransport> transports =orderDao.getOrderTransportById(orderId);
        if (transports.size() != orderId.size()) throw new TransportException("There is a Problem with the order Ids");
        return transports;
    }

    public OrderTransport getRequestsByPriority() {
        OrderTransport orderTransport =orderDao.getOldestOrder();
        if (orderTransport==null) throw new RuntimeException("No available orders to show");
        return orderTransport;
    }

    public List<OrderTransport> getRequestsByZone(Zone fromZone, Zone toZone) {
        List<OrderTransport> list= orderDao.getReqByZone(fromZone.name(),toZone.name());
        return list;
    }

    public OrderTransport getOrderTransport(int id) {

        OrderTransport t = orderDao.getRow(OrderTransportDTO.getPK(id));
        if (t == null) throw new TransportException("The order is not exist or already finished");
        return t;
    }
    private int uploadCurrentOrderId()
    {
        return orderDao.getCurrentId();
    }

}
