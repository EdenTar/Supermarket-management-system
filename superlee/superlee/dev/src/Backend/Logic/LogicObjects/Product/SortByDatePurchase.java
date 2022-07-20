package Backend.Logic.LogicObjects.Product;

import Backend.ServiceLayer.ServiceObjects.Product.SPurchase;

import java.util.Comparator;

public class SortByDatePurchase implements Comparator<SPurchase> {
    public int compare(SPurchase x, SPurchase y){
        return x.getPurchaseTime().compareTo(y.getPurchaseTime());
    }
}
