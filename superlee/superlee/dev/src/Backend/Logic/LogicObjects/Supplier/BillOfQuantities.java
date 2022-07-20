package Backend.Logic.LogicObjects.Supplier;

import Obj.Pair;
import java.util.LinkedList;

public class BillOfQuantities {
    private LinkedList<Pair<Integer,Double>> ranges;
    private String cn;
    private String productName;
    public BillOfQuantities(String cn, String productName){
        ranges = new LinkedList<>();
        ranges.add(new Pair<>(1, 0.0));
        this.cn = cn;
        this.productName = productName;
    }

    //is a contructor without the option to buy less then range amount of items - used for dal
    public BillOfQuantities(String cn, String productName, int range, double discount){
        ranges = new LinkedList<>();
        ranges.add(new Pair<Integer, Double>(range,discount));
        this.cn = cn;
        this.productName = productName;

    }

    //throwing exception if not valid btw
    public void addRange(int startOfRange, double discountPercentage){
        //checking for valid input
        if(startOfRange <= 1 || discountPercentage < 0){
            throw new IllegalArgumentException("start of range is negative or less than 1, or that discount precentage is negative");
        }
        Pair<Integer,Double> addRange = new Pair<>(startOfRange,discountPercentage);
            //running over the list

            for(int i = 0; i <= ranges.size(); i++){
                if(i == ranges.size()){
                    ranges.addLast(addRange);
                    return;
                }else{
                    //case there is a discount for same amount of items - current discount will be replaced with new one
                    if(ranges.get(i).getKey() == startOfRange){
                        removeRange(startOfRange);
                        ranges.add(i,addRange);
                        return;
                    }else{
                        if(ranges.get(i).getKey() > startOfRange) {
                            ranges.add(i,addRange);
                            return;
                        }
                    }
                }
            }
        }


    public LinkedList<Pair<Integer, Double>> getRanges() {
        return ranges;
    }

    public void removeRange(int startOfRange){
        if(startOfRange <= 1){
            throw new IllegalArgumentException("invalid range");
        }
        //can be improved using findRange
        if(ranges.size() <= 1){
            throw new IllegalArgumentException("if this will be removed the ranges list will be empty or with negative size! ");
        }
        boolean wasItemRemoved = false;
        for(int i = 0; i < ranges.size(); i++){
            if(ranges.get(i).getKey() == startOfRange){
                ranges.remove(i);
                wasItemRemoved = true;
            }
        }
        //if no item was removed there was no startOfRange with the
        // given value so we should throw an exception - the command wasnt legal
        if(!wasItemRemoved){
            throw new IllegalArgumentException("this range isnt in the list of billOfQuantities");
        }
    }

    public double getDiscountWithGivenRange(int quantity){
        return ranges.get(findRangeIndex(quantity)).getValue();

    }

    private int findRangeIndex(int givenRange){
        if(ranges.isEmpty()){
            throw new IllegalArgumentException("the ranges list is empty");
        }
        if(givenRange <= 0){
            throw new IllegalArgumentException("the ranges cannot 0 or lower");
        }
        for(int i = 0; i < ranges.size(); i++){
            if(givenRange == ranges.get(i).getKey()){
                return i;
            }
            if(givenRange < ranges.get(i).getKey()){
                return i - 1;
            }
        }
        return ranges.size() - 1;
    }

    public void editDiscountForRange(int quantityToChangeDiscountFor,double newDiscount){
        if(!doesRangeExists(quantityToChangeDiscountFor))
            throw new IllegalArgumentException("this range doesnt exist for this product!");
        removeRange(quantityToChangeDiscountFor);
        addRange(quantityToChangeDiscountFor,newDiscount);
        
    }
    
    private boolean doesRangeExists(int quantity){
        boolean wasRangeFound = false;
        for (Pair<Integer,Double> p: ranges) {
            if (p.getKey() == quantity) {
                wasRangeFound = true;
                break;
            }
        }
        return wasRangeFound;
    }

    public String getCn() {
        return cn;
    }

    public String getProductName() {
        return productName;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
