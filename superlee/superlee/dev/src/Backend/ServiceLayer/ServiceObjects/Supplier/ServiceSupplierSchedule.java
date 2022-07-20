package Backend.ServiceLayer.ServiceObjects.Supplier;

import java.util.LinkedList;

public class ServiceSupplierSchedule {

    private LinkedList<Integer> daysList;

    public ServiceSupplierSchedule(LinkedList<Integer> daysList){
        this.daysList = daysList;
    }

    @Override
    public String toString() {
        if(daysList != null && daysList.isEmpty() == false)
            return "the supplier is supplying consistently on the following days : (presenting their indexes) " + daysList;
        return "the supplier doesnt supply on a weekly basis";

    }
}
