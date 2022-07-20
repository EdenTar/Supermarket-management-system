package Backend.Logic.LogicObjects.Supplier;

import java.util.Date;
import java.util.LinkedList;

public class ConsistentSupplierSchedule implements SupplierSchedule{
    private LinkedList<Integer> daysList;
    private String cn;
    public ConsistentSupplierSchedule(String cn){
        this.cn = cn;
        this.daysList = new LinkedList<>();
    }

    public String getCn(){return cn;}

    public ConsistentSupplierSchedule(LinkedList<Integer> days){
        this.daysList = new LinkedList<>();
        daysList.addAll(days);
    }


    @Override
    public LinkedList<Integer> getDaysList() {
        return daysList;
    }


    @Override
    public boolean isScheduleConsistent(){
        return true;
    }


    public void defineSupplyingDays(LinkedList<Integer> days){
        //we check all days are legal
        for (Integer day: days) {
            if(day <= 0 || day > 7)
                throw new IllegalStateException("invalid day given" + day);
        }
        daysList = days;
    }

    private boolean doesDayExist(int day){
        return daysList.contains(day);
    }

    public void addSupplyingDay(int day){
        if(day <= 0 || day > 7)
            throw new IllegalStateException("invalid day");

        if(doesDayExist(day))
            throw new IllegalStateException("this supplying day was already in the list.");

        this.daysList.add(day);
    }

    public void removeSupplyingDay(int day){
        if(!doesDayExist(day))
            throw new IllegalStateException("this supplying day was not in the list.");
        if(daysList.size() == 1)
            throw new IllegalArgumentException("this is going to turn into an empty scheduled supplier with " +
                    "undefined supplying days or shipmentTime");

        this.daysList.remove(Integer.valueOf(day));
    }

    @Override
    public boolean isShippingInDay(int day){
        Integer i = day;
        return daysList.contains(i);
    }

    @Override
    public int getTimeForTillNextNextShipment() {
        //i chose 15 because it has to be lower than 15 - more than two weeks
        int closestInTwoShipment = 15;
        for (Integer i : daysList) {
            closestInTwoShipment = Math.min(closestInTwoShipment,getTimeDiffBetweenDays(i));
        }
        return closestInTwoShipment;
    }

    @Override
    public boolean isSupplierWithEmptySchedule() {
        return false;
    }

    //can use days till next shipment
    private int getTimeDiffBetweenDays(int day){
        Date dateOfNextShipment = new Date();
        int dayTillNextShipment = getDaysTillNextShipment();
        dateOfNextShipment.setDate(dateOfNextShipment.getDate() + dayTillNextShipment);
        return dayTillNextShipment + getDaysTillNextShipmentFromDate(dateOfNextShipment);
    }




    public int getDaysTillNextShipmentFromDate(Date date){
        if(daysList.isEmpty())
            throw new IllegalArgumentException("cannot get next possible shipment time " +
                    "- consistent supplying days or shipment time isnt defined!");
        int currentDay = date.getDay();
        //default value
        int min = 8;
        for (Integer dayOfTheWeek : daysList) {
            if(dayOfTheWeek > currentDay){
                int diff = dayOfTheWeek - currentDay;
                if(diff < min){
                    min = diff;
                }
            }else if(dayOfTheWeek < currentDay){
                int diff = 7 - (currentDay - dayOfTheWeek);
                if(diff < min){
                    min = diff;
                }
            }else{
                min = 7;
            }
        }

        return min;
    }

    public int getDaysTillNextShipment(){
        if(daysList.isEmpty())
            throw new IllegalArgumentException("cannot get next possible shipment time " +
                    "- consistent supplying days or shipment time isnt defined!");
        Date date = new Date();
        int currentDay = date.getDay();
        //default value
        int min = 8;
        for (Integer dayOfTheWeek : daysList) {
            if(dayOfTheWeek > currentDay){
                int diff = dayOfTheWeek - currentDay;
                if(diff < min){
                    min = diff;
                }
            }else if(dayOfTheWeek < currentDay){
                int diff = 7 - (currentDay - dayOfTheWeek);
                if(diff < min){
                    min = diff;
                }
            }else{
                min = 7;
            }
        }
        return min;
    }





}
