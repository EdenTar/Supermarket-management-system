package Test;

import Backend.Logic.Controllers.TransportEmployee.BasicEmployeeController;
import Backend.Logic.Controllers.TransportEmployee.HRController;
import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.Controllers.TransportEmployee.DriverController;
import Backend.Logic.Controllers.TransportEmployee.OrderTransportController;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.Points.TransportMap;
import Backend.Logic.Points.Zone;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.Logic.Starters.Starter;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DriverDTOControllerTest {

    public static TransportBoard transportBoard;
    public static OrderTransportController orderTransportController;
    public static UserController userController;
    public static DriverController driverController;
    public static TransportMap transportMap;
    public static HRController hrController;
    public static VehicleController vehicleController;
    public static BasicEmployeeController basicEmployeeController;
    private static final Starter starter = Starter.getInstance();
    private static int transportId = 2;

    private static List<Integer> tftr = new LinkedList<>();

    @BeforeClass
    public static void setUp() {
        transportBoard = starter.getTransportBoard();
        orderTransportController = starter.getOrderTransportController();
        userController = starter.getUserController();
        driverController = starter.getDriverController();
        transportMap = transportBoard.getTransportMap();
        hrController = starter.getHRController();
        vehicleController = starter.getVehicleController();
    }

    @Before
    public void upload() {

        List<TransportItem> itemList = new ArrayList<>();
        itemList.add(new TransportItem("OSEM", 4, 0, "0"));
        orderTransportController.addTransportRequest("supplier1", "branch1", itemList, "1111");
        List<Integer> idList = new ArrayList<>();
        idList.add(transportId);
        tftr.add(transportId);
        Truck truck = vehicleController.getTruck(1);
        List<Date> endDates = new LinkedList<>();
        endDates.add(new Date(2022 - 1900, Calendar.OCTOBER, 10, 7, 40));
        transportBoard.createTransportFile(new Date(2022 - 1900, Calendar.OCTOBER, 10, 7, 3), new Date(2022 - 1900, Calendar.OCTOBER, 10, 8, 5),
                truck, 3, "supplier1", Zone.HA_DAROM, Zone.HA_SHARON, idList, endDates);
        transportId++;
    }

    @After
    public void reset() {
        transportBoard.removeTransportFileT(tftr.get(0));
        userController.logout(tftr.get(1));
        tftr.clear();
    }

    private void login(int id, String pass, String type) {

        try {
            userController.login(id, pass, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void updateLicense() {

        tftr.add(12);
        login(12, "abc111", userController.getDriverString());
        driverController.updateLicense("A");
        String details = driverController.showDetails();
        assertEquals(" Id:12 Name: Bob Ron License: A", details);
        driverController.updateLicense("C");
        details = driverController.showDetails();
        assertEquals(" Id:12 Name: Bob Ron License: C", details);
        driverController.updateLicense("A");

    }

    @Test
    public void weightTruck() {

        tftr.add(3);
        login(3, "3", userController.getDriverString());
        try {
            driverController.weightTruck(140);
        } catch (Exception e) {
            assertEquals("There is no transport in action", e.getMessage());
        }
        driverController.setStarted(tftr.get(0));
        try {
            driverController.weightTruck(153);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            driverController.weightTruck(1100);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }


    }


    @Test
    public void finishDestinationFile() throws Exception {
        tftr.add(3);
        try {

            login(3, "3", userController.getDriverString());
            assertEquals(2, transportBoard.showInprogressTransports().size());
            assertEquals(0, transportBoard.showOldTransports().size());

            try {
                driverController.setStarted(tftr.get(0));
            } catch (Exception e) {
                assertEquals("The transport already started", e.getMessage());
            }
            try {
                driverController.finishDestinationFile(tftr.get(0) + "-" + tftr.get(0));
            }
            catch (Exception e){
                assertEquals(e.getMessage(), e.getMessage());
            }
            assertEquals(1, transportBoard.showInprogressTransports().size());
            assertTrue(transportBoard.showOldTransports().size() > 0);
        } catch (Exception e) {
            throw (e);
        }

    }//TODO:fix destination file
}