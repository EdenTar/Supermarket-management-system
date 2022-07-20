package Backend.Logic.LogicObjects.Jobs;

import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;

import java.util.Date;

public class StoreManager extends Employee{
    public StoreManager(int employeeId, String employeeName, String employeeLastName, String password, Date startingDate, boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId, employeeName, employeeLastName, password, startingDate, new Job("store manager"), isShiftManager, bankAccount, employmentConditions, branch);
    }

    public StoreManager(int employeeId, String employeeName, String employeeLastName, Date startingDate, boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId, employeeName, employeeLastName, startingDate, new Job("store manager"), isShiftManager, bankAccount, employmentConditions, branch);
    }
}
