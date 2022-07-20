package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.TransportDAOs.DriverDAO;
import Backend.DataAccess.DTOs.TransportDTOS.DriverDTO;
import Backend.Logic.Controllers.Callbacks.AvailableDrivers;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicLambdas.GetList;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Transport.TransportBoard;

import java.util.Date;
import java.util.List;

public class DriverController extends Controller implements GetList<Driver> {
    private TransportBoard transportBoard;
    private Driver loggedInDriver;
    private DriverDAO driverDAO;

    public DriverController(){

        this.loggedInDriver = null;
        this.transportBoard=null;
        this.driverDAO=new DriverDAO();
    }

    public List<Driver> getList()
    {
        return uploadDrivers();
    }

    public void setTransportBoard(TransportBoard transportBoard) {
        this.transportBoard = transportBoard;
    }

    public AvailableDrivers getAllDriversLambda()
    {
        return this::uploadDrivers;
    }
    public void setStarted(int transportId) {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        this.loggedInDriver.startTransport(transportId);
    }
    public void register(int driverId, String driverName, String driverLastName, Date date, boolean isShiftManager,
                         BankAccount bankAccount, EmploymentConditions employmentConditions){
        containsEmployee(uploadDrivers(),driverId);
        Driver d = new Driver(driverId,driverName,driverLastName,date,transportBoard,isShiftManager,bankAccount,employmentConditions);
        driverDAO.insert(d);

    }

    /**
     * @param userId
     * @param password
     */

    public void login(int userId, String password) {
        Driver driver=driverDAO.getRow(DriverDTO.getPK(userId));
        if(driver==null)
            throw new IllegalArgumentException(userId+" is not register in the system");
        if(!password.equals( driver.getPassword()))
            throw new TransportException("password is not correct");
        if(driver.isFullyInit())
            driver.setTransportBoard(transportBoard);
        this.loggedInDriver = driver;

    }
    public boolean isLoggedIn(){
        return this.loggedInDriver != null;
    }

    /**
     * @param userId
     */
    @Override
    protected void logout(int userId) {
        if(!isLoggedIn() ||  userId != this.loggedInDriver.getEmployeeId())throw new RuntimeException("The user is not logged in.");
        this.loggedInDriver = null;

    }

    /**
     * @param id
     * @return
     */
    @Override
    protected boolean isLoggedIn(int id) {
        return isLoggedIn() && id==this.loggedInDriver.getEmployeeId();
    }

    public void removeDriver(int userId)
    {
      Driver driver= getDriver(userId);
      driver.removeDriver();
      driverDAO.deleteRow(driver);

    }
    //TODO:split the function so the user side will be in the user controller
    private Driver getDriverByID(int userId,String password)
    {
        Driver toRemove = driverDAO.getRow(DriverDTO.getPK(userId));
        if(toRemove==null)throw new TransportException("Driver is not exist");
        if(!toRemove.getPassword().equals(password))throw new TransportException("Password is incorrect");
        if(toRemove.isFullyInit())
            toRemove.setTransportBoard(transportBoard);
        return toRemove;
    }
    public void weightTruck(int weight) {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        loggedInDriver.weightTruck(weight);
    }
    public void updateLicense(String license) {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        loggedInDriver.setDriverLicense(license);
    }

    public void finishDestinationFile(String destinationId) {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        loggedInDriver.finishDestinationFile(destinationId);
    }

    public String showDetails() {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        return loggedInDriver.showDetails();
    }

    public Driver getDriver(int userId) {
        Driver driver=driverDAO.getRow(DriverDTO.getPK(userId));
        if(driver==null)throw new TransportException("Driver is not exist");
        if(driver.isFullyInit())
            driver.setTransportBoard(transportBoard);
        return driver;
    }
    @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if(loggedInDriver==null)throw new TransportException("Driver is not login");
        loggedInDriver.addConstraint(date,shiftTime);
    }

    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Driver is not login");
        loggedInDriver.deleteConstraint(date,shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Driver is not login");
        loggedInDriver.changePassword(oldPassword, newPassword);
    }
    private List<Driver> uploadDrivers()
    {
        List<Driver> drivers=driverDAO.getRowsFromDB();
        for(Driver d:drivers)
        {
            if(d.isFullyInit())
                d.setTransportBoard(transportBoard);
        }
        return drivers;
    }


}
