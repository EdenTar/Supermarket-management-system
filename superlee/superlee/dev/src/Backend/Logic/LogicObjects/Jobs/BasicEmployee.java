package Backend.Logic.LogicObjects.Jobs;

import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;

import java.util.Date;


public class BasicEmployee extends Employee{

    public BasicEmployee(int employeeId, String employeeName, String employeeLastName, Job job, Date startingDate, boolean isShiftManager,
                         BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId,employeeName,employeeLastName,startingDate,job,isShiftManager,bankAccount, employmentConditions, branch);

    }
    public BasicEmployee(int employeeId, String employeeName, String employeeLastName,String password, Job job, Date startingDate, boolean isShiftManager,
                         BankAccount bankAccount, EmploymentConditions employmentConditions, String branch) {
        super(employeeId,employeeName,employeeLastName,password,startingDate,job,isShiftManager,bankAccount, employmentConditions, branch);
    }

}
