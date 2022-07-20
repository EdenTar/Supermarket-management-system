package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.TransportDAOs.TransportManagerDAO;
import Backend.DataAccess.DTOs.TransportDTOS.TransportManagerDTO;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.LogicObjects.Jobs.TransportManager;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Transport.DestinationFile;
import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Points.Zone;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;
import Backend.Logic.LogicLambdas.GetList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TransportManagerController extends Controller implements GetList<TransportManager> {

    private TransportManager loggedInTransportManager;

    private VehicleController vehicleController;
    private TransportBoard transportBoard;
    private TransportManagerDAO managerDAO;

    public TransportManagerController(VehicleController vehicleController, TransportBoard transportBoard) {
        loggedInTransportManager = null;
        this.vehicleController = vehicleController;
        this.transportBoard = transportBoard;
        this.managerDAO=new TransportManagerDAO();

    }
    public List<TransportManager> getList()
    {
        return uploadManager();
    }

    public void register(int employeeId, String employeeName, String employeeLastName, Date startingDate,
                         boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions) {
        containsEmployee(uploadManager(), employeeId);
        managerDAO.insert(new TransportManager(employeeId, employeeName,employeeLastName, startingDate, vehicleController, transportBoard,isShiftManager,bankAccount,employmentConditions));
    }
    public void removeTransportManager(int userId)
    {
        TransportManager manager= getTransportManager(userId);
        managerDAO.deleteRow(manager);
    }

    private TransportManager getTransportManager(int userId) {
        TransportManager transportManager=managerDAO.getRow(TransportManagerDTO.getPK(userId));
        if(transportManager==null)throw new TransportException("Transport Manager is not exist");
        transportManager.initialize(transportBoard, vehicleController);
        return transportManager;
    }

    /**
     * @param userName
     * @param password
     */

    public void login(int userName, String password) {
        TransportManager manager=managerDAO.getRow(TransportManagerDTO.getPK(userName));
        if(manager==null)
            throw new IllegalArgumentException(userName+" is not register in the system");
        if(!password.equals( manager.getPassword()))
            throw new TransportException("password is not correct");
        manager.initialize(transportBoard, vehicleController);
        this.loggedInTransportManager = manager;

    }

    public void addTruck(int truckId, String model, int currentWeight, int maxWeight, License license) {
        TransportManager actor = getTransportManager();
        actor.addTruck(truckId, model, currentWeight, maxWeight, license);
    }

    public void deleteTruck(int truckId) {
        TransportManager actor = getTransportManager();
        actor.removeTruck(truckId);

    }
    public void insertBranch( String address,
                              String phone,
                              String contactName,
                              Zone zone)
    {
        TransportManager actor = getTransportManager();
        try{
        actor.insertBranch(address,phone,contactName,zone);}
        catch (Exception e)
        {
            throw new TransportException("one of the details wrong");
        }
    }
    public void insertSupplier(String address,
                               String phone,
                               String contactName,
                               Zone zone)
    {
        TransportManager actor = getTransportManager();
        try{
            actor.insertSupplier(address,phone,contactName,zone);}
        catch (Exception e)
        {
            throw new TransportException("one of the details wrong");
        }

    }

    public Truck getFreeTruck() {//TODO: not for use
        TransportManager actor = getTransportManager();
        return actor.getFreeTruck();
    }

    public Truck getTruck(int truckId) {
        TransportManager actor = getTransportManager();
        return actor.getTruck(truckId);
    }

    public List<Truck> showAllAvailableTrucks(Date from,Date to) {
        TransportManager actor = getTransportManager();
        return actor.showAllAvailableTrucks(from, to);

    }

    /**
     * @param userId
     */
    @Override
    protected void logout(int userId) {
        boolean b=userId!=this.loggedInTransportManager.getEmployeeId();
        if(!isLoggedIn() ||  b)throw new RuntimeException("The user is not logged in.");
        this.loggedInTransportManager = null;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    protected boolean isLoggedIn(int userId) {

        return isLoggedIn() && userId==this.loggedInTransportManager.getEmployeeId();
    }


    public boolean isLoggedIn() {
        return loggedInTransportManager != null;
    }

    public TransportManager getTransportManager() {
        if (!isLoggedIn()) throw new TransportException("Not log in as a Manager");
        return loggedInTransportManager;
    }

    public List<OrderTransport> showAllTransportRequests() {
        TransportManager transportManager = getTransportManager();
        return transportManager.showAllTransportRequests();
    }

    public List<Driver> showAllAvailableDrivers(Date from,Date to) {
        TransportManager transportManager = getTransportManager();
        return transportManager.showAllAvailableDrivers(from, to);
    }
//NotForUse
    public void createTransportFile(Date startingDate,Date endDate ,int truckId, int driverId, String source, String from,String to,List<Integer>orderId,List<Date> endDates) {
        TransportManager transportManager = getTransportManager();

        transportManager.createTransportFile(startingDate,endDate,truckId,driverId,source,Zone.valueOf(from),Zone.valueOf(to),orderId,endDates);
    }
    public void createTransportFile(Date startingDate,Date endDate ,int truckId, String source, String from,String to,List<Integer>orderId,List<Date> endDates) {
        TransportManager transportManager = getTransportManager();

        transportManager.createTransportFile(startingDate,endDate,truckId,source,Zone.valueOf(from),Zone.valueOf(to),orderId,endDates);
    }
    public String showAndNotifyMatchDates(Integer orderId)
    {
        TransportManager transportManager = getTransportManager();
        return transportManager.showAndNotifyMatchDates(orderId);
    }
    public void removeItems(String destinationFileId,List<Pair<String,Integer>> pairList) {
        TransportManager transportManager = getTransportManager();
        transportManager.removeItems(destinationFileId,pairList);
    }
    public void deleteDestination(int transportId,String destination)
    {
        TransportManager transportManager = getTransportManager();
        transportManager.deleteDestination(transportId,destination);
    }

    public void changeTruck(int truckId, int transportFileId) {
        TransportManager transportManager = getTransportManager();
        transportManager.changeTruck(truckId,transportFileId);
    }
    public void changeDriver(int driverId,int transportFileId)
    {
        TransportManager transportManager = getTransportManager();
        transportManager.changeDriver(driverId,transportFileId);
    }
    public void addComment(String comment,int transportId) {
        TransportManager transportManager = getTransportManager();
        transportManager.addComment(comment,transportId);

    }

    public void cancelTransport( int transportFileId) {
        TransportManager transportManager = getTransportManager();
        transportManager.cancelTransport(transportFileId);
    }
    public List<String> getAllPointInfo()
    {
        TransportManager transportManager = getTransportManager();
        return transportManager.getAllPointInfo();
    }


    public DestinationFile getDestinationFile(String destinationFileId) {
        TransportManager transportManager = getTransportManager();
        return transportManager.getDestinationFile(destinationFileId);
    }

    public List<TransportFile> showOldTransports() {
        TransportManager transportManager = getTransportManager();
        return transportManager.showOldTransports();
    }
    public List<TransportFile> showInProgressTransports() {
        TransportManager transportManager = getTransportManager();
        return transportManager.showInprogressTransports();
    }


    public OrderTransport getRequestsByPriority() {
        TransportManager transportManager = getTransportManager();
        return transportManager.getRequestsByPriority();
    }

    public List<OrderTransport> getRequestsByZone(String fromZone, String toZone) {
        TransportManager transportManager = getTransportManager();
        return transportManager.getRequestsByZone(Zone.valueOf( fromZone),Zone.valueOf(toZone));
    }
    public TransportFile getTransportFile(int id)
    {
        TransportManager transportManager = getTransportManager();
        return transportManager.getTransportFile(id);
    }
    public OrderTransport getOrderTransport(int id)
    {
        TransportManager transportManager = getTransportManager();
        return transportManager.getOrderTransport(id);
    }

    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Transport Manager is not logged in");
        loggedInTransportManager.addConstraint(date,shiftTime);
    }

    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Transport Manager is not logged in");
        loggedInTransportManager.deleteConstraint(date,shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Transport Manager is not logged in");
        loggedInTransportManager.changePassword(oldPassword,newPassword);
    }
    private List<TransportManager> uploadManager()
    {
        List<TransportManager> list=managerDAO.getRowsFromDB();
        for(TransportManager t:list)
            t.initialize(transportBoard, vehicleController);
        return list;
    }

}
