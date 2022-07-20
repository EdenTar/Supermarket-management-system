package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.EmployeeDAOS.BankAccountDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.EmployeeDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.EmploymentConditionsDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.JobDAO;
import Backend.DataAccess.DAOs.TransportDAOs.PointDAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.JobsDTO;
import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Jobs.*;
import Backend.Logic.Points.Point;
import Backend.Logic.Points.Supplier;

import java.util.*;

public class EmployeeController {
    private EmployeeDAO employeeDAO;
    private JobDAO jobDAO;
    private PointDAO pointDAO;
    private BankAccountDAO bankAccountDAO;
    private EmploymentConditionsDAO employmentConditionsDAO;
    private final String driver = "driver";
    private final String HRManager = "hr manager";
    private final String transportManager = "transport manager";
    private final String StockKeeper = "stock keeper";
    private final String StoreManager = "store manager";
    private final String SupplierManager = "supplier manager";

    public EmployeeController() {
        employeeDAO = new EmployeeDAO();
        jobDAO = new JobDAO();
        pointDAO = new PointDAO();
        bankAccountDAO = new BankAccountDAO();
        employmentConditionsDAO = new EmploymentConditionsDAO();
    }

    public Employee getEmployee(int id) throws Exception {
        Employee toReturn = employeeDAO.getRow(EmployeeDTO.getPK(id));
        if (toReturn == null)
            throw new Exception("There is no such employee");
        return toReturn;
    }

    public boolean isEmployee(int id) {
        return employeeDAO.getRow(EmployeeDTO.getPK(id)) != null;
    }

    public boolean isAJob(String jobName) {
        return jobDAO.getRow(JobsDTO.getPK(jobName)) != null;

    }

    public void addNewJob(String jobName) throws Exception {
        if (jobName == null)
            throw new IllegalArgumentException("Job name can't be null");
        if (isAJob(jobName))
            throw new Exception("Job already Exists");
        jobDAO.insert(new Job(jobName.toLowerCase()));
    }

