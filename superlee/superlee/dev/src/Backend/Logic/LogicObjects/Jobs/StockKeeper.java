package Backend.Logic.LogicObjects.Jobs;


import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;

import java.util.Date;

public class StockKeeper extends Employee{

    public StockKeeper(int employeeId, String employeeName, String employeeLastName, String password, Date startingDate, boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId, employeeName, employeeLastName, password, startingDate, new Job("stock keeper"), isShiftManager, bankAccount, employmentConditions, branch);
    }

    public StockKeeper(int employeeId, String employeeName, String employeeLastName, Date startingDate, boolean isShiftManager, BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId, employeeName, employeeLastName, startingDate, new Job("stock keeper"), isShiftManager, bankAccount, employmentConditions, branch);
    }
}
