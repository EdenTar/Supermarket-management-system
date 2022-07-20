package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.EmployeeDAOS.EmployeeDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.JobDAO;
import Backend.Logic.LogicObjects.Jobs.BasicEmployee;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Employee.ShiftTime;

import java.util.Date;

public class BasicEmployeeController extends Controller {

    private JobDAO jobDao;
    private EmployeeDAO employeeDAO;
    private BasicEmployee loggedInBasicEmployee;

    public BasicEmployeeController(){
        this.loggedInBasicEmployee = null;
       employeeDAO=new EmployeeDAO();
    }

    public boolean isLoggedIn(){
        return this.loggedInBasicEmployee != null;
    }

    public void login(int id, String password) throws Exception {
        containsEmployeeByIDAndPassword(id, password);
        Employee e = getEmployeeByIDAndPassword(id, password);
        if(e.getJob().getJobName().equals("hr manager") || e.getJob().getJobName().equals("driver")||
                e.getJob().getJobName().equals("order man") || e.getJob().getJobName().equals("transport manager"))
            throw new Exception("you are not basic Employee...");
        else
            loggedInBasicEmployee =(BasicEmployee) e;



//        Employee basicEmployee=employeeDAO.getRow(new EmployeePrimaryKey(id));
//        if(basicEmployee==null)
//            throw new IllegalArgumentException(id+" is not register in the system");
//        if(!password.equals( basicEmployee.getPassword()))
//            throw new TransportException("password is not correct");
//        loggedInBasicEmployee =(BasicEmployee) basicEmployee;
    }

    @Override
    protected void logout(int userId) {
        if(!isLoggedIn() ||  userId != this.loggedInBasicEmployee.getEmployeeId())throw new RuntimeException("The user is not logged in.");
        this.loggedInBasicEmployee = null;

    }

    @Override
    protected boolean isLoggedIn(int id) {
        return false;
    }

    @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInBasicEmployee.addConstraint(date,shiftTime);
    }

    @Override
    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInBasicEmployee.deleteConstraint(date,shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInBasicEmployee.changePassword(oldPassword,newPassword);
    }
}
