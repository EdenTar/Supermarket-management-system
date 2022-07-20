package Backend.Logic.LogicObjects.Supplier;

import java.util.LinkedList;

public class EmptySchedule implements SupplierSchedule{

    //for dal
    private String cn;

    public EmptySchedule(String cn){
        this.cn = cn;
    }

    @Override
    public boolean isScheduleConsistent() {
        return false;
    }

    @Override
    public LinkedList<Integer> getDaysList() {
        throw new IllegalArgumentException("schedule wasnt defined for this supplier!");
    }

    @Override
    public int getDaysTillNextShipment() {
        throw new IllegalArgumentException("cannot get next possible shipment time, it wasnt defined!");
    }

    @Override
    public boolean isShippingInDay(int day) {
        return false;
    }

    @Override
    public int getTimeForTillNextNextShipment() {
        throw new IllegalArgumentException("cannot get next possible shipment time, it wasnt defined!");
    }

    @Override
    public boolean isSupplierWithEmptySchedule() {
        return true;
    }

    public String getCn() {
        return cn;
    }
}
