package Backend.Logic.LogicObjects.Transport;

import Backend.DataAccess.DAOs.TransportDAOs.TransportFileDAO;
import Backend.Logic.Controllers.Callbacks.AvailableDrivers;
import Backend.Logic.LogicLambdas.*;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.Controllers.TransportEmployee.OrderTransportController;
import Backend.Logic.LogicObjects.Supplier.Order;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Points.*;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class TransportBoard {
    private int transportId;
    private final OrderTransportController orderTransportController;
    private List<TransportFile> inProgress;
    private List<TransportFile> done;
    private final AvailableDrivers availableDrivers;

    private final DriversInShift driversInShift;
    private final IsStoreKeeperInShift isStoreKeeperInShift;
    private final TransportMap transportMap;
    private final TransportFileDAO transportFileDAO;
    private final AcceptDelivery acceptDelivery;
    private final SupplierDays supplierDays;
    private final NotifyForDrivers notifyForDrivers;

    public TransportBoard(OrderTransportController orderTransportController, AvailableDrivers availableDrivers, TransportMap transportMap, DriversInShift driversInShift, IsStoreKeeperInShift isStoreKeeperInShift, AcceptDelivery acceptDelivery, SupplierDays supplierDays, NotifyForDrivers notifyForDrivers) {
        this.orderTransportController = orderTransportController;
        this.inProgress = null;
        this.done = null;
        this.availableDrivers = availableDrivers;
        this.transportMap = transportMap;
        this.driversInShift = driversInShift;
        this.isStoreKeeperInShift = isStoreKeeperInShift;
        this.transportFileDAO = new TransportFileDAO();
        this.transportId = uploadCurrentOrderId() + 1;
        this.acceptDelivery = acceptDelivery;
        this.supplierDays = supplierDays;
        this.notifyForDrivers = notifyForDrivers;
        uploadInprogress();
    }

    public FreeTruck getFreeTruck() {
        return this::isTruckFree;
    }

    private boolean isTruckFree(Truck truck, Date start, Date end) {

        return inProgress.stream().noneMatch(t -> t.getTruck() == truck && (t.getStartDate().before(start) && t.getEndDate().after(start)) ||
                (t.getStartDate().before(end) && t.getEndDate().after(end)));
    }

    public void createTransportFile(Date startingDate, Date endDate, Truck truck, int driverId, String source, Zone from, Zone to, List<Integer> orderId, List<Date> endDates) {
        Driver driver = getInAvailable(driverId, startingDate, endDate);
        createTransportFile(startingDate, endDate, driver, truck, source, from, to, orderId, endDates);

    }

    public void createTransportFile(Date startingDate, Date endDate, Truck truck, String source, Zone from, Zone to, List<Integer> orderId, List<Date> endDates) {
        Driver driver = getFreshDriver(truck.getLicense(), startingDate, endDate);
        createTransportFile(startingDate, endDate, driver, truck, source, from, to, orderId, endDates);

    }

    public String showAndNotifyMatchDates(Integer orderId) {
        Integer today = (LocalDateTime.now().getDayOfWeek().getValue() + 1) % 7;
        OrderTransport orderTransport = getOrderTransport(orderId);
        if (supplierDays.isConstSupplier(orderTransport.getSupplierId())) {
            List<Integer> days = supplierDays.supplierDays(orderTransport.getSupplierId()).stream().map(x -> (x - today) % 7).collect(Collectors.toList());
            if (days.isEmpty())
                return "There is no supply days for this supplier";
            for (Integer plus : days) {
                LocalDateTime plusDate = LocalDateTime.now().plusDays(plus).minusMinutes(LocalDateTime.now().getMinute()).minusHours(LocalDateTime.now().getHour());
                int transportTime = (orderTransport.getDestination().getZone().getNumVal() - orderTransport.getOrigin().getZone().getNumVal()) % 5;
                for (int i = 0; i < 23 - transportTime; i++) {
                    Date from = convertToDateViaInstant(plusDate.plusHours(i));
                    Date to = convertToDateViaInstant(plusDate.plusHours(i + transportTime));
                    List<Driver> drivers = driversInShift.inShift(from).stream().filter(x -> driversInShift.inShift(to).contains(x)).collect(Collectors.toList());
                    drivers = drivers.stream().filter(x -> getInAvailable2(x.getEmployeeId(), from, to) != null).collect(Collectors.toList());

                    if (!drivers.isEmpty())
                        return "Free driver: " + drivers.get(0).getEmployeeId() + " The dates are: From- " + from.toString() + " To- " + to.toString();

                }

            }
            notifyForDrivers.notify(days.stream().map(num -> convertToDateViaInstant(LocalDateTime.now().plusDays(num))).collect(Collectors.toList()));
            return "Missing available days";
        }
        return "The Supplier is not Const";
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    private void createTransportFile(Date startingDate, Date endDate, Driver driver, Truck truck, String source, Zone from, Zone to, List<Integer> orderId, List<Date> endDates) {

        canCreateTransportFile(startingDate, endDate, truck, driver, endDates);
        Point realSource = transportMap.getPoint(source);
        List<OrderTransport> orderTransports = getTransportRequests(orderId);
        TransportFile t = TransportFile.getInstance(transportId, startingDate, endDate, driver, truck, realSource, truck.getBasicWeight(), from,
                to, orderTransports, orderTransportController.getAddExistLambda(), endDates, isStoreKeeperInShift);
        inProgress.add(t);
        transportFileDAO.insert(t);
        transportId++;
    }

    private void canCreateTransportFile(Date startingDate, Date endDate, Truck truck, Driver driver, List<Date> endTimes) {

        if (!driversInShift.inShift(startingDate).contains(driver))
            throw new TransportException("Driver will not be in shift");
        for (TransportFile file : inProgress) {
            if ((file.getDriver() == driver || file.getTruck() == truck) && isDateOverlapping(startingDate, endDate, file.getStartDate(), file.getEndDate()))
                throw new TransportException("The truck or the driver is already placed on this date");
        }

    }

    private boolean isDateOverlapping(Date startingDate1, Date endDate1, Date startingDate2, Date endDate2) {
        return (startingDate1.before(startingDate2) && endDate1.after(startingDate2)) || (startingDate1.before(endDate2) && endDate1.after(endDate2));
    }

    public void startTransport(int transportId, int driverId) {

        getNotFinishedFileById(transportId).setStarted(true, driverId);
    }

    public void removeTransportFile(int transportId) {

        TransportFile t = inProgress.stream().filter(x -> x.getFileId() == transportId & !x.isStarted()).findFirst().orElse(null);
        if (t == null) throw new TransportException("The file is not exist or already finished/started");
        t.delete();
        inProgress.remove(t);
        transportFileDAO.deleteRow(t);
    }

    public void removeTransportFileT(int transportId)//Not for use ! Only For Unit tests
    {
        uploadDone();
        TransportFile inProgressL = inProgress.stream().filter(x -> x.getFileId() == transportId).findFirst().orElse(null);
        TransportFile doneL = done.stream().filter(x -> x.getFileId() == transportId).findFirst().orElse(null);
        if (inProgressL != null) {
            inProgressL.deleteT();
            inProgress.remove(inProgressL);
            transportFileDAO.deleteRow(inProgressL);
        } else if (doneL != null) {
            doneL.deleteT();
            done.remove(doneL);
            transportFileDAO.deleteRow(doneL);
        } else
            throw new TransportException("The file is not exist");
    }

    public void removeItems(String destinationFileId, List<Pair<String, Integer>> pairList) {
        int transportId1 = Integer.parseInt(destinationFileId.split("-")[1]);
        TransportFile t = getNotFinishedFileById(transportId1);
        t.removeItems(destinationFileId, pairList);

    }

    public void removeDestination(int transportId, String destination) {
        TransportFile t = getNotFinishedFileById(transportId);
        t.removeDestination(destination);
    }

    public void FinishDest(String destId, int driverId) {
        TransportFile t = getActiveMission(driverId);
        uploadDone();
        t.finishDest(destId, acceptDelivery);
        if (t.isFinish()) {
            inProgress.remove(t);
            done.add(t);
            t.getDriver().setBusy(false);
            t.getTruck().setBusy(false);
            t.setFinish(true);
        }

    }

    public void insertBranch(String address,
                             String phone,
                             String contactName,
                             Zone zone) {
        transportMap.insertPoint(new Branch(address, phone, contactName, zone));
    }

    public void insertSupplier(String address,
                               String phone,
                               String contactName,
                               Zone zone) {
        transportMap.insertPoint(new Supplier(address, phone, contactName, zone));
    }

    public TransportFile getActiveMission(int driverId) {
        TransportFile t = inProgress.stream().filter(x -> x.getDriver().getEmployeeId() == driverId && x.isStarted()).findFirst().orElse(null);
        if (t == null) throw new TransportException("There is no transport in action");
        return t;
    }

    public void changeTruck(Truck truck, int transportFileId) {
        TransportFile t = getNotFinishedFileById(transportFileId);
        t.changeTruck(truck);
    }

    public void addComment(String comment, int transportFileId) {
        TransportFile t = getNotFinishedFileById(transportFileId);
        t.addComment(comment);
    }

    public void changeDriver(int driver, int transportId) {
        TransportFile t = getNotFinishedFileById(transportId);
        t.changeDriver(getInAvailable(driver, t.getStartDate(), t.getEndDate()));
    }

    private TransportFile getNotFinishedFileById(int transportId) {

        TransportFile t = inProgress.stream().filter(x -> x.getFileId() == transportId).findFirst().orElse(null);
        if (t == null) throw new TransportException("The file is not exist or already finished");
        return t;
    }

    public OrderTransport getOrderTransport(int id) {
        return orderTransportController.getOrderTransport(id);
    }

    public TransportFile getTransportFile(int id) {
        uploadDone();

        TransportFile t1 = inProgress.stream().filter(x -> x.getFileId() == id).findFirst().orElse(null);
        TransportFile t2 = done.stream().filter(x -> x.getFileId() == id).findFirst().orElse(null);
        if (t1 == null && t2 == null) throw new TransportException("The file is not exist or already finished");
        if (t1 != null) return t1;
        else return t2;
    }

    public TransportMap getTransportMap() {
        return transportMap;
    }

    private List<OrderTransport> getTransportRequests(List<Integer> orderId) {
        return orderTransportController.getTransportRequests(orderId);
    }

    private void isBusyDriver(Driver driver, Date start, Date end) {
        for (TransportFile file : inProgress) {
            if ((file.getDriver() == driver) && isDateOverlapping(start, end, file.getStartDate(), file.getEndDate()))
                throw new TransportException("The driver is already placed on this date");
        }
        List<Driver> list = driversInShift.inShift(start);
        if (!list.contains(driver))
            throw new TransportException("The driver is not in shift");

    }

    private boolean isBusyDriverBoolean(Driver driver, Date start, Date end) {
        for (TransportFile file : inProgress) {
            if ((file.getDriver() == driver) && isDateOverlapping(start, end, file.getStartDate(), file.getEndDate()))
                return false;
        }
        return driversInShift.inShift(start).contains(driver);
    }

    private Driver getInAvailable(int driverId, Date start, Date end) {
        Driver driver = availableDrivers.getAvailableDrivers().stream().
                filter(x -> x.getEmployeeId() == driverId).findFirst().orElse(null);
        if (driver == null) throw new TransportException("The driver is Busy or not exist");
        isBusyDriver(driver, start, end);
        return driver;
    }

    private Driver getInAvailable2(int driverId, Date start, Date end) {
        try {
            Driver driver = availableDrivers.getAvailableDrivers().stream().
                    filter(x -> x.getEmployeeId() == driverId).findFirst().orElse(null);
            if (driver == null) throw new TransportException("The driver is Busy or not exist");
            isBusyDriver(driver, start, end);
            return driver;
        } catch (Exception e) {
            return null;
        }
    }

    private Driver getFreshDriver(License license, Date start, Date end) {
        Driver driver = availableDrivers.getAvailableDrivers().
                stream().
                filter(x -> x.getDriverLicense().equals(license)
                        && driversInShift.inShift(start).contains(x)).min(Comparator.comparingInt(Driver::getDistance)).orElse(null);
        if (driver == null) throw new TransportException("The drivers are Busy");
        isBusyDriver(driver, start, end);
        return driver;
    }

    public List<Driver> getAvailableDrivers(Date start, Date end) {
        return availableDrivers.getAvailableDrivers().stream().filter(x -> isBusyDriverBoolean(x, start, end)).collect(Collectors.toList());
    }

    public List<OrderTransport> showAllTransportRequests() {
        return orderTransportController.showAllTransportRequests();
    }


    public OrderTransport getRequestsByPriority() {
        return orderTransportController.getRequestsByPriority();
    }

    public List<OrderTransport> getRequestsByZone(Zone fromZone, Zone toZone) {
        return orderTransportController.getRequestsByZone(fromZone, toZone);
    }

    public DestinationFile getDestinationFile(String destinationFileId) {
        String transportFileId = destinationFileId.split("-")[0];

        uploadDone();
        TransportFile transportFileReturn1 = getTransportFile(inProgress, transportFileId);

        TransportFile transportFileReturn2 = getTransportFile(done, transportFileId);

        return transportFileReturn1 != null ? transportFileReturn1.getDestinationFile(destinationFileId) :
                transportFileReturn2 != null ? transportFileReturn2.getDestinationFile(destinationFileId) : null;
    }

    private TransportFile getTransportFile(List<TransportFile> list, String transportFileId) {
        return list.stream().
                filter(transportFile
                        -> transportFile.getFileId() == Integer.parseInt(transportFileId)).
                findFirst().orElse(null);
    }

    public List<TransportFile> showInprogressTransports() {
        return inProgress;
    }

    public List<TransportFile> showOldTransports() {
        uploadDone();
        return done;
    }

    private void uploadInprogress() {
        if (inProgress == null)
            inProgress = transportFileDAO.uploadInprogress();
    }

    private void uploadDone() {
        if (done == null)
            done = transportFileDAO.uploadDone();
    }

    private int uploadCurrentOrderId() {
        return transportFileDAO.getCurrentId();
    }
}
