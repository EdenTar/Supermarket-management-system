package Backend.Logic.LogicObjects.Jobs;

import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Transport.DestinationFile;
import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Points.Zone;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;

import java.util.Date;
import java.util.List;

public class TransportManager extends Employee {
    private TransportBoard transportBoard;
    private VehicleController vehicleController;

    public TransportManager(int employeeId, String employeeName, String employeeLastName, Date startingDate, VehicleController vehicleController,
                            TransportBoard transportBoard, boolean isShiftManager, BankAccount bankAccount
            , EmploymentConditions employmentConditions) {
        super(employeeId, employeeName, employeeLastName,startingDate,new Job("transport manager"), isShiftManager,bankAccount,employmentConditions, "general");
        this.transportBoard = transportBoard;
        this.vehicleController = vehicleController;
    }
    public TransportManager(int employeeId, String employeeName, String employeeLastName,
                            String password,Date startingDate, VehicleController vehicleController,
                            TransportBoard transportBoard, boolean isShiftManager,BankAccount bankAccount
            ,EmploymentConditions employmentConditions) {
        super(employeeId, employeeName, employeeLastName,password,startingDate,new Job("transport manager"), isShiftManager,bankAccount,employmentConditions, "general");
        this.transportBoard = transportBoard;
        this.vehicleController = vehicleController;
    }
    public void initialize(TransportBoard transportBoard, VehicleController vehicleController)
    {
        this.transportBoard=this.transportBoard==null?transportBoard:this.transportBoard;
        this.vehicleController =this.vehicleController ==null? vehicleController :this.vehicleController;
    }

    public void addTruck(int truckId, String model, int basicWeight, int maxWeight, License license) {
        vehicleController.addTruck(truckId, model, basicWeight, maxWeight,license);
    }

    public void createTransportFile(Date startingDate, Date endDate, int truckId, int driverId, String source, Zone from, Zone to, List<Integer>orderId, List<Date> endDates) {

        transportBoard.createTransportFile(startingDate,endDate,getTruck(truckId),driverId,source,from,to,orderId,endDates);
    }
    public void createTransportFile(Date startingDate,Date endDate, int truckId, String source, Zone from,Zone to,List<Integer>orderId,List<Date> endDates) {
        transportBoard.createTransportFile(startingDate,endDate,getTruck(truckId),source,from,to,orderId,endDates);
    }
    public String showAndNotifyMatchDates(Integer orderId)
    {
       return transportBoard.showAndNotifyMatchDates(orderId);
    }

    public void removeTruck(int truckId) {
        vehicleController.removeTruck(truckId);
    }

    public Truck getFreeTruck() {
        return vehicleController.getFreeTruck();
    }

    public Truck getTruck(int truckId) {
        return vehicleController.getTruck(truckId);
    }

    public List<Truck> showAllAvailableTrucks(Date start,Date end) {//TODO:tomer
        return vehicleController.availableTruck(start,end);
    }

    public List<OrderTransport> showAllTransportRequests() {
        return transportBoard.showAllTransportRequests();
    }

    public List<Driver> showAllAvailableDrivers(Date start,Date end) {
       return transportBoard.getAvailableDrivers(start,end);
    }

    public void insertBranch( String address,
                              String phone,
                              String contactName,
                              Zone zone)
    {
        transportBoard.insertBranch(address,phone,contactName,zone);
    }
    public void insertSupplier(String address,
                               String phone,
                               String contactName,
                               Zone zone)
    {
        transportBoard.insertSupplier(address,phone,contactName,zone);
    }
    public List<String> getAllPointInfo()
    {
        return transportBoard.getTransportMap().getAllPoints();
    }
    public void removeItems(String destinationFileId,List<Pair<String,Integer>>pairList) {
        transportBoard.removeItems(destinationFileId,pairList);
    }
    public void deleteDestination(int transportId,String destination)
    {
        transportBoard.removeDestination(transportId,destination);
    }

    public void changeTruck(int truckId, int transportFileId) {
        Truck newTruck=getTruck(truckId);
        transportBoard.changeTruck(newTruck,transportFileId);
    }
    public void changeDriver(int driverId,int transportFileId)
    {
        transportBoard.changeDriver(driverId,transportFileId);
    }

    public void cancelTransport(int transportFileId) {
        transportBoard.removeTransportFile(transportFileId);

    }
    public void addComment(String comment,int transportId)
    {
        transportBoard.addComment(comment,transportId);
    }

    public TransportFile getTransportFile(int id)
    {
        return transportBoard.getTransportFile(id);
    }

    public OrderTransport getOrderTransport(int id)
    {
        return transportBoard.getOrderTransport(id);
    }
    public DestinationFile getDestinationFile(String destinationFileId) {
        return transportBoard.getDestinationFile(destinationFileId);
    }
//TODO
    public List<TransportFile> showOldTransports() {
        return transportBoard.showOldTransports();
    }

    public OrderTransport getRequestsByPriority() {
        return transportBoard.getRequestsByPriority();
    }

    public List<OrderTransport> getRequestsByZone(Zone fromZone, Zone toZone) {
        return transportBoard.getRequestsByZone(fromZone,toZone);
    }

    public List<TransportFile> showInprogressTransports() {
        return transportBoard.showInprogressTransports();
    }


}
