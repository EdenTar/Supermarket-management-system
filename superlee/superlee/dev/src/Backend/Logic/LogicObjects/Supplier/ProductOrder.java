package Backend.Logic.LogicObjects.Supplier;

public class ProductOrder {

    private int id;
    private String catalogNum;
    private String name;
    private int quantity;
    private double totalPriceWithoutDiscount;
    private double discount;
    private double finalPrice;

    public ProductOrder(int id, String catalogNum, String name, int quantity,
                        double totalPriceWithoutDiscount, double discount, double finalPrice){
        this.id = id;
        this.catalogNum = catalogNum;
        this.name = name;
        this.quantity = quantity;
        this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getDiscount() {
        return discount;
    }

    public double getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public int getQuantity() {
        return quantity;
    }
}
