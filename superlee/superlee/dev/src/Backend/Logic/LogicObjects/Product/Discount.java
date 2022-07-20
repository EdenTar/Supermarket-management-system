package Backend.Logic.LogicObjects.Product;


import java.util.Date;

public class Discount {

    private String id;
    private Integer category_id;
    private String product_id;
    private double discount;
    private Date dateFrom;
    private Date dateTo;

    public Discount(String id, Integer category_id, String product_id,double discount, Date dateFrom, Date dateTo){
        this.id = id;
        this.category_id = category_id;
        this.product_id = product_id;
        this.discount = discount;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
    public String getId() {
        return id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public boolean isActive(){
        Date current = new Date();
        return !current.after(dateTo) && !current.before(dateFrom);
    }
    public double getDiscount(){
        return discount;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }
}
