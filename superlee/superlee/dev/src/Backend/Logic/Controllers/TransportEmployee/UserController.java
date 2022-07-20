package Backend.Logic.Controllers.TransportEmployee;


import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.Exceptions.Transport.TransportException;

import java.util.Date;

public class UserController extends Controller {
    private final OrderManController orderManController;
    private final TransportManagerController transportManagerController;
    private final DriverController driverController;
    private final HRController hrController;
    private final BasicEmployeeController basicEmployeeController;
    private final StockKeeperController stockKeeperController;
    private final StoreManagerController storeManagerController;
    private final SupplierManagerController supplierManagerController;


    private final String manager = "TRANSPORT_MANAGER";
    private final String driver = "DRIVER";
    private final String hrManager = "HR_MANAGER";
    private final String basicEmployee = "BASIC_EMPLOYEE";
    private final String storeManager = "STORE_MANAGER";
    private final String stockKeeper = "STOCK_KEEPER";
    private final String supplierManager = "SUPPLIER_MANAGER";

    private boolean isLoggedIn;

    public UserController(OrderManController orderManController,
                          TransportManagerController transportManagerController,
                          DriverController driverController, HRController hrController,
                          BasicEmployeeController basicEmployeeController, StockKeeperController stockKeeperController,
                          StoreManagerController storeManagerController, SupplierManagerController supplierManagerController) {

        this.isLoggedIn = false;
        this.orderManController = orderManController;
        this.transportManagerController = transportManagerController;
        this.driverController = driverController;
        this.hrController = hrController;
        this.basicEmployeeController = basicEmployeeController;
        this.storeManagerController = storeManagerController;
        this.supplierManagerController = supplierManagerController;
        this.stockKeeperController = stockKeeperController;
    }


    public boolean isLoggedIn(int userId, String type) {
        switch (type) {
            case manager:
                return transportManagerController.isLoggedIn(userId);
            case driver:
                return driverController.isLoggedIn(userId);

            case hrManager:
                return hrController.isLoggedIn(userId);

            case basicEmployee:
                return basicEmployeeController.isLoggedIn(userId);

            case storeManager:
                return storeManagerController.isLoggedIn(userId);

            case stockKeeper:
                return stockKeeperController.isLoggedIn(userId);

            case supplierManager:
                return supplierManagerController.isLoggedIn(userId);
            default:
                throw new IllegalArgumentException("Not Valid type: " + type);
        }
    }



    /*
     * types: ,manager,orderMan
     * */

//    public void register(String userName, String password, String firstName, String lastName, String userId, String type) {
//        switch (type) {
//            case manager:
//                transportManagerController.register(userName, password, firstName, lastName, userId);
//                break;
//            case orderMan:
//                orderManController.register(userName, password, firstName, lastName, userId);
//                break;
//            case driver:
//                driverController.register(userName, password, firstName, lastName, userId);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Not Valid type: " + type);
//        }
    // }//


    public void login(int userId, String password, String type) throws Exception {

        if (isLoggedIn) throw new RuntimeException("There is an already logged in user.");


        if (type == null) throw new IllegalArgumentException("Login error");
        switch (type) {
            case manager:
                transportManagerController.login(userId, password);
                break;

            case driver:
                driverController.login(userId, password);
                break;

            case hrManager:
                hrController.login(userId, password);
                break;

            case basicEmployee:
                basicEmployeeController.login(userId, password);
                break;

            case storeManager:
                storeManagerController.login(userId, password);
                break;

            case stockKeeper:
                stockKeeperController.login(userId, password);
                break;

            case supplierManager:
                supplierManagerController.login(userId, password);
                break;
        }
        this.isLoggedIn = true;
    }

    private String getType() {
        return driverController.isLoggedIn() ? driver :
                        transportManagerController.isLoggedIn() ? manager :
                                hrController.isLoggedIn() ? hrManager :
                                        basicEmployeeController.isLoggedIn() ? basicEmployee :
                                                stockKeeperController.isLoggedIn() ? stockKeeper :
                                                        storeManagerController.isLoggedIn() ? storeManager :
                                                                supplierManagerController.isLoggedIn() ? supplierManager :
                                                                        null;
    }

