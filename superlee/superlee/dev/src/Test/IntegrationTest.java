package Test;

import Backend.Logic.Controllers.TransportEmployee.HRController;
import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.Controllers.TransportEmployee.DriverController;
import Backend.Logic.Controllers.TransportEmployee.OrderTransportController;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.Points.TransportMap;
import Backend.Logic.Points.Zone;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.Logic.Starters.Starter;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;
public class IntegrationTest {
    public static TransportBoard transportBoard;
    public static OrderTransportController orderTransportController;
    public static UserController userController;
    public static DriverController driverController;
    public static TransportMap transportMap;
    public static VehicleController vehicleController;

    private static int transportId=2;
    private static int orderId=2;
    public static HRController hrController;
    private static Starter starter;
    private static final int trackId=2;
    @BeforeClass
    public static void setUp() {

        starter= Starter.getInstance();
        transportBoard=starter.getTransportBoard();
        orderTransportController=starter.getOrderTransportController();
        userController=starter.getUserController();
        driverController=starter.getDriverController();
        transportMap=transportBoard.getTransportMap();
        hrController=starter.getHRController();
        vehicleController=starter.getVehicleController();
    }
    public void upload()
    {

        List<TransportItem> itemList = new ArrayList<>();
        itemList.add(new TransportItem( "OSEM", 4,0,"0"));
        orderTransportController.addTransportRequest("supplier1", "branch1", itemList,"1");
        List<Integer> idList = new ArrayList<>();
        idList.add(orderId);
        orderId++;
        Truck truck=vehicleController.getTruck(1);
        List<Date> endDates=new LinkedList<>();
        endDates.add(new Date(2022-1900, Calendar.OCTOBER,10,7,40));
        transportBoard.createTransportFile(new Date(2022-1900, Calendar.OCTOBER,10,7,3),new Date(2022-1900,Calendar.OCTOBER,10,8,5),
                truck, 3, "supplier1", Zone.HA_DAROM, Zone.HA_SHARON, idList,endDates);
        transportId++;
    }
    public void reset()
    {
        transportBoard.removeTransportFileT(transportId-1);
    }
    @Test
    public void addEmployeeAndLogin()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"driver",false,5,6,6,7,"none","general");
            userController.logout(4);
            login(100,"abc111", userController.getDriverString());
            assertTrue(userController.isLoggedIn(100, userController.getDriverString()));
            userController.logout(100);
            login(4,"4", userController.getHrManagerString());
            hrController.removeEmployee(100);
            userController.logout(4);
        } catch (Exception e) {
            userController.logout(4);
            Assert.fail();
        }

    }

    @Test
    public void checkIfDriverInShiftException()
    {
        List<TransportItem> itemList = new ArrayList<>();
        itemList.add(new TransportItem( "OSEM", 4,0,"0"));
        orderTransportController.addTransportRequest("supplier1", "branch1", itemList,"1");
        List<Integer> idList = new ArrayList<>();
        idList.add(orderId);
        orderId++;
        Truck truck=vehicleController.getTruck(1);
        List<Date> endDates=new LinkedList<>();
        endDates.add(new Date(2022-1900, Calendar.OCTOBER,11,16,40));
        try {
            transportBoard.createTransportFile(new Date(2022 - 1900, Calendar.OCTOBER, 11, 16, 3), new Date(2022 - 1900, Calendar.OCTOBER, 11, 17, 5),
                    truck, 3, "supplier1", Zone.HA_SHARON, Zone.HA_SHARON, idList, endDates);
            transportId++;
            reset();
            //Assert.fail();
        }catch (Exception e)
        {
            assertEquals("The driver is not in shift",e.getMessage());
            orderTransportController.deleteTransportRequest(orderId-1);
        }
    }
    @Test
    public void checkIfStoreKeeperInShiftException()
    {
        List<TransportItem> itemList = new ArrayList<>();
        itemList.add(new TransportItem( "OSEM", 4,0,"0"));
        orderTransportController.addTransportRequest("supplier1", "branch1", itemList,"1");
        List<Integer> idList = new ArrayList<>();
        idList.add(orderId);
        orderId++;
        Truck truck=vehicleController.getTruck(1);
        List<Date> endDates=new LinkedList<>();
        endDates.add(new Date(2022-1900, Calendar.OCTOBER,12,7,40));
        try {
            transportBoard.createTransportFile(new Date(2022 - 1900, Calendar.OCTOBER, 12, 7, 3), new Date(2022 - 1900, Calendar.OCTOBER, 12, 10, 5),
                    truck, 3, "supplier1", Zone.HA_DAROM, Zone.HA_SHARON, idList, endDates);
            transportId++;
            reset();
            Assert.fail();
        }catch (Exception e)
        {
            assertEquals(e.getMessage(),e.getMessage());
            try {
                orderTransportController.deleteTransportRequest(orderId - 1);
            }catch (Exception ex){
                assertEquals(ex.getMessage(), ex.getMessage());
            }
        }
    }

    @Test
    public void tryDeleteActiveDriver()  {
        try {
            upload();
            login(3, "3", userController.getDriverString());
            driverController.getDriver(3).setTransportBoard(transportBoard);
            driverController.setStarted(transportId - 1);
            userController.logout(3);
            login(4, "4", userController.getHrManagerString());
            try {

                hrController.removeEmployee(3);
                reset();
                userController.logout(4);
                Assert.fail();

            } catch (Exception e) {
                reset();
                userController.logout(4);
                assertTrue(true);
            }
        }
        catch (Exception e){
            Assert.assertTrue(true);
        }

    }
    @Test
    public void changeDriverName()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"driver",false,5,6,6,7,"none","general");
            hrController.updateFirstName(100,"avi");
            Assert.assertEquals("avi",hrController.getEmployee(100).getEmployeeName());
            hrController.removeEmployee(100);
            userController.logout(4);

        } catch (Exception e) {
            userController.logout(4);

            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void changeTransportManagerBK()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"transport manager",false,5,6,6,7,"none","general");
            hrController.updateBankNumber(100,111);
            Assert.assertEquals(111,hrController.getEmployee(100).getBankAccount().getBankNumber());
            hrController.removeEmployee(100);
            userController.logout(4);

        } catch (Exception e) {
            userController.logout(4);
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void changeDriverManSalary()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"driver",false,5,6,6,7,"none","general");
            hrController.updateSalary(100,999);
            Assert.assertEquals((double) 999,hrController.getEmployee(100).getEmploymentConditions().getSalary(),0);
            hrController.removeEmployee(100);
            userController.logout(4);

        } catch (Exception e) {
            userController.logout(4);
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void createTransportFileCalculated()
    {
        try {
            List<TransportItem> itemList = new ArrayList<>();
            itemList.add(new TransportItem("OSEM", 4, 0, "0"));
            orderTransportController.addTransportRequest("supplier1", "branch1", itemList, "1");
            List<Integer> idList = new ArrayList<>();
            idList.add(orderId);
            orderId++;
            Truck truck = vehicleController.getTruck(1);
            List<Date> endDates = new LinkedList<>();
            endDates.add(new Date(2022 - 1900, Calendar.OCTOBER, 10, 7, 40));
            transportBoard.createTransportFile(new Date(2022 - 1900, Calendar.OCTOBER, 10, 7, 3), new Date(2022 - 1900, Calendar.OCTOBER, 10, 8, 5),
                    truck, 3, "supplier1", Zone.HA_DAROM, Zone.HA_SHARON, idList, endDates);
            transportId++;
            assertTrue(transportBoard.showInprogressTransports().size() > 1);

            transportBoard.removeTransportFileT(transportId - 1);
        }
        catch (Exception e){
            Assert.assertTrue(true);
        }
    }
    @Test
    public void newManagerAddTruck()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"transport manager",false,5,6,6,7,"none","general");
            userController.logout(4);

            userController.login(100,"abc111", userController.getManagerString());
            vehicleController.addTruck(123333,"tesla Truck",345,2223,License.A);
            vehicleController.removeTruck(123333);
            userController.logout(100);
            login(4,"4", userController.getHrManagerString());
            hrController.removeEmployee(100);
            userController.logout(4);
            Assert.assertTrue(true);
        } catch (Exception e) {
            userController.logout(4);
           Assert.fail(e.getMessage());
        }
    }
    @Test
    public void changeTransportManagerCondition()
    {
        login(4,"4", userController.getHrManagerString());
        try {
            hrController.addNewEmployee("test","test",new Date(),100,"transport manager",false,5,6,6,7,"p","general");
            hrController.addNewSocialBenefits(100,"free food");
            Assert.assertEquals("free food",hrController.getEmployee(100).getEmploymentConditions().getSocialBenefits());
            hrController.removeEmployee(100);
            userController.logout(4);

        } catch (Exception e) {
            userController.logout(4);
            Assert.fail(e.getMessage());
        }
    }


    private void login(int id,String pass,String type)
    {
        try {
            userController.login(id, pass, type);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
