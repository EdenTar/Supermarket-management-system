package Backend.ServiceLayer.ServiceObjects.Supplier;

import Backend.Logic.LogicObjects.Supplier.BillOfQuantities;
import Obj.Pair;

import java.util.LinkedList;

public class ServiceBillOfQuantities
{
    private LinkedList<Pair<Integer,Double>> ranges;

    public ServiceBillOfQuantities(BillOfQuantities billOfQuantities){
        ranges = new LinkedList<>(billOfQuantities.getRanges());
    }

    public LinkedList<Pair<Integer, Double>> getRanges() {
        return ranges;
    }

    @Override
    public String toString() {
        //check if the linked list is sorted...
        LinkedList<String> rangesString = new LinkedList<>();
        for(int i = 0; i<ranges.size()-1; i++){
            rangesString.add("{ [" + ranges.get(i).getKey() + " -> " + ranges.get(i+1).getKey()+ ") = " + ranges.get(i).getValue() + "% }");
        }
        rangesString.add("{ [" + ranges.getLast().getKey() + " -> inf) = " + ranges.getLast().getValue() + "% }");

        return rangesString.toString();

    }
}
