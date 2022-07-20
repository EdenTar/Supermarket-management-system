package Backend.Logic.Controllers.TransportEmployee;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Jobs.HrManager;

import java.util.*;

public class HRController extends Controller {
    private HrManager loggedInHrManager;
    private DriverController driverController;
    private TransportManagerController transportManagerController;
    private ShiftController shiftController;
    private EmployeeController employeeController;

    public HRController(ShiftController shiftController, EmployeeController employeeController) {
        super();
        loggedInHrManager = null;
        this.transportManagerController = null;
        this.driverController = null;
        this.shiftController = shiftController;
        this.employeeController = employeeController;
    }

    public void initialize(DriverController driverController, TransportManagerController transportManagerController) {
        this.driverController = driverController;
        this.transportManagerController = transportManagerController;

    }

    @Override
    protected boolean isLoggedIn(int id) {
        return isLoggedIn() && id == this.loggedInHrManager.getEmployeeId();
    }

    public void addConstraints(Date date, ShiftTime shiftTime) throws Exception {
        loggedInHrManager.addConstraint(date, shiftTime);
    }

    public void login(int id, String password) throws Exception {
        containsEmployeeByIDAndPassword(id, password);
        Employee e = getEmployeeByIDAndPassword(id, password);
        if (e.getJob().getJobName().equals("hr manager"))
            loggedInHrManager = (HrManager) e;
        else
            throw new Exception("you are not HR manager...");
    }

    protected void logout(int userId) {
        if (!isLoggedIn() || userId != this.loggedInHrManager.getEmployeeId())
            throw new RuntimeException("The user is not logged in.");
        this.loggedInHrManager = null;

    }

    public boolean isLoggedIn() {
        return this.loggedInHrManager != null;
    }

    public void addScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee, String jobName) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.addScheduling(shiftTime, date, branch, employee, jobName);
    }

    public void removeScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.removeScheduling(shiftTime, date, branch, employee);
    }

    public void updateShiftManager(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.updateShiftManager(shiftTime, date, branch, employee);
    }

    public void addNewShift(ShiftTime shiftTime, Date date, String branch, Employee shiftManager) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.addNewShift(shiftTime, date, branch, shiftManager);
    }

    public void addShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.addShiftPosition(shiftTime, date, branch, jobName, quantity);
    }

    public void removeShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.removeShiftPosition(shiftTime, date, branch, jobName);
    }

    public void setPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        loggedInHrManager.setPosition(shiftTime, date, branch, jobName, quantity);
    }

    public String getHistory() throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        return loggedInHrManager.getHistory();
    }

    public String getShiftPlacements(Date date, ShiftTime shiftTime, String branch) {

        return loggedInHrManager.getShiftPlacements(date, shiftTime, branch);
    }

    public String getNumberOfShiftsStatistics(String branch) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        return loggedInHrManager.getNumberOfShiftsStatistics(branch);
    }

    public String getNumberOfMorningShiftsStatistics(String branch) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        return loggedInHrManager.getNumberOfMorningShiftsStatistics(branch);
    }

    public String getNumberOfEveningShiftsStatistics(String branch) throws Exception {
        if (!isLoggedIn()) throw new Exception("Hr Manager is not login");
        return loggedInHrManager.getNumberOfEveningShiftsStatistics(branch);
    }

    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Hr Manager is not logged in");
        loggedInHrManager.addConstraint(date, shiftTime);
    }

    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Hr Manager is not logged in");
        loggedInHrManager.deleteConstraint(date, shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        loggedInHrManager.changePassword(oldPassword, newPassword);
    }

    public void addNewJob(String jobName) throws Exception {
        if (loggedInHrManager == null) throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.addNewJob(jobName);
    }

    public void addNewEmployee(String firstName, String lastName, Date date, int id, String jobName, boolean isShiftManager,
                               int bankNumber, int accountNumber, int bankBranch, double salary, String socialBenefits, String branchAddress) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.addNewEmployee(firstName, lastName, date, id, jobName, isShiftManager, bankNumber, accountNumber, bankBranch, salary, socialBenefits, branchAddress, driverController, transportManagerController, shiftController, employeeController);
    }

    public void removeEmployee(int id) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.removeEmployee(id, driverController, transportManagerController);
    }

    public void updateBankNumber(int id, int newBankNumber) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateBankNumber(id, newBankNumber);
    }

    public void updateAccountNumber(int id, int newAccountNumber) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateAccountNumber(id, newAccountNumber);

    }

    public void updateBankBranch(int id, int newBankBranch) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateBankBranch(id, newBankBranch);

    }

    public HashMap<Integer, String> showForEachEmployeeHisJob() throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        return loggedInHrManager.showForEachEmployeeHisJob();
    }

    public void addNewSocialBenefits(int id, String socialBenefits) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.addNewSocialBenefits(id, socialBenefits);
    }

    public void addSocialBenefits(int id, String socialBenefits) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.addSocialBenefits(id, socialBenefits);

    }

    public void updateSalary(int id, double newSalary) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateSalary(id, newSalary);

    }

    public LinkedList<String> showEmployees() throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        return loggedInHrManager.showEmployees();

    }

    public String getEmployeeConstrains(int employeeID) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        return loggedInHrManager.getEmployeeConstrains(employeeID);
    }

    public void updateIsShiftManager(int id, boolean isShiftManager) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateIsShiftManager(id, isShiftManager);
    }

    public void updateLastName(int id, String newLastName) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateLastName(id, newLastName);
    }

    public ShiftController getShiftController() {
        return shiftController;
    }

    public void updateFirstName(int id, String newFirstName) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        loggedInHrManager.updateFirstName(id, newFirstName);
    }

    public Employee getEmployee(int id) throws Exception {
        return loggedInHrManager.getEmployee(id);
    }

    public boolean isJobExists(String jobName) throws Exception {
        if (loggedInHrManager == null)
            throw new Exception("Hr Manager is not logged in");
        return loggedInHrManager.isJobExists(jobName);
    }
    public void scheduleDriver() throws Exception {
        loggedInHrManager.scheduleDriver();
    }
}
