package Backend.ServiceLayer.Services;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.Utilities.Pair;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.ServiceObjects.Transport.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface TransportEmployeeServiceAPI {

    Response login(int userId, String password, String type);

    Response logout(int userId);

    Response<List<ServiceTransportFile>> showInProgressTransports();

    Response<ServiceTransportFile> getTransportFile(int id);

    Response<OrderTransportService> getOrderTransport(int id);

    Response<List<String>> getAllPointInfo();

    Response<List<OrderTransportService>> showAllTransportRequests();

    Response addTruck(int truckId, String model, int currentWeight, int maxWeight, LicenseService l);

    Response deleteTruck(int truckId);
    Response<String> showAndNotifyMatchDates(Integer orderId);

    Response<List<ServiceTruck>> showAllAvailableTrucks(Date from, Date to);


    Response createTransportFile(Date startDate, Date endDate, int truckId, String source, String from,
                                 String to, List<Integer> orderId, List<Date> arrivalDates);

    Response<List<ServiceDriver>> showAvailableDrivers(Date from, Date to);

    Response updateLicense(String license);

    Response removeItems(String destinationId, List<Pair<String, Integer>> list);

    Response changeTruck(int truckId, int transportFileId);

    Response changeDriver(int userId, int transportFileId);

    Response cancelTransport(int transportFileId);

    Response removeDestination(int destinationFileId, String destination);

    Response<?> addComment(String comment, int transportId);

    Response<ServiceDestinationFile> getDestinationFile(String destinationFileId);

    Response<List<ServiceTransportFile>> showOldTransports();

    Response<OrderTransportService> getRequestsByPriority();

    Response<List<OrderTransportService>> getRequestsByZone(String fromZone, String toZone);

    Response weightTruck(int weight);

    Response finishDestinationFile(String destinationId);

    Response<LicenseService[]> showAvailableLicenses();

    Response<String> showDetails();

    Response insertBranch(String address, String phone, String contactName, String zone);

    Response insertSupplier(String address, String phone, String contactName, String zone);

    Response setStarted(int transportId);

    Response<Boolean> isLoggedIn(int userId, String type);

    Response addNewJob(String jobName);

    Response addNewEmployee(String firstName, String lastName, Date date, int id, String jobName, boolean isShiftManager,
                            int bankNumber, int accountNumber, int bankBranch, double salary, String socialBenefits, String branchAddress);

    Response removeEmployee(int id);

    Response updateBankNumber(int id, int newBankNumber);

    Response updateAccountNumber(int id, int newAccountNumber);

    Response updateBankBranch(int id, int newBankBranch);

    Response<HashMap<Integer, String>> showForEachEmployeeHisJob();

    Response<LinkedList<String>> showEmployees();

    Response updateSalary(int id, double newSalary);

    Response addSocialBenefits(int id, String socialBenefits);

    Response addNewSocialBenefits(int id, String socialBenefits);

    Response updateFirstName(int id, String newFirstName);

    Response updateLastName(int id, String newLastName);

    Response changePassword(String oldPassword, String newPassword);

    Response updateIsShiftManager(int id, boolean isShiftManager);

    Response<String> getEmployeeConstrains(int employeeID);


    Response addScheduling(ShiftTime shiftTime, Date date, String branch, int employeeID, String jobName);

    Response removeScheduling(ShiftTime shiftTime, Date date, String branch, int employeeID);

    Response updateShiftManager(ShiftTime shiftTime, Date date, String branch, int employeeID);

    Response addNewShift(ShiftTime shiftTime, Date date, String branch, int employeeID);

    Response setPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity);

    Response addShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity);

    Response removeShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName);

    Response<String> getNumberOfShiftsStatistics(String branch);

    Response<String> getNumberOfEveningShiftsStatistics(String branch);

    Response<String> getNumberOfMorningShiftsStatistics(String branch);

    Response<String> getHistory();

    Response scheduleDriver();

    Response<String> getShiftPlacements(Date date, ShiftTime shiftTime, String branch);

    Response addConstraint(Date date, ShiftTime shiftTime);

    Response deleteConstraint(Date date, ShiftTime shiftTime);

}
