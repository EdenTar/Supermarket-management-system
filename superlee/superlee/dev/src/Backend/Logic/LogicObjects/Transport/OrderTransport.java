package Backend.Logic.LogicObjects.Transport;

import Backend.DataAccess.DAOs.TransportDAOs.OrderTransportDAO;
import Backend.DataAccess.DAOs.TransportDAOs.TransportItemDAO;
import Backend.Logic.Points.Point;

import java.util.Date;
import java.util.List;

public class OrderTransport {
    private List<TransportItem> transportItemList;
    private int orderTransportId;
    private Point origin;
    private Point destination;
    private Date creationDate;//TODO add this
    private OrderTransportDAO orderDao;
    private TransportItemDAO itemDAO;
    private String supplierId;



    public OrderTransport(List<TransportItem> transportItemList, int orderTransportId,Point origin, Point destination,Date date,String supplierId) {
        this.transportItemList = transportItemList;
        this.orderTransportId = orderTransportId;
        this.origin = origin;
        this.destination = destination;
        this.supplierId=supplierId;
        this.creationDate= date!=null? date:new Date();
        this.orderDao=new OrderTransportDAO();
        this.itemDAO=new TransportItemDAO();
        setOwner(transportItemList);

    }
    public OrderTransport(int orderTransportId,Point origin, Point destination,Date creationDate,String supplierId) {
        this.transportItemList = null;
        this.orderTransportId = orderTransportId;
        this.origin = origin;
        this.destination = destination;
        this.creationDate= creationDate;
        this.supplierId=supplierId;
        this.orderDao=new OrderTransportDAO();
        this.itemDAO=new TransportItemDAO();

    }

    private void setOwner(List<TransportItem> transportItemList)
    {
        for(TransportItem transportItem:transportItemList)
            transportItem.setOrderTransportId(orderTransportId);
    }
    public void saveItems()
    {
        for(TransportItem transportItem:transportItemList)
            itemDAO.insert(transportItem);
    }
    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {

        this.origin = origin;
        update();
    }
    public List<TransportItem> getTransportItemList() {
        if(transportItemList==null)
            uploadItems();
        return transportItemList;
    }
    public void delete()
    {
        getTransportItemList().forEach(x->itemDAO.deleteRow(x));
    }


    public int getOrderTransportId() {
        return orderTransportId;
    }

    public void setOrderTransportId(int orderTransportId) {

        this.orderTransportId = orderTransportId;
        update();
    }
    public void specialSetOrderTransportId(int orderTransportId) {
        this.orderTransportId = orderTransportId;
    }
    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {

        this.destination = destination;
        update();
    }
    private void update()
    {
        orderDao.update(this);
    }
    private void uploadItems()
    {
        this.transportItemList= itemDAO.getItemForOrderTransport(orderTransportId);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getSupplierId() {
        return supplierId;
    }
}
