package Backend.ServiceLayer.ServiceObjects.Supplier;

public class ServiceProductOrder {
    private String catalogNum;
    private String name;
    private int quantity;
    private double totalPriceWithoutDiscount;
    private double discount;
    private double finalPrice;

    public ServiceProductOrder(String catalogNum, String name, int quantity,
                        double totalPriceWithoutDiscount, double discount, double finalPrice){
        this.catalogNum = catalogNum;
        this.name = name;
        this.quantity = quantity;
        this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    public String getName() {
        return name;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getTotalDiscount() {
        return discount;
    }

    public double getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "\n{ " +
                "catalogNum = '" + catalogNum + '\'' + "\n" +
                "name = '" + name + '\'' + "\n" +
                "quantity = " + quantity + "\n" +
                "totalPriceWithoutDiscount = " + totalPriceWithoutDiscount + "\n" +
                "discount = " + discount + '%' + "\n" +
                "finalPrice = " + finalPrice +
                " }";
    }
}
