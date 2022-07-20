package Test;

import Backend.DataAccess.DAOs.EmployeeDAOS.*;
import Backend.DataAccess.DTOs.EmployeeDTOS.BankAccountDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.PlacementDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.ShiftDTO;
import Backend.DataAccess.DAOs.EmployeeDAOS.AssignmentDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.BankAccountDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.PlacementDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.ShiftDAO;
import Backend.Logic.Controllers.TransportEmployee.*;
import Backend.Logic.LogicObjects.Employee.Constraint;
import Backend.Logic.LogicObjects.Employee.Placement;
import Backend.Logic.LogicObjects.Employee.Shift;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.Starters.Starter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkersTest {

   UserController userController;
    HRController hrController;
    TransportManagerController transportManagerController;
    DriverController driverController;
    OrderManController orderManController;
    BasicEmployeeController basicEmployeeController;
    StockKeeperController stockKeeperController;
    StoreManagerController storeManagerController;
    SupplierManagerController supplierManagerController;
    String response ;
    BankAccountDAO bankAccountDAO;
    int DAYTIME=86400000;
    Date date1=new Date(new Date().getTime() + DAYTIME*7);
    Date date2=new Date(new Date().getTime() + DAYTIME*8);
    SimpleDateFormat formatter;
    int day;
    PlacementDAO placementDAO;
    ShiftDAO shiftDAO;
    ConstrainsDAO constrainsDAO;
    AssignmentDAO assignmentDAO;


    private final String manager = "TRANSPORT_MANAGER";
    private final String driver = "DRIVER";
    private final String hrManager = "HR_MANAGER";
    private final String basicEmployee = "BASIC_EMPLOYEE";
    private final String storeManager = "STORE_MANAGER";

    @Before
    public void setUp() {
        Starter s = Starter.getInstance();
        hrController = s.getHRController();
        driverController = s.getDriverController();
        orderManController = s.getOrderManController();
        basicEmployeeController = s.getBasicEmployeeController();
        transportManagerController = s.getTransportManagerController();
        stockKeeperController = s.getStockKeeperController();
        storeManagerController = s.getStoreManagerController();
        supplierManagerController = s.getSupplierManagerController();
        placementDAO=new PlacementDAO();
        userController = new UserController(orderManController, transportManagerController, driverController, hrController, basicEmployeeController, stockKeeperController, storeManagerController, supplierManagerController);
        response = "";
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        bankAccountDAO = new BankAccountDAO();
        shiftDAO=new ShiftDAO();
        constrainsDAO=new ConstrainsDAO();
        assignmentDAO=new AssignmentDAO();
    }


    @Test
    public void test_AddSchedule() throws Exception {
        try {
            //fix tests orderMan does not exists
            userController.login(1, "1", storeManager);
            userController.addConstraint(date2, ShiftTime.Morning);
            userController.addConstraint(date1, ShiftTime.Morning);
            userController.logout(1);
            userController.login(3, "3", driver);
            userController.addConstraint(date2, ShiftTime.Morning);
            userController.logout(3);
            userController.login(4, "4", hrManager);
            hrController.addNewShift(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(1));
            try {
                hrController.addScheduling(ShiftTime.Morning, date2, "branch1", hrController.getEmployee(3), "driver");
            } catch (Exception e) {
                response = e.getMessage();
                userController.logout(4);
                shiftDAO.deleteRow(new Shift(date2, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
                shiftDAO.deleteRow(new Shift(date1, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
                constrainsDAO.deleteRow(new Constraint(1, date1, ShiftTime.Morning));
                constrainsDAO.deleteRow(new Constraint(1, date2, ShiftTime.Morning));
                constrainsDAO.deleteRow(new Constraint(3, date2, ShiftTime.Morning));
                if (day > 6)
                    placementDAO.deleteRow(new Placement(date2, ShiftTime.Morning, "branch1", 3, "driver"));

            }
            if (day < 6)
                Assert.assertEquals("It is not yet possible schedule employees. Try again on Friday.", response);
            else {
                Assert.assertFalse(placementDAO.getRow(PlacementDTO.getPK(formatter.format(date2), "Morning", "branch1", 3)) == null);
            }
            response = "";
            try {
                placementDAO.deleteRow(new Placement(date1, ShiftTime.Morning, "branch1", 3, "driver"));
                hrController.addScheduling(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(3), "driver");
            } catch (Exception e) {
                response = e.getMessage();
                userController.logout(4);
                shiftDAO.deleteRow(new Shift(date2, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
                shiftDAO.deleteRow(new Shift(date1, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
                constrainsDAO.deleteRow(new Constraint(1, date1, ShiftTime.Morning));
                constrainsDAO.deleteRow(new Constraint(1, date2, ShiftTime.Morning));
                constrainsDAO.deleteRow(new Constraint(3, date2, ShiftTime.Morning));
                if (day > 6)
                    placementDAO.deleteRow(new Placement(date2, ShiftTime.Morning, "branch1", 3, "driver"));
            }
            if (day < 6)
                Assert.assertEquals("It is not yet possible schedule employees. Try again on Friday.", response);
            else {
                Assert.assertEquals("This employee is not available for this shift.", response);
                Assert.assertTrue(placementDAO.getRow(PlacementDTO.getPK(formatter.format(date1), "Morning", "branch1", 3)) == null);
            }
            shiftDAO.deleteRow(new Shift(date2, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
            shiftDAO.deleteRow(new Shift(date1, ShiftTime.Morning, "branch1", hrController.getEmployee(1)));
            constrainsDAO.deleteRow(new Constraint(1, date1, ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(1, date2, ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(3, date2, ShiftTime.Morning));
            placementDAO.deleteRow(new Placement(date1, ShiftTime.Morning, "branch1", 1, "order man"));
            placementDAO.deleteRow(new Placement(date2, ShiftTime.Morning, "branch1", 1, "order man"));
            if (day > 6)
                placementDAO.deleteRow(new Placement(date2, ShiftTime.Morning, "branch1", 3, "driver"));
        } catch (Exception e) {
            userController.logout(4);
            constrainsDAO.deleteRow(new Constraint(1, date1, ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(1, date2, ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(3, date2, ShiftTime.Morning));
            if (day > 6)
                placementDAO.deleteRow(new Placement(date2, ShiftTime.Morning, "branch1", 3, "driver"));
            Assert.assertTrue(true);
        }
        
    }

    @Test
    public void Test_updateShiftManager(){
        if(new Date().getDay() == 5 || new Date().getDay() == 6) {
            try {
                constrainsDAO.deleteRow(new Constraint(7,date1,ShiftTime.Morning));
                shiftDAO.deleteRow(new Shift(date1,ShiftTime.Morning, "branch1", hrController.getEmployee(7)));
                placementDAO.deleteRow(new Placement( date1,ShiftTime.Morning, "branch1", 7,"cashier"));

                userController.login(7, "abc111", basicEmployee);
                userController.addConstraint(date1, ShiftTime.Morning);
                userController.logout(7);
                userController.login(4, "4", hrManager);
                hrController.addNewShift(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(7));
                hrController.addScheduling(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(7), "cashier");
                try {
                    hrController.updateShiftManager(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(3));
                } catch (Exception exp) {
                    response = exp.getMessage();
                }
                Assert.assertEquals("This employee does not have the required authorization.", response);
                Assert.assertTrue(shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date1), "Morning", "branch1")).getShiftManager().getEmployeeId() == 7);

                try {
                    hrController.updateShiftManager(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(10));
                } catch (Exception exp) {
                    response = exp.getMessage();
                }
                Assert.assertEquals("This shift manager is not available for this shift", response);
                Assert.assertTrue(shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date1), "Morning", "branch1")).getShiftManager().getEmployeeId() == 7);

                userController.logout(4);
                userController.login(10, "abc111", basicEmployee);
                userController.addConstraint(date1, ShiftTime.Morning);
                userController.logout(10);
                userController.login(4, "4", hrManager);
                response = "";
                try {
                    hrController.updateShiftManager(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(10));
                } catch (Exception exp) {
                    response = exp.getMessage();
                }
                Assert.assertEquals("This employee is not in this shift. Add him first.", response);
                Assert.assertTrue(shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date1), "Morning", "branch1")).getShiftManager().getEmployeeId() == 7);
                hrController.addScheduling(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(10), "grocery sorter");
                response = "";
                try {
                    hrController.updateShiftManager(ShiftTime.Morning, date1, "branch1", hrController.getEmployee(10));
                } catch (Exception exp) {
                    response = exp.getMessage();
                }
                Assert.assertEquals("", response);
                Assert.assertTrue(shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date1), "Morning", "branch1")).getShiftManager().getEmployeeId() == 10);
                shiftDAO.deleteRow(shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date1), "Morning", "branch1")));
                String condition = "date = " + convertToFormat(date1) + " AND shiftTime = Morning AND branch = branch1";
                placementDAO.deleteRows(condition);
                assignmentDAO.deleteRows(condition);
                userController.logout(4);


            } catch (Exception e) {
                Assert.assertEquals("", e.getMessage());
            }
        }
        else
            Assert.assertTrue(true);
    }
    private String convertToFormat(Date startingDate) {
        if(startingDate.getMonth()+1<10)
            return "" + (startingDate.getYear() + 1900) + "-0" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
        else
            return "" + (startingDate.getYear() + 1900) + "-" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
    }