    public void addNewEmployee(String firstName, String lastName, Date date, int id, String jobName, boolean isShiftManager, int bankNumber, int accountNumber, int bankBranch, double salary, String socialBenefits, String branchAddress, DriverController driverController, TransportManagerController transportManagerController, EmployeeController employeeController, ShiftController shiftController) throws Exception {
        if (date.compareTo(new Date()) > 0)
            throw new Exception("starting date is invalid.");
        if (isEmployee(id))
            throw new Exception("Employee already Exists.");
        if (!isAJob(jobName))
            throw new Exception("The job does not exists.");
        Point branch = pointDAO.getRow(PointDTO.getPK(branchAddress));
        if(branchAddress.equals("general") && !jobName.equals(transportManager) && !jobName.equals(HRManager) && !jobName.equals(driver)){
            throw new Exception("this type of employee must have a regular branch");
        }
        if(!branchAddress.equals("general") && (jobName.equals(transportManager) || jobName.equals(HRManager) || jobName.equals(driver))){
            throw new Exception("this type of employee must have a general branch");
        }
        if ((branch == null || branch instanceof Supplier) && !branchAddress.equals("general"))
            throw new Exception("there isn't such branch");
        switch (jobName) {
            case driver:
                driverController.register(id, firstName, lastName, date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits));
                break;
            case transportManager:
                transportManagerController.register(id, firstName, lastName, date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits));
                break;
            case StockKeeper:
                employeeDAO.insert(new StockKeeper(id, firstName, lastName, date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits), branchAddress));
                break;
            case StoreManager:
                employeeDAO.insert(new StoreManager(id, firstName, lastName, date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits), branchAddress));
                break;
            case SupplierManager:
                employeeDAO.insert(new SupplierManager(id, firstName, lastName, date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits), branchAddress));
                break;
            case HRManager:
                employeeDAO.insert(new HrManager(id, date, firstName, lastName, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits), shiftController, employeeController));
                break;
            default:
                employeeDAO.insert(new BasicEmployee(id, firstName, lastName, new Job(jobName), date, isShiftManager, new BankAccount(id, bankNumber, accountNumber, bankBranch), new EmploymentConditions(id, salary, socialBenefits), branchAddress));
                break;
        }
    }

    public void removeEmployee(int id, DriverController driverController, TransportManagerController transportManagerController) throws Exception {
        Employee toRemove = getEmployee(id);
        switch (getEmployee(id).getJob().getJobName()) {
            case driver:
                driverController.removeDriver(id);
                break;
            case transportManager:
                transportManagerController.removeTransportManager(id);
                break;
            default:
                employeeDAO.deleteRow(toRemove);
                break;
        }
    }

    public void updateBankNumber(int id, int newBankNumber) throws Exception {
        Employee employee = getEmployee(id);
        employee.getBankAccount().setBankNumber(newBankNumber);
        bankAccountDAO.update(employee.getBankAccount());
    }

    public void updateAccountNumber(int id, int newAccountNumber) throws Exception {
        Employee employee = getEmployee(id);
        employee.getBankAccount().setAccountNumber(newAccountNumber);
        bankAccountDAO.update(employee.getBankAccount());
    }

    public void updateBankBranch(int id, int newBankBranch) throws Exception {
        Employee employee = getEmployee(id);
        employee.getBankAccount().setBankBranch(newBankBranch);
        bankAccountDAO.update(employee.getBankAccount());
    }

    public HashMap<Integer, String> showForEachEmployeeHisJob() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        List<Employee> employees = employeeDAO.selectAllRowsToBusiness();
        for (Employee employee : employees) {
            hashMap.put(employee.getEmployeeId(), employee.getJob().getJobName());
        }
        return hashMap;
    }

    public LinkedList<String> showEmployees() {
        LinkedList<String> employeeLinkedList = new LinkedList<>();
        List<Employee> employees = employeeDAO.selectAllRowsToBusiness();
        for (Employee employee : employees) {
            employeeLinkedList.add(employee.toString());
        }
        return employeeLinkedList;

    }

    public void updateSalary(int id, double newSalary) throws Exception {
        Employee employee = getEmployee(id);
        employee.getEmploymentConditions().setSalary(newSalary);
        employmentConditionsDAO.update(employee.getEmploymentConditions());
    }

    public void addSocialBenefits(int id, String socialBenefits) throws Exception {
        if (socialBenefits == null)
            throw new NullPointerException("social benefits can't be null");
        Employee employee = getEmployee(id);
        employee.addSocialBenefits(socialBenefits);
        employmentConditionsDAO.update(employee.getEmploymentConditions());
    }

    public void addNewSocialBenefits(int id, String socialBenefits) throws Exception {
        if (socialBenefits == null)
            throw new NullPointerException("social benefits can't be null");
        Employee employee = getEmployee(id);
        employee.setNewSocialBenefits(socialBenefits);
        employmentConditionsDAO.update(employee.getEmploymentConditions());
    }

    public String getEmployeeConstrains(int employeeID) throws Exception {
        return getEmployee(employeeID).getEmployeeConstrains();
    }

    public void updateIsShiftManager(int id, boolean isShiftManager) throws Exception {
        Employee employee = getEmployee(id);
        employee.updateIsShiftManager(isShiftManager);
        employeeDAO.update(employee);
    }

    public void updateLastName(int id, String newLastName) throws Exception {
        if (newLastName == null)
            throw new IllegalArgumentException("Name can't be null");
        Employee employee = getEmployee(id);
        employee.setEmployeeLastName(newLastName);
        employeeDAO.update(employee);
    }

    public void updateFirstName(int id, String newFirstName) throws Exception {
        if (newFirstName == null)
            throw new IllegalArgumentException("Name can't be null");
        Employee employee = getEmployee(id);

        employee.setEmployeeName(newFirstName);
        employeeDAO.update(employee);
    }
}
