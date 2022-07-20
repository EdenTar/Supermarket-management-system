package Backend.Logic.Controllers.Stock;

import Backend.Logic.LogicObjects.Product.Discount;
import Backend.DataAccess.DAOs.StockDAOs.DiscountsDAO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DiscountHolder {
    private DiscountsDAO discountsDAO = new DiscountsDAO();
    public DiscountHolder(){

    }
    public void add(Discount discount){
        discountsDAO.insert(discount);
    }
    private List<Discount> activeDiscounts(){
        List<Discount> active_discounts = new LinkedList<>();
        for (Discount discount: getDiscounts()) {
            if(discount.isActive()){
                active_discounts.add(discount);
            }
        }
        return active_discounts;
    }

    public List<Discount> getDiscounts() {
        return discountsDAO.selectAllRowsToBusiness();
    }

    public Discount getMaxActiveDiscount(){
        Discount maxDiscount = new Discount("",0,"",100,new Date(), new Date());
        for(Discount discount: activeDiscounts()){
            if(discount.getDiscount()<=maxDiscount.getDiscount()){
                maxDiscount = discount;
            }
        }
        return maxDiscount;
    }

}
