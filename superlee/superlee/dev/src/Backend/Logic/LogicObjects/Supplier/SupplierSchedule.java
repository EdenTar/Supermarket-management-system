package Backend.Logic.LogicObjects.Supplier;

import java.util.LinkedList;

public interface SupplierSchedule {

    boolean isScheduleConsistent();
    LinkedList<Integer> getDaysList();
    int getDaysTillNextShipment();
    boolean isShippingInDay(int day);
    int getTimeForTillNextNextShipment();
    boolean isSupplierWithEmptySchedule();
}
