package Backend.Logic.LogicLambdas;

import Backend.Logic.Vehicles.Truck;

import java.util.Date;

public interface FreeTruck {
    public boolean freeTruck(Truck truck, Date start,Date end);
}
