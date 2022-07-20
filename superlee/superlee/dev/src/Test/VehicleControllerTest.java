package Test;

import Backend.Logic.Starters.Starter;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;
import Backend.Logic.Vehicles.VehicleController;
import Backend.ServiceLayer.ServiceObjects.Transport.LicenseService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class VehicleControllerTest {
    public static VehicleController vehicleController;
    @BeforeClass
    public static void setUp() throws Exception {
        Starter starter=Starter.getInstance();
        vehicleController =starter.getVehicleController();
    }

    @Test
    public void addTruck() {
        int truckId = 3;
        String model = "Mercedes";
        int currentWeight = 3000;
        int maxWeight = 7000;
        LicenseService licenseService = LicenseService.A;
        Date from=new Date(2022-1900,10,11,7,3);
        Date to =new Date(2022-1900,10,11,8,5);
        List<Truck> trucks = vehicleController.availableTruck(from,to);
        Assert.assertTrue(trucks.stream().noneMatch(truck -> truck.getId() == truckId));
        vehicleController.addTruck(truckId, model, currentWeight, maxWeight, License.A);
        Assert.assertTrue(vehicleController.availableTruck(from,to).
                stream().anyMatch(serviceTruck -> serviceTruck.getId() == truckId));
        vehicleController.removeTruck(3);

    }

    @Test
    public void removeTruck() {
        int truckId = 3;
        String model = "Mercedes";
        int currentWeight = 3000;
        int maxWeight = 7000;
        LicenseService licenseService = LicenseService.A;
        Date from=new Date(2022-1900,10,11,7,3);
        Date to =new Date(2022-1900,10,11,8,5);
        vehicleController.addTruck(truckId, model, currentWeight, maxWeight, License.A);
        List<Truck> trucks = vehicleController.availableTruck(from,to);
        Assert.assertTrue(trucks.stream().anyMatch(truck -> truck.getId() == truckId));
        vehicleController.removeTruck(truckId);
        Assert.assertTrue(vehicleController.availableTruck(from,to).
                stream().noneMatch(truck -> truck.getId() == truckId));

    }
}