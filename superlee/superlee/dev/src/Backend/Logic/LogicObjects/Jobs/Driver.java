package Backend.Logic.LogicObjects.Jobs;



import Backend.DataAccess.DAOs.TransportDAOs.DriverDAO;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.Vehicles.License;
import Backend.Logic.LogicLambdas.InformObservable;
import Backend.Logic.LogicLambdas.InformObserver;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Driver extends Employee implements InformObservable {

    private TransportBoard transportBoard;
    private boolean isBusy;
    private License driverLicense;
    private int distance;
    private LocalDate lastUpdateDistance;
    private DriverDAO driverDAO;
    private List<InformObserver> usedBy;

    public Driver(int driverId, String driverName, String driverLastName, Date date, TransportBoard transportBoard, boolean isShiftManager,
                  BankAccount bankAccount, EmploymentConditions employmentConditions){
        super(driverId,driverName,driverLastName,date,new Job("driver"), isShiftManager,bankAccount,employmentConditions, "general");
        isBusy=false;
        driverLicense=License.NONE;
        usedBy=new LinkedList<>();
        this.transportBoard=transportBoard;
        this.distance=0;
        this.lastUpdateDistance=LocalDate.now();
        this.driverDAO=new DriverDAO();
    }
    public Driver(int driverId, String driverName, String driverLastName,String password, Date date, TransportBoard transportBoard, boolean isShiftManager,
                  BankAccount bankAccount,EmploymentConditions employmentConditions,License license,LocalDate lastUpdateDistance,int distance){
        super(driverId,driverName,driverLastName,password,date,new Job("driver"), isShiftManager,bankAccount,employmentConditions, "general");
        isBusy=false;
        driverLicense=license;
        usedBy=new LinkedList<>();
        this.transportBoard=transportBoard;
        this.distance=distance;
        this.lastUpdateDistance = lastUpdateDistance;
        this.driverDAO=new DriverDAO();
    }

    public void weightTruck(int weight)
    {
        transportBoard.getActiveMission(getEmployeeId()).setStartingWeight(weight);
    }
    public void finishDestinationFile(String destinationId) {
        transportBoard.FinishDest(destinationId,getEmployeeId());
    }
    public void startTransport(int transportId){
        transportBoard.startTransport(transportId,this.getEmployeeId());
    }
    public void setDriverLicense(String driverLicense) {
        if(!checkIfCanNotify())throw new TransportException("The driver is used by other open files");
        try {
            this.driverLicense = License.valueOf(driverLicense);
        }catch (Exception e)
        {
            throw new TransportException("Not type of license");
        }
        update();
    }
//
    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        if(this.isBusy==busy&busy)throw new TransportException("The driver already Busy ");
        isBusy = busy;
        update();
    }
    public void removeDriver()
    {
        if(!checkIfCanNotify())throw new TransportException("The driver is used by other open files");
        notifyAllObservers();
    }

    @Override
    public void notifyAllObservers() {
        for(InformObserver observer:usedBy)
            observer.act(this);
    }

    @Override
    public boolean checkIfCanNotify() {
        boolean success=true;
        for(InformObserver observer:usedBy)
            success=observer.canAct(this)&success;
        return success;
    }

    @Override
    public void addObserver(InformObserver observer) {
        if(!usedBy.contains(observer))usedBy.add(observer);
    }

    @Override
    public void removeObserver(InformObserver observer) {
        if(usedBy.contains(observer))usedBy.remove(observer);
    }

    public License getDriverLicense() {
        return driverLicense;
    }

    public String showDetails() {
        return  super.showDetails()+" License: "+getDriverLicense();
    }

    public int getDistance() {
        if(!afterLastWeek())
        {
            this.distance=0;
            this.lastUpdateDistance=LocalDate.now();
            update();
        }
        return distance;

    }

    public void setDistance(int distance) {
            this.distance += distance;
            update();
    }
    public boolean afterLastWeek()
    {
        return lastUpdateDistance.plusWeeks(1).isAfter(LocalDate.now());
    }
    private void update()
    {
        driverDAO.update(this);
    }
    public void setTransportBoard(TransportBoard transportBoard) {
        this.transportBoard = transportBoard;
    }
    public boolean isFullyInit()
    {
        return transportBoard==null;
    }

    public LocalDate getLastUpdateDistance() {
        return lastUpdateDistance;
    }
}
