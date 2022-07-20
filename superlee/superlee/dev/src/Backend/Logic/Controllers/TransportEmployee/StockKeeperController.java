package Backend.Logic.Controllers.TransportEmployee;


import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Jobs.Employee;

import Backend.Logic.LogicObjects.Jobs.StockKeeper;


import java.util.Date;

public class StockKeeperController extends Controller{

    private StockKeeper loggedInStockKeeper;


    protected void login(int userId, String password) {
        containsEmployeeByIDAndPassword(userId, password);
        Employee e = getEmployeeByIDAndPassword(userId, password);
        if(!e.getJob().getJobName().equals("stock keeper")) {
            throw new IllegalArgumentException("you are not stock manger...");
        }else{
            loggedInStockKeeper = (StockKeeper) e;
        }
    }

    @Override
    protected void logout(int userId) {
        if(!isLoggedIn(userId)) throw new RuntimeException("The user is not logged in.");
        loggedInStockKeeper = null;
    }

    @Override
    protected boolean isLoggedIn(int id) {
        return isLoggedIn() && loggedInStockKeeper.getEmployeeId() == id;
    }

    @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInStockKeeper.addConstraint(date,shiftTime);
    }

    @Override
    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn()) throw new RuntimeException("Employee is not logged in");
        loggedInStockKeeper.deleteConstraint(date,shiftTime);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        loggedInStockKeeper.changePassword(oldPassword,newPassword);
    }

    @Override
    protected boolean isLoggedIn() {
        return this.loggedInStockKeeper != null;
    }
}
