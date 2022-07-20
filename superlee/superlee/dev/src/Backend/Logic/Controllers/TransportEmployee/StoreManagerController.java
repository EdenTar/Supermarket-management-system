package Backend.Logic.Controllers.TransportEmployee;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Jobs.StoreManager;

import java.util.Date;

public class StoreManagerController extends Controller{

    private StoreManager loggedInStoreManager;

    protected void login(int userId, String password) {
        containsEmployeeByIDAndPassword(userId, password);
        Employee e = getEmployeeByIDAndPassword(userId, password);
        if(!e.getJob().getJobName().equals("store manager")) {
            throw new IllegalArgumentException("you are not store manger...");
        }else {
            this.loggedInStoreManager = (StoreManager) e;
        }

    }

    @Override
    protected void logout(int userId) {
        if(!isLoggedIn(userId)) throw new RuntimeException("The user is not logged in.");
        loggedInStoreManager = null;
    }

    @Override
    protected boolean isLoggedIn(int id) {
        return isLoggedIn() && loggedInStoreManager.getEmployeeId() == id;
    }

    @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInStoreManager.addConstraint(date,shiftTime);
    }

    @Override
    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInStoreManager.deleteConstraint(date,shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        loggedInStoreManager.changePassword(oldPassword,newPassword);
    }

    @Override
    protected boolean isLoggedIn() {
        return this.loggedInStoreManager != null;
    }
}
