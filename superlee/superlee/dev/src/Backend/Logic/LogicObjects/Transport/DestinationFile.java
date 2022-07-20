package Backend.Logic.LogicObjects.Transport;

import Backend.DataAccess.DAOs.TransportDAOs.DestinationFileDAO;
import Backend.DataAccess.DAOs.TransportDAOs.TransportItemDAO;
import Backend.Logic.LogicLambdas.AcceptDelivery;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Points.Point;
import Backend.Logic.LogicLambdas.AddExist;
import Backend.Logic.Exceptions.Transport.TransportException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DestinationFile {
    private final String id;
    private final Point destination;
    private final Point source;
    private boolean isDone;
    private final Date arrivalDate;
    private final Date basedOnCreationTime;
    private final String supplierID;
    private final int transportId;
    private final DestinationFileDAO destinationFileDAO;
    private final TransportItemDAO transportItemDAO;


    private List<TransportItem> transportItems;
    private final AddExist addExistLambda;

    public DestinationFile(String id, Point destination, Point source, Date arrivalDate, Date basedOnCreationTime,int transportId,boolean isDone,String supplierID) {
        this.id = id;
        this.destination = destination;
        this.source = source;
        this.isDone = isDone;
        this.transportItems = null;
        this.addExistLambda = null;
        this.arrivalDate=arrivalDate;
        this.basedOnCreationTime = basedOnCreationTime;
        this.supplierID=supplierID;
        this.destinationFileDAO=new DestinationFileDAO();
        this.transportItemDAO=new TransportItemDAO();
        this.transportId=transportId;
    }

    public DestinationFile(int transportId, OrderTransport orderTransport, AddExist addExistLambda, Date arrivalDate, Date basedOnCreationTime,String supplierID) {
        this.id = transportId + "-" + orderTransport.getOrderTransportId();
        this.destination = orderTransport.getDestination();
        this.source = orderTransport.getOrigin();
        this.isDone = false;
        this.transportItems = orderTransport.getTransportItemList();
        this.addExistLambda = addExistLambda;
        this.arrivalDate=arrivalDate;
        this.basedOnCreationTime = basedOnCreationTime;
        this.supplierID=supplierID;
        this.destinationFileDAO=new DestinationFileDAO();
        this.transportItemDAO=new TransportItemDAO();
        this.transportId=transportId;
    }
    public List<TransportItem> getTransportItems() {
            upload();
        return transportItems;
    }
    public void setOwner()
    {
        for(TransportItem t:transportItems)
            t.setDestinationFileId(id);
    }
    public OrderTransport getOrderTransport(List<Pair<String, Integer>> toRemove) {
            upload();
        List<TransportItem> rest = new LinkedList<>();
        try {
            for (Pair<String, Integer> product : toRemove) {
                TransportItem t = transportItems.stream().filter(x -> Objects.equals(x.getName(), product.getFirst())).findFirst().orElse(null);
                if (t == null) throw new TransportException("illegal item id");
                if (product.getSecond() >= t.getQuantity()) {
                    rest.add(new TransportItem( t.getName(), t.getQuantity(),0,""));
                    t.setQuantity(0);
                } else {
                    rest.add(new TransportItem( t.getName(), product.getSecond(),0,""));
                    t.setQuantity(t.getQuantity() - product.getSecond());
                }
            }
            return new OrderTransport(rest, 0, source, destination, getBasedOnCreationTime(),supplierID);

        } catch (TransportException e) {
            for (TransportItem r : rest) {
                TransportItem t = transportItems.stream().filter(x -> Objects.equals(x.getName(), r.getName())).findFirst().orElse(null);
                assert t != null;
                t.setQuantity(t.getQuantity() + r.getQuantity());
            }
            throw e;
        } finally {
            transportItems = transportItems.stream().filter(x -> x.getQuantity() != 0).collect(Collectors.toList());
        }

    }

    public void delete() {
        if (isDone()) throw new TransportException("The destination file is already done");
        upload();
        addExistLambda.addExist(new OrderTransport(transportItems, 0, source, destination, getBasedOnCreationTime(),supplierID));

    }
    public void deleteT() {//Not for use ! Only For Unit tests
        upload();
       for(TransportItem transportItem:transportItems)
           transportItemDAO.deleteRow(transportItem);

    }
    public void removeItems(List<Pair<String, Integer>> toRemove) {
        if (isDone()) throw new TransportException("The destination file is already done");
        addExistLambda.addExist(getOrderTransport(toRemove));
    }

    public String getId() {
        return id;
    }

    public Point getDestination() {
        return destination;
    }

    public Point getSource() {
        return source;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done, AcceptDelivery acceptDelivery) {
        if (this.isDone) throw new TransportException("the destination file is already done");
        isDone = done;
        if(destination.getAddress().equals("Branch1"))
            acceptDelivery.acceptDelivery(this.transportItems);
        destinationFileDAO.update(this);
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getBasedOnCreationTime() {
        return basedOnCreationTime;
    }
    private void upload()
    {
        if(transportItems==null)
             transportItems = transportItemDAO.getItemForOrderDestinationFile(id);
    }

    public int getTransportId() {
        return transportId;
    }

    public String getSupplierID() {
        return supplierID;
    }
}
