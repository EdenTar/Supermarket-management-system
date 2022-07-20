package Backend.ServiceLayer.Services;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.Starters.Starter;
import Backend.Logic.Utilities.Pair;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.HRService;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.UserService;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Transport.DriverService;
import Backend.ServiceLayer.Facades.ServicePerJob.Transport.TransportManagerService;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.ServiceObjects.Transport.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TransportEmployeeService implements TransportEmployeeServiceAPI, DeleteSupplierOrderFunctionality {
    private final DriverService driverService;
    private final UserService userService;
    private final TransportManagerService transportManagerService;
    private final HRService hrService;


    public TransportEmployeeService() {
        Starter starter = Starter.getInstance();
        this.userService = starter.getUserService();
        this.transportManagerService = starter.getManagerService();
        this.driverService = starter.getDriverService();
        this.hrService = starter.getHrService();
    }

    @Override
    public Response login(int userId, String password, String type) {
        return userService.login(userId, password, type);
    }

    @Override
    public Response logout(int userId) {
        return userService.logout(userId);
    }


    @Override
    public Response<List<ServiceTransportFile>> showInProgressTransports() {
        return transportManagerService.showInProgressTransports();
    }

    public Response<String> showAndNotifyMatchDates(Integer orderId) {
        return transportManagerService.showAndNotifyMatchDates(orderId);
    }

    @Override
    public Response<ServiceTransportFile> getTransportFile(int id) {

        return transportManagerService.getTransportFile(id);

    }

    @Override
    public Response<OrderTransportService> getOrderTransport(int id) {
        return transportManagerService.getOrderTransport(id);

    }

    @Override
    public Response<List<String>> getAllPointInfo() {
        return transportManagerService.getAllPointInfo();
    }


    @Override
    public Response<List<OrderTransportService>> showAllTransportRequests() {
        return transportManagerService.showAllTransportRequests();
    }


    @Override
    public Response addTruck(int truckId, String model, int currentWeight, int maxWeight, LicenseService l) {
        return transportManagerService.addTruck(truckId, model, currentWeight, maxWeight, l);
    }

    @Override
    public Response deleteTruck(int truckId) {

        return transportManagerService.deleteTruck(truckId);
    }

    @Override
    public Response<List<ServiceTruck>> showAllAvailableTrucks(Date from, Date to) {

        return transportManagerService.showAllAvailableTrucks(from, to);
    }

    public Response insertBranch(String address, String phone, String contactName, String zone) {
        return transportManagerService.insertBranch(address, phone, contactName, zone);
    }

    public Response insertSupplier(String address, String phone, String contactName, String zone) {
        return transportManagerService.insertSupplier(address, phone, contactName, zone);
    }



    @Override
    public Response createTransportFile(Date startDate, Date endDate, int truckId, String source, String from, String to, List<Integer> orderId, List<Date> arrivalDates) {

        return transportManagerService.createTransportFile(startDate, endDate, truckId, source, from, to, orderId, arrivalDates);
    }


    @Override
    public Response<List<ServiceDriver>> showAvailableDrivers(Date from, Date to) {
        return transportManagerService.showAllAvailableDrivers(from, to);
    }


    @Override
    public Response updateLicense(String license) {
        return driverService.updateLicense(license);
    }

    @Override
    public Response removeItems(String destinationId, List<Pair<String, Integer>> list) {
        return transportManagerService.removeItems(destinationId, list);
    }


    @Override
    public Response changeTruck(int truckId, int transportFileId) {
        return transportManagerService.changeTruck(truckId, transportFileId);
    }

    @Override
    public Response changeDriver(int driverId, int transportFileId) {
        return transportManagerService.changeDriver(driverId, transportFileId);
    }


    @Override
    public Response cancelTransport(int transportFileId) {
        return transportManagerService.cancelTransport(transportFileId);
    }

    @Override
    public Response removeDestination(int transportFileId, String destination) {
        return transportManagerService.removeDestination(transportFileId, destination);
    }

    @Override
    public Response<?> addComment(String comment, int transportId) {
        return transportManagerService.addComment(comment, transportId);
    }


    @Override
    public Response<ServiceDestinationFile> getDestinationFile(String destinationFileId) {
        return transportManagerService.getDestinationFile(destinationFileId);
    }

    @Override
    public Response<List<ServiceTransportFile>> showOldTransports() {
        return transportManagerService.showOldTransports();
    }

    @Override
    public Response<OrderTransportService> getRequestsByPriority() {
        return transportManagerService.getRequestsByPriority();
    }


    @Override
    public Response<List<OrderTransportService>> getRequestsByZone(String fromZone, String toZone) {
        return null;
    }


    @Override
    public Response weightTruck(int weight) {

        return driverService.weightTruck(weight);
    }

    @Override
    public Response finishDestinationFile(String destinationId) {
        return driverService.finishDestinationFile(destinationId);
    }

    @Override
    public Response setStarted(int transportId) {
        return driverService.setStarted(transportId);
    }

    @Override
    public Response<Boolean> isLoggedIn(int userId, String type) {
        return userService.isLoggedIn(userId, type);
    }


    @Override
    public Response<LicenseService[]> showAvailableLicenses() {
        return new Response<LicenseService[]>(LicenseService.values());
    }

    @Override
    public Response<String> showDetails() {
        return driverService.showDetails();
    }


    // HR Service:
    @Override
    public Response addNewJob(String jobName) {
        return hrService.addNewJob(jobName);
    }

    @Override
    public Response addNewEmployee(String firstName, String lastName, Date date, int id, String jobName, boolean isShiftManager,
                                   int bankNumber, int accountNumber, int bankBranch, double salary, String socialBenefits, String branchAddress) {
        return hrService.addNewEmployee(firstName, lastName, date, id, jobName, isShiftManager, bankNumber, accountNumber, bankBranch, salary, socialBenefits, branchAddress);
    }

    @Override
    public Response removeEmployee(int id) {
        return hrService.removeEmployee(id);
    }

    @Override
    public Response updateBankNumber(int id, int newBankNumber) {
        return hrService.updateBankNumber(id, newBankNumber);
    }

    @Override
    public Response updateAccountNumber(int id, int newAccountNumber) {
        return hrService.updateAccountNumber(id, newAccountNumber);
    }

    @Override
    public Response updateBankBranch(int id, int newBankBranch) {
        return hrService.updateBankBranch(id, newBankBranch);
    }

    @Override
    public Response<HashMap<Integer, String>> showForEachEmployeeHisJob() {
        return hrService.showForEachEmployeeHisJob();
    }

    @Override
    public Response<LinkedList<String>> showEmployees() {
        return hrService.showEmployees();
    }

    @Override
    public Response updateSalary(int id, double newSalary) {
        return hrService.updateSalary(id, newSalary);
    }

    @Override
    public Response addSocialBenefits(int id, String socialBenefits) {
        return hrService.addSocialBenefits(id, socialBenefits);
    }

    @Override
    public Response addNewSocialBenefits(int id, String socialBenefits) {
        return hrService.addNewSocialBenefits(id, socialBenefits);
    }

    @Override
    public Response updateFirstName(int id, String newFirstName) {
        return hrService.updateFirstName(id, newFirstName);
    }

    @Override
    public Response updateLastName(int id, String newLastName) {
        return hrService.updateLastName(id, newLastName);
    }

    @Override
    public Response changePassword(String oldPassword, String newPassword) {
        return userService.changePassword(oldPassword, newPassword);
    }

    @Override
    public Response updateIsShiftManager(int id, boolean isShiftManager) {
        return hrService.updateIsShiftManager(id, isShiftManager);
    }

    @Override
    public Response<String> getEmployeeConstrains(int employeeID) {
        return hrService.getEmployeeConstrains(employeeID);

    }

    @Override
    public Response scheduleDriver() {
        return hrService.scheduleDriver();

    }

    public Response<String> getShiftPlacements(Date date, ShiftTime shiftTime, String branch) {
        return hrService.getShiftPlacements(date, shiftTime, branch);
    }

    @Override
    public Response addScheduling(ShiftTime shiftTime, Date date, String branch, int employeeID, String jobName) {
        return hrService.addScheduling(shiftTime, date, branch, employeeID, jobName);
    }

    @Override
    public Response removeScheduling(ShiftTime shiftTime, Date date, String branch, int employeeID) {
        return hrService.removeScheduling(shiftTime, date, branch, employeeID);

    }

    @Override
    public Response updateShiftManager(ShiftTime shiftTime, Date date, String branch, int employeeID) {
        return hrService.updateShiftManager(shiftTime, date, branch, employeeID);
    }

    @Override
    public Response addNewShift(ShiftTime shiftTime, Date date, String branch, int employeeID) {
        return hrService.addNewShift(shiftTime, date, branch, employeeID);
    }

    @Override
    public Response setPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) {
        return hrService.setPosition(shiftTime, date, branch, jobName, quantity);
    }

    @Override
    public Response addShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) {
        return hrService.addShiftPosition(shiftTime, date, branch, jobName, quantity);
    }

    @Override
    public Response removeShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName) {
        return hrService.removeShiftPosition(shiftTime, date, branch, jobName);
    }

    @Override
    public Response<String> getNumberOfShiftsStatistics(String branch) {
        return hrService.getNumberOfShiftsStatistics(branch);
    }

    @Override
    public Response<String> getNumberOfEveningShiftsStatistics(String branch) {
        return hrService.getNumberOfEveningShiftsStatistics(branch);
    }

    @Override
    public Response<String> getNumberOfMorningShiftsStatistics(String branch) {
        return hrService.getNumberOfMorningShiftsStatistics(branch);
    }

    @Override
    public Response<String> getHistory() {
        return hrService.getHistory();
    }

    public Response addConstraint(Date date, ShiftTime shiftTime) {
        return userService.addConstraint(date, shiftTime);
    }

    public Response deleteConstraint(Date date, ShiftTime shiftTime) {
        return userService.deleteConstraint(date, shiftTime);
    }
}