    public void logout(int userId) {
        if (!isLoggedIn) throw new RuntimeException("There is no user logged in .");

        String type = getType();
        if (type == null) throw new IllegalArgumentException("Not Valid type: ");
        switch (type) {
            case manager:
                transportManagerController.logout(userId);
                break;

            case driver:
                driverController.logout(userId);
                break;

            case hrManager:
                hrController.logout(userId);
                break;

            case basicEmployee:
                basicEmployeeController.logout(userId);
                break;

            case storeManager:
                storeManagerController.logout(userId);
                break;

            case stockKeeper:
                stockKeeperController.logout(userId);
                break;

            case supplierManager:
                supplierManagerController.logout(userId);
                break;
        }
        this.isLoggedIn = false;
    }

    /**
     * @param userId user identifier
     */
    @Override
    protected boolean isLoggedIn(int userId) {
        throw new TransportException("Please LogIn");
    }



  /*  @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        throw new TransportException("Login First");

    }*/


    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (!isLoggedIn) throw new RuntimeException("There is no user logged in .");
        String type = getType();
        if (type == null) throw new IllegalArgumentException("Not Valid type: ");
        switch (type) {
            case manager:
                transportManagerController.changePassword(oldPassword, newPassword);
                break;

            case driver:
                driverController.changePassword(oldPassword, newPassword);
                break;

            case hrManager:
                hrController.changePassword(oldPassword, newPassword);
                break;

            case basicEmployee:
                basicEmployeeController.changePassword(oldPassword, newPassword);
                break;

            case storeManager:
                storeManagerController.changePassword(oldPassword, newPassword);
                break;

            case stockKeeper:
                stockKeeperController.changePassword(oldPassword, newPassword);
                break;

            case supplierManager:
                supplierManagerController.changePassword(oldPassword, newPassword);
                break;
        }
    }

    @Override
    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn) throw new RuntimeException("There is no user logged in .");
        String type = getType();
        if (type == null) throw new IllegalArgumentException("Not a Valid type.");
        switch (type) {
            case manager:
                transportManagerController.addConstraint(date, shiftTime);
                break;
            case driver:
                driverController.addConstraint(date, shiftTime);
                break;

            case hrManager:
                hrController.addConstraint(date, shiftTime);
                break;

            case basicEmployee:
                basicEmployeeController.addConstraint(date, shiftTime);
                break;

            case storeManager:
                storeManagerController.addConstraint(date, shiftTime);
                break;

            case stockKeeper:
                stockKeeperController.addConstraint(date, shiftTime);
                break;

            case supplierManager:
                supplierManagerController.addConstraint(date, shiftTime);
                break;
        }
    }


    @Override
    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {
        if (!isLoggedIn) throw new RuntimeException("There is no user logged in .");
        String type = getType();
        if (type == null) throw new IllegalArgumentException("Not Valid type: ");
        switch (type) {
            case manager:
                transportManagerController.deleteConstraint(date, shiftTime);
                break;

            case driver:
                driverController.deleteConstraint(date, shiftTime);
                break;

            case hrManager:
                hrController.deleteConstraint(date, shiftTime);
                break;

            case basicEmployee:
                basicEmployeeController.deleteConstraint(date, shiftTime);
                break;

            case storeManager:
                storeManagerController.deleteConstraint(date, shiftTime);
                break;

            case stockKeeper:
                stockKeeperController.deleteConstraint(date, shiftTime);
                break;

            case supplierManager:
                supplierManagerController.deleteConstraint(date, shiftTime);
                break;
        }
    }

    @Override
    protected boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getManagerString() {
        return manager;
    }

    public String getDriverString() {
        return driver;
    }

    public String getHrManagerString() {
        return hrManager;
    }

    public String getBasicEmployeeString() {
        return basicEmployee;
    }
}
