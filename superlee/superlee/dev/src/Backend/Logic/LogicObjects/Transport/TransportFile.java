package Backend.Logic.LogicObjects.Transport;

import Backend.DataAccess.DAOs.TransportDAOs.DestinationFileDAO;
import Backend.DataAccess.DAOs.TransportDAOs.OrderTransportDAO;
import Backend.DataAccess.DAOs.TransportDAOs.TransportFileDAO;
import Backend.Logic.LogicLambdas.AcceptDelivery;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Points.Branch;
import Backend.Logic.Points.Point;
import Backend.Logic.Points.Zone;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.LogicLambdas.AddExist;
import Backend.Logic.LogicLambdas.InformObserver;
import Backend.Logic.LogicLambdas.IsStoreKeeperInShift;


import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TransportFile implements InformObserver {
    private List<DestinationFile> destinationFiles;
    private int fileId;
    private boolean started;
    private Date startDate;
    private Date endDate;
    private Driver driver;
    private int driverId;
    private Truck truck;
    private Point source;
    private int startingWeight;
    private Zone from;
    private String comment;
    private Zone to;
    private boolean isFinish;
    private AddExist addExistLambda;
    private IsStoreKeeperInShift isStoreKeeperInShift;
    private DestinationFileDAO destinationFileDAO;
    private TransportFileDAO transportFileDAO;


    public static TransportFile getInstance(int fileId, Date startDate,Date endDate, Driver driver, Truck truck,
                                            Point source, int startingWeight, Zone from, Zone to,
                                            List<OrderTransport> orderTransports, AddExist addExistLambda,List<Date> endDates,IsStoreKeeperInShift isStoreKeeperInShift) {

        if(!startDate.after(new Date())||!startDate.before(endDate))throw new TransportException("invalid dates");
        if(endDates.size()!=orderTransports.size())throw new TransportException("not equal number of dates and OrderTransports");

        endTimeChecker(startDate,endDate,endDates);
        TransportFile transportFile = new TransportFile(fileId, startDate,endDate, driver, truck, source, startingWeight, from, to, addExistLambda,isStoreKeeperInShift);
        try {
            Iterator<Date> iter=endDates.iterator();
            for (OrderTransport orderTransport : orderTransports) {

                DestinationFile toAdd=new DestinationFile(fileId, orderTransport,addExistLambda,iter.next(),orderTransport.getCreationDate(), orderTransport.getSupplierId());
                transportFile.addDestinationFile(toAdd);
            }
            OrderTransportDAO dao= new OrderTransportDAO();
            for(OrderTransport orderTransport : orderTransports)
                dao.deleteRow(orderTransport);
            for(DestinationFile d: transportFile.getDestinationFiles())
                d.setOwner();
        } catch (Exception e) {
            if(e instanceof TransportException)
                throw e;
            throw new TransportException("one of the order Transport File is not match with the zones");
        }
        return transportFile;
    }


    private TransportFile(int fileId, Date startDate,Date endDate, Driver driver, Truck truck, Point source,
                          int startingWeight, Zone from, Zone to, AddExist addExistLambda,IsStoreKeeperInShift isStoreKeeperInShift) {
        this.destinationFiles = new LinkedList<>();
        this.fileId = fileId;
        this.started = false;
        this.startDate = startDate;
        this.endDate=endDate;
        this.driver = driver;
        this.driverId=0;
        driver.addObserver(this);
        driver.setDistance(Math.abs(from.getNumVal()-to.getNumVal()));
        this.truck = truck;
        truck.addObserver(this);
        this.source = source;
        this.startingWeight = startingWeight;
        this.from = from;
        this.to = to;
        this.isFinish=false;
        this.addExistLambda = addExistLambda;
        this.comment="";
        this.isStoreKeeperInShift=isStoreKeeperInShift;
        this.destinationFileDAO=new DestinationFileDAO();
        this.transportFileDAO=new TransportFileDAO();

    }
    public TransportFile(int fileId, Date startDate,Date endDate, Driver driver,int driverId, Truck truck, Point source,
                          int startingWeight, Zone from, Zone to,boolean isFinish,String comment,boolean started) {
        this.destinationFiles = null;
        this.fileId = fileId;
        this.started = started;
        this.startDate = startDate;
        this.endDate=endDate;
        this.driver = driver;
        this.driverId=driverId;
        if(driver!=null)driver.addObserver(this);
        this.truck = truck;
        truck.addObserver(this);
        this.source = source;
        this.startingWeight = startingWeight;
        this.from = from;
        this.to = to;
        this.isFinish=isFinish;
        this.comment=comment;
        this.destinationFileDAO=new DestinationFileDAO();
        this.transportFileDAO=new TransportFileDAO();

    }
    private void upload()
    {
        if(destinationFiles==null)
            destinationFiles=destinationFileDAO.getDestinationFile(fileId);
    }
    public List<DestinationFile> getDestinationFiles() {
        upload();
        return destinationFiles;
    }

    @Override
    public String toString() {
        String s="TransportFile{" +
                "destinationFiles=" + destinationFiles +
                ", fileId=" + fileId +
                ", started=" + started +
                ", startDate=" + startDate +
                ", startDate=" + endDate;
        if(driver==null)
            s=s+", driver=" + driverId;
        else
            s=s+", driver=" + driver ;

        s=s+", truck=" + truck +
                ", source=" + source +
                ", startingWeight=" + startingWeight +
                ", from=" + from +
                ", to=" + to +
                '}';
        return s;
    }

    private static void endTimeChecker(Date startingDate,Date endDate,List<Date>endTimes)
    {
        for(Date date:endTimes)
        {
            if(startingDate.after(date)||endDate.before(date))
                throw new TransportException("invalid date");
        }
    }

    public boolean isFinish() {
        upload();
        return destinationFiles.stream().allMatch(DestinationFile::isDone);
    }

    public void finishDest(String destId, AcceptDelivery acceptDelivery) {
        upload();
        DestinationFile d = destinationFiles.stream().filter(x -> x.getId().equals(destId)).findFirst().orElse(null);
        if (d == null) throw new TransportException("there is no such Destination File");
        d.setDone(true,acceptDelivery);

    }
    public void addComment(String comment)
    {
        this.comment=this.comment+comment+"\n";
        transportFileDAO.update(this);
    }

    public void delete() {
        upload();
        if (isStarted()) throw new TransportException("the transport already started");
        for (DestinationFile d : destinationFiles)
        { d.delete();
          destinationFileDAO.deleteRow(d);
        }
        getDriver().setBusy(false);
        getTruck().setBusy(false);

    }
    public void deleteT() {//Not for use ! Only For Unit tests
        upload();
        for (DestinationFile d : destinationFiles)
        { d.deleteT();
            destinationFileDAO.deleteRow(d);
        }
        if(!isFinish) {
            getDriver().setBusy(false);
            getTruck().setBusy(false);
        }
    }

    public void removeItems(String destinationId, List<Pair<String, Integer>> toRemove) {
        DestinationFile dFile = getDestinationFile(destinationId);
        dFile.removeItems(toRemove);
    }

    public void addDestinationFile(DestinationFile destinationFile) {
        upload();
        if (destinationFiles.contains(destinationFile))
            throw new TransportException("the file is already exist in the transport");
        if (destinationFile.getSource().getZone() != from || destinationFile.getDestination().getZone() != to)
            throw new TransportException("zone is not match" + destinationFile.getSource().getZone() + " " + destinationFile.getDestination().getZone());
        storeKeeperChecker(destinationFile);
        destinationFiles.add(destinationFile);
        destinationFileDAO.insert(destinationFile);
    }//
    private void storeKeeperChecker(DestinationFile destinationFile)
    {
        if(destinationFile.getDestination() instanceof Branch && !isStoreKeeperInShift.isStoreKeeperInShift((Branch)destinationFile.getDestination(),destinationFile.getArrivalDate()))
            throw new TransportException("No Store keeper will work");
    }

    public void removeDestinationFile(String destinationFileId) {
        upload();
        DestinationFile dest = getDestinationFile(destinationFileId);
        if (dest.isDone()) throw new TransportException("Can`t delete Destination file that already done");
        dest.delete();
        destinationFileDAO.deleteRow(dest);
        destinationFiles.remove(dest);

    }

    public void removeDestination(String destination) {
        upload();
        if (destinationFiles.stream().anyMatch(x -> x.getDestination().getAddress().equals(destination) && x.isDone()))
            throw new TransportException("Can`t delete the destination");
        if (destinationFiles.stream().noneMatch(x -> x.getDestination().getAddress().equals(destination)))
            throw new TransportException("The destination does not exists");
        List<DestinationFile> rest = new LinkedList<>();
        for (DestinationFile d : destinationFiles) {
            if (d.getDestination().getAddress().equals(destination))
            {
                d.delete();
                destinationFileDAO.deleteRow(d);
            }
            else
                rest.add(d);
        }
        destinationFiles = rest;
    }

    public void changeTruck(Truck truck) {

        setTruck(truck);

    }
    public void changeDriver(Driver driver)
    {
        setDriver(driver);

    }

    public DestinationFile getDestinationFile(String destinationFileId) {
        upload();
        DestinationFile dest = destinationFiles.stream().
                filter(d -> d.getId().equals(destinationFileId))
                .findFirst().orElse(null);
        if (dest == null) throw new TransportException("destination file is not exist");
        return dest;
    }

    @Override
    public void act(Object o) {
        if (o instanceof Truck) this.truck = null;
        if (o instanceof Driver) this.driver = null;
    }

    @Override
    public boolean canAct(Object o) {
        return !isStarted()||(isFinish);
    }


    public int getFileId() {
        return fileId;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started, int driverId) {
        if (this.started) throw new TransportException("The transport already started");
        if (this.truck == null || this.driver == null) throw new TransportException("There is no driver or truck");
        if (this.driver.getEmployeeId()!=driverId)
            throw new TransportException("The driver is not part of this transport");
        if (started) {
            if (!truck.isBusy() && !driver.isBusy()) {
                truck.setBusy(true);
                driver.setBusy(true);
            } else throw new TransportException("The driver or the truck busy");
        }
        this.started = started;
        transportFileDAO.update(this);

    }

    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setStartDate(Date startDate) {
        if (startDate.after(new Date())) throw new TransportException("Date is invalid");
        this.startDate = startDate;
        transportFileDAO.update(this);
    }

    public Driver getDriver() {
        return driver;
    }

    //TODO
    public void setDriver(Driver driver) {
        if (this.started) throw new TransportException("The transport already started");
        if (truck != null && truck.getLicense() != driver.getDriverLicense())
            throw new TransportException("driver cant drive this truck");
        if (this.driver != null)
            this.driver.removeObserver(this);
        this.driver = driver;
        this.driver.addObserver(this);
        transportFileDAO.update(this);
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        //if (this.started) throw new TransportException("The transport already started");//TODO:check if there is no problem
        if (driver != null && truck.getLicense() != driver.getDriverLicense())
            throw new TransportException("driver cant drive this truck");
        if (this.truck != null)
            this.truck.removeObserver(this);
        this.truck = truck;
        this.truck.addObserver(this);
        transportFileDAO.update(this);
    }

    public Point getSource() {
        return source;
    }

    public void setSource(Point source) {
        if (source.getZone() != from) throw new TransportException("Source Point is not in The Zone");
        this.source = source;
        transportFileDAO.update(this);
    }

    public int getStartingWeight() {
        return startingWeight;
    }

    public void setStartingWeight(int startingWeight) {
        if (!isStarted()) throw new TransportException("The Transport didnt start");
        if (startingWeight <truck.getBasicWeight() || startingWeight > truck.getMaxWeight())
            throw new TransportException("Weight is to much big or small");
        this.startingWeight = startingWeight;
        transportFileDAO.update(this);
    }

    public Zone getFrom() {
        return from;
    }

    public void setFrom(Zone from) {
        this.from = from;
        transportFileDAO.update(this);
    }

    public Zone getTo() {
        return to;
    }

    public void setTo(Zone to) {
        this.to = to;
        transportFileDAO.update(this);
    }

    public String getComment() {
        return this.comment;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
        driverId=driver.getEmployeeId();
        driver.removeObserver(this);
        driver=null;
        transportFileDAO.update(this);
    }
    public int getDriverId() {
        return driverId;
    }
}
