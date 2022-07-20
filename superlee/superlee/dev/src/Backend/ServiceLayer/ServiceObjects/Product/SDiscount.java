package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Discount;

import java.util.Date;

public class SDiscount {

    private double discount;
    private Date dateFrom;
    private Date dateTo;

    public SDiscount(double discount, Date dateFrom, Date dateTo) {
        this.discount = discount;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public SDiscount(Discount discount){
        this(discount.getDiscount(), discount.getDateFrom(), discount.getDateTo());
    }

    public double getDiscount() {
        return discount;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    @Override
    public String toString(){
        return String.format("[Discount: %,.2f, From: %s, To: %s]",getDiscount(),getDateFrom().toString(),getDateTo().toString());
    }
}