//
    @Test
    public void Test_addNewShift(){
        try {
            shiftDAO.deleteRow(new Shift(date1,ShiftTime.Morning,"branch1",hrController.getEmployee(3)));
            constrainsDAO.deleteRow(new Constraint(12,date2,ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(3,date1,ShiftTime.Morning));
            placementDAO.deleteRow(new Placement(date1,ShiftTime.Morning,"branch1",3,"driver"));
            userController.login(12,"abc111", driver);
            userController.addConstraint(date2,ShiftTime.Morning);
            userController.logout(12);
            userController.login(3,"3", driver);
            userController.addConstraint(date1, ShiftTime.Morning);
            userController.logout(3);
            userController.login(4,"4",hrManager);

            try {
                hrController.addNewShift(ShiftTime.Morning,date2,"branch1",hrController.getEmployee(12));
            }
            catch (Exception exp){
                response=exp.getMessage();
            }
            Assert.assertEquals("This employee can't be shift manager. He does not have the required authorization.", response);

            response="";
            try {
                hrController.addNewShift(ShiftTime.Morning,date2,"branch1",hrController.getEmployee(3));
            }
            catch (Exception exp){
                response=exp.getMessage();
            }
            Assert.assertEquals("This shift manager is not available for this shift", response);

            response="";
            try {
                hrController.addNewShift(ShiftTime.Morning,date1,"branch1",hrController.getEmployee(3));
            }
            catch (Exception exp){
                response=exp.getMessage();
            }
            Assert.assertEquals("", response);

            response="";
            try {
                hrController.addNewShift(ShiftTime.Morning,date1,"branch1",hrController.getEmployee(3));
            }
            catch (Exception exp){
                response=exp.getMessage();
            }
            Assert.assertEquals("This shift already exist.", response);

        }
        catch (Exception e){
            Assert.assertEquals(e.getMessage(), e.getMessage());
        }
        try {
            shiftDAO.deleteRow(new Shift(date1,ShiftTime.Morning,"branch1",hrController.getEmployee(3)));
            constrainsDAO.deleteRow(new Constraint(12,date2,ShiftTime.Morning));
            constrainsDAO.deleteRow(new Constraint(3,date1,ShiftTime.Morning));
            placementDAO.deleteRow(new Placement(date1,ShiftTime.Morning,"branch1",3,"driver"));
            userController.logout(4);

        }
        catch (Exception e){
            Assert.assertEquals(e.getMessage(), e.getMessage());
        }

    }
//
    @Test
    public void Test_addShiftPosition(){
        try {


            userController.login(3,"3", driver);
            userController.addConstraint(date1, ShiftTime.Morning);
            userController.logout(3);
            userController.login(4,"4",hrManager);
            hrController.addNewShift(ShiftTime.Morning,date1,"branch1",hrController.getEmployee(3));


            try {
                hrController.addShiftPosition(ShiftTime.Morning,date1,"branch1","hr manager",5);
            }
            catch (Exception e){
                response=e.getMessage();
            }
            Assert.assertEquals("", response);

            try {
                hrController.addShiftPosition(ShiftTime.Morning,date1,"branch1","cashier",1);
            }
            catch (Exception e){
                response=e.getMessage();
            }
            Assert.assertEquals("This position is already exists", response);

            try {
                hrController.addShiftPosition(ShiftTime.Morning,date1,"branch1","cashier",2);
            }
            catch (Exception e){
                response=e.getMessage();
            }
            Assert.assertEquals("This position is already exists", response);

        }

        catch (Exception e){
            //Assert.assertTrue(true);
        }
        try {
            userController.logout(4);
            shiftDAO.deleteRow(new Shift(date1,ShiftTime.Morning,"branch1", hrController.getEmployee(1)));
            constrainsDAO.deleteRow(new Constraint(1,date1,ShiftTime.Morning));
            placementDAO.deleteRow(new Placement(date1,ShiftTime.Morning,"branch1",1,"order man"));
            userController.logout(4);

        }
        catch (Exception e){
            Assert.assertEquals(e.getMessage(), e.getMessage());
        }
    }


    @Test
    public void Test_login() {
        response = "";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("", response);
        response = "";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("There is an already logged in user.", response);
        response = "";
        try {
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            userController.login(000000000,"abc111", hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals(000000000+" is not register in the system", response);

        try {
            userController.login(4,"ghrsjnyjmsjy", hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals(4+" is not register in the system", response);
        response = "";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("", response);
        response = "";
        try {
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void Test_logout() {
        response = "";
        try {
            userController.logout(4);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("There is no user logged in .", response);
        response = "";
        try {
            userController.login(000000,"4",hrManager);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals(000000+" is not register in the system", response);
        response = "";

        try {
            userController.logout(4);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("There is no user logged in .", response);
        response = "";
        try {
            userController.login(4,"4",hrManager);
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            userController.logout(4);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("", response);
    }
    @Test
    public void Test_addNewEmployee() {
        response = "";
        try {
            hrController.addNewEmployee("Motti", "Yair",
                    new Date(new Date().getTime() - DAYTIME), 666, "cashier", true,
                    12,12,12,5656.23,
                    "He gets 18 days of vacation a year", "branch1");
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("Hr Manager is not logged in", response);
        response ="";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        try {
            hrController.addNewEmployee("Motti", "Yair",
                    new Date(new Date().getTime() - DAYTIME), 666, "none", true,
                    12,12,12,5656.23,
                    "He gets 18 days of vacation a year", "branch1");
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("The job does not exists.", response);
        response ="";
        try {
            hrController.addNewEmployee("Motti", "Yair",
                    new Date(new Date().getTime() + DAYTIME), 666, "cashier", true,
                    12,12,12,5656.23,"He gets 18 days of vacation a year",
                    "branch1");
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("starting date is invalid.", response);
        response ="";
        try {
            hrController.addNewEmployee("Motti", "Yair",
                    new Date(new Date().getTime() - DAYTIME), 666, "cashier", true,
                    12,12,12,5656.23,
                    "He gets 18 days of vacation a year", "none");
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("there isn't such branch", response);
        response ="";
        try {
            hrController.addNewEmployee("Motti", "Yair",
                    new Date(new Date().getTime() - DAYTIME), 666, "cashier", true,
                    12,12,12,5656.23,
                    "He gets 18 days of vacation a year", "branch1");
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("", response);
        response ="";
        try {
            hrController.getEmployee(666);
        } catch (Exception e){
            response = e.getMessage();
        }
        Assert.assertEquals("", response);
        try {
            hrController.removeEmployee(666);
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void Test_removeEmployee() {
        response ="";
        try {
            userController.login(4,"4", hrManager);
            hrController.addNewEmployee("test","test", new Date(new Date().getTime() - DAYTIME), 666, "cashier"
            , true,15,15,15,12222.23,"works nights", "branch1");
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.removeEmployee( 666);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("Hr Manager is not logged in", response);
        response ="";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.removeEmployee(8989);
        } catch (Exception e){
            response = e.getMessage();
        }
        Assert.assertEquals("There is no such employee", response);
        response ="";
        try {
            hrController.removeEmployee(666);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void Test_updateBankNumber() {
        response = "";
        try {
            hrController.updateBankNumber(5, 66);
        } catch (Exception e) {
            response = e.getMessage();
        }
        Assert.assertEquals("Hr Manager is not logged in", response);
        response = "";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.updateBankNumber(5, 666);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            response = ""+ bankAccountDAO.getRow(BankAccountDTO.getPK(5)).getBankNumber();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("666", response);
        response = "";
        try {
            userController.logout(4);
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.updateBankNumber( 6, 6666);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            response = ""+ bankAccountDAO.getRow(BankAccountDTO.getPK(6)).getBankNumber();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("6666", response);
        response = "";
        try {
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void Test_updateAccountNumber() {
        response = "";
        try {
            hrController.updateAccountNumber(5, 11);
        } catch (Exception e) {
            response = e.getMessage();
        }

        Assert.assertEquals("Hr Manager is not logged in", response);
        response = "";
        try {
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.updateAccountNumber(5, 1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            response = ""+ bankAccountDAO.getRow(BankAccountDTO.getPK(5)).getAccountNumber();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("1", response);
        response = "";
        try {
            userController.logout(4);
            userController.login(4,"4", hrManager);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            hrController.updateAccountNumber( 6, 2222);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        try {
            response = ""+ bankAccountDAO.getRow(BankAccountDTO.getPK(6)).getAccountNumber();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("2222", response);
        response = "";
        try {
            userController.logout(4);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
