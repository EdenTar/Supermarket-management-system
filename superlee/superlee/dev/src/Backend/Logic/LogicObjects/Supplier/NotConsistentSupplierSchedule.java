package Backend.Logic.LogicObjects.Supplier;

import java.util.Date;
import java.util.LinkedList;

public class NotConsistentSupplierSchedule implements SupplierSchedule{
    private int shipmentTime;
    private String cn;

    public NotConsistentSupplierSchedule(String cn, int shipmentTime){
        this.cn = cn;
        setShipmentTime(shipmentTime);
    }

    public String getCn() {
        return cn;
    }

    @Override
    public boolean isScheduleConsistent() {
        return false;
    }

    @Override
    public LinkedList<Integer> getDaysList() {
        return new LinkedList<>();
    }

    @Override
    public int getDaysTillNextShipment() {

        return shipmentTime;
    }

    public void setShipmentTime(int shipmentTime) {
        this.shipmentTime = shipmentTime;
    }




    @Override
    public boolean isShippingInDay(int day){
        Date date = new Date();
        return ((getDaysTillNextShipment() + date.getDay())%7 == day%7 &&
                getDaysTillNextShipment() <= 7);
    }


    @Deprecated
    @Override
    public int getTimeForTillNextNextShipment() {
        return getDaysTillNextShipment();
    }

    @Override
    public boolean isSupplierWithEmptySchedule() {
        return false;
    }

    public int getShipmentTime() {
        return shipmentTime;
    }
}
