package Backend.Logic.LogicObjects.Jobs;

import Backend.Logic.Controllers.TransportEmployee.EmployeeController;
import Backend.Logic.Controllers.TransportEmployee.ShiftController;
import Backend.Logic.Controllers.TransportEmployee.DriverController;
import Backend.Logic.Controllers.TransportEmployee.OrderManController;
import Backend.Logic.Controllers.TransportEmployee.TransportManagerController;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Employee.ShiftTime;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class HrManager extends Employee {
    private EmployeeController employeeController;
    private ShiftController shiftController;


    public HrManager(int employeeId, Date startingDate, String employeeName, String employeeLastName,
                     boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, ShiftController shiftController, EmployeeController employeeController) {
        super(employeeId, employeeName, employeeLastName, startingDate, new Job("hr manager"), isShiftManager,
                bankAccount, employmentConditions, "general");
        this.employeeController = employeeController;
        this.shiftController = shiftController;

    }

    public HrManager(int employeeId, Date startingDate, String employeeName, String employeeLastName, String password,
                     boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, ShiftController shiftController, EmployeeController employeeController) {
        super(employeeId, employeeName, employeeLastName, password, startingDate, new Job("hr manager"), isShiftManager,
                bankAccount, employmentConditions, "general");
        this.employeeController = employeeController;
        this.shiftController = shiftController;

    }

    public void addNewJob(String jobName) throws Exception {
        employeeController.addNewJob(jobName);
    }

    public void addNewEmployee(String firstName, String lastName, Date date, int id, String jobName, boolean isShiftManager,
                               int bankNumber, int accountNumber, int bankBranch, double salary, String socialBenefits, String branchAddress, DriverController driverController, TransportManagerController transportManagerController, ShiftController shiftController, EmployeeController employeeController) throws Exception {
        employeeController.addNewEmployee(firstName, lastName, date, id, jobName, isShiftManager, bankNumber, accountNumber, bankBranch, salary, socialBenefits, branchAddress, driverController, transportManagerController, employeeController, shiftController);
    }

    public void removeEmployee(int id, DriverController driverController, TransportManagerController transportManagerController) throws Exception {
        boolean isAssignedNextWeek = shiftController.isAssignedNextWeek(getEmployee(id));
        if (isAssignedNextWeek)
            throw new Exception("you can't remove an employee that has assignments for next week");
        else {
            employeeController.removeEmployee(id, driverController, transportManagerController);
            shiftController.deleteAllConstraints(id);
        }
    }


    public void updateBankNumber(int id, int newBankNumber) throws Exception {
        employeeController.updateBankNumber(id, newBankNumber);
    }

    public void updateAccountNumber(int id, int newAccountNumber) throws Exception {
        employeeController.updateAccountNumber(id, newAccountNumber);
    }

    public void updateBankBranch(int id, int newBankBranch) throws Exception {
        employeeController.updateBankBranch(id, newBankBranch);
    }

    public HashMap<Integer, String> showForEachEmployeeHisJob() {
        return employeeController.showForEachEmployeeHisJob();
    }

    public LinkedList<String> showEmployees() {
        return employeeController.showEmployees();
    }

    public void updateSalary(int id, double newSalary) throws Exception {
        employeeController.updateSalary(id, newSalary);
    }

    public void addSocialBenefits(int id, String socialBenefits) throws Exception {
        employeeController.addSocialBenefits(id, socialBenefits);
    }

    public void addNewSocialBenefits(int id, String socialBenefits) throws Exception {
        employeeController.addNewSocialBenefits(id, socialBenefits);
    }

    public void updateFirstName(int id, String newFirstName) throws Exception {
        employeeController.updateFirstName(id, newFirstName);
    }

    public void updateLastName(int id, String newLastName) throws Exception {
        employeeController.updateLastName(id, newLastName);
    }

    public void updateIsShiftManager(int id, boolean isShiftManager) throws Exception {
        employeeController.updateIsShiftManager(id, isShiftManager);
    }

    public void addScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee, String jobName) throws Exception {
        shiftController.addScheduling(shiftTime, date, branch, employee, jobName,false);
    }

    public void removeScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {
        shiftController.removeScheduling(shiftTime, date, branch, employee);
    }

    public void updateShiftManager(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {
        shiftController.updateShiftManager(shiftTime, date, branch, employee);
    }

    public void addNewShift(ShiftTime shiftTime, Date date, String branch, Employee shiftManager) throws Exception {
        shiftController.addNewShift(shiftTime, date, branch, shiftManager);
    }

    public void addShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {
        shiftController.addShiftPosition(shiftTime, date, branch, jobName, quantity);
    }

    public void removeShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName) throws Exception {
        shiftController.removeShiftPosition(shiftTime, date, branch, jobName);
    }

    public void setPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {
        shiftController.setPosition(shiftTime, date, branch, jobName, quantity);
    }

    public String getHistory() throws Exception {
        return shiftController.getHistory();
    }

    public String getNumberOfShiftsStatistics(String branch) throws Exception {
        return shiftController.getNumberOfShiftsStatistics(branch);
    }

    public String getNumberOfMorningShiftsStatistics(String branch) throws Exception {
        return shiftController.getNumberOfMorningShiftsStatistics(branch);
    }

    public String getNumberOfEveningShiftsStatistics(String branch) throws Exception {
        return shiftController.getNumberOfEveningShiftsStatistics(branch);
    }

    public String getEmployeeConstrains(int employeeID) throws Exception {
        return employeeController.getEmployeeConstrains(employeeID);
    }

    public Employee getEmployee(int id) throws Exception {
        return employeeController.getEmployee(id);//TODO:get employee from his dao
    }

    public EmployeeController getEmployeeController() {
        return employeeController;
    }

    public ShiftController getShiftController() {
        return shiftController;
    }

    public boolean isJobExists(String jobName) {
        return employeeController.isAJob(jobName);
    }

    public String getShiftPlacements(Date date, ShiftTime shiftTime, String branch) {
        return shiftController.getShiftPlacements(date, shiftTime, branch);
    }

    public void scheduleDriver() throws Exception {
        shiftController.scheduleDriver();
    }

}
