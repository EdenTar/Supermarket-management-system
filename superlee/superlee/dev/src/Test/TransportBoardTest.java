package Test;

import Backend.Logic.Controllers.TransportEmployee.BasicEmployeeController;
import Backend.Logic.Controllers.TransportEmployee.HRController;
import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.Controllers.TransportEmployee.DriverController;
import Backend.Logic.Controllers.TransportEmployee.OrderTransportController;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Points.TransportMap;
import Backend.Logic.Points.Zone;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.Starters.Starter;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TransportBoardTest {
    public static TransportBoard transportBoard;
    public static OrderTransportController orderTransportController;
    public static UserController userController;
    public static DriverController driverController;
    public static TransportMap transportMap;
    public static HRController hrController;
    public static VehicleController vehicleController;
    private static int transportId=2;
    public static BasicEmployeeController basicEmployeeController;
    @BeforeClass
    public static void setUp() throws Exception {
        Starter starter=Starter.getInstance();
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
        idList.add(transportId);
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
    public void createTransportFile() {
        try {
            boolean t = transportBoard.showInprogressTransports().stream().anyMatch(x -> x.getFileId() == (transportId - 1) && (transportId - 1) != 1);
            assertFalse(t);
            upload();
            assertTrue(transportBoard.showInprogressTransports().stream().anyMatch(x -> x.getFileId() == (transportId - 1)));
            reset();
        }
        catch (Exception e){
            assertEquals(e.getMessage(), e.getMessage());
        }
    }

    @Test
    public void showInprogressTransports() {
        try {
            upload();
            assertArrayEquals(new Integer[]{1, (transportId - 1)}, transportBoard.showInprogressTransports().stream().map(TransportFile::getFileId).toArray());
            reset();
        }catch (Exception e){
            assertEquals(e.getMessage(), e.getMessage());
        }
    }

    @Test
    public void getAvailableDrivers() {
        Integer[] expected = {3};
        Assert.assertArrayEquals(expected, transportBoard.getAvailableDrivers(new Date(2022-1900,9,10,8,3),new Date(2022-1900,9,10,8,30)).stream().map(Driver::getEmployeeId).toArray());
    }

}