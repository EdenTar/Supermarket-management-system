package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.Vehicles.Truck;

public class ServiceTruck {
    private final int truckId;
    private final String model;
    private final int currentWeight;
    private final int maxWeight;
    private final LicenseService license;

    @Override
    public String toString() {
        return
                "truckId:\n" + truckId +
                        ", model='" + model + '\'' +
                        ", currentWeight=" + currentWeight +
                        ", maxWeight=" + maxWeight +
                        ", license=" + license +
                        '\n';
    }


    public ServiceTruck(Truck truck) {
        this.truckId = truck.getId();
        this.model = truck.getModel();
        this.currentWeight = truck.getBasicWeight();
        this.maxWeight = truck.getMaxWeight();
        this.license = LicenseService.valueOf(truck.getLicense().name());
    }

    public int getTruckId() {
        return truckId;
    }

    public String getModel() {
        return model;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }


    public LicenseService getLicense() {
        return license;
    }


}
