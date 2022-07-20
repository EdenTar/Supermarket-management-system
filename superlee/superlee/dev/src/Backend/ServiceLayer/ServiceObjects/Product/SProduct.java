package Backend.ServiceLayer.ServiceObjects.Product;

import Backend.Logic.LogicObjects.Product.Product;
import Backend.Logic.LogicObjects.Report.Category;

import java.util.List;
import java.util.stream.Collectors;

public class SProduct {


    private String productNumber;
    private String name;
    private String manufacturer;
    private double price;
    private List<String> category;
    private List<SItem> items;
    private List<SPurchase> purchases;
    private List<SDiscount> discounts;

    public SProduct(String productNumber, String name, String manufacturer, double price, List<String> category, List<SItem> items, List<SPurchase> purchases, List<SDiscount> discounts) {
        this.productNumber = productNumber;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category = category;
        this.items = items;
        this.purchases = purchases;
        this.discounts = discounts;
    }

    public SProduct(Product product){
        this(product.getProductNumber(), product.getName(), product.getManufacturer(), product.getPrice(),
                product.getCategory().getCategoryPath().stream().map(Category::getName).collect(Collectors.toList()),
                product.getItems().stream().map(SItem::new).collect(Collectors.toList()),
                product.getPurchases().stream().map(SPurchase::new).collect(Collectors.toList()),
                product.getDiscountHolder().getDiscounts().stream().map(SDiscount::new).collect(Collectors.toList()));
    }

    public List<SPurchase> getPurchases() {
        return purchases;
    }

    public List<SDiscount> getDiscounts() {
        return discounts;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getCategory() {
        return category;
    }

    public List<SItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String res = String.format("[Product number: %s, Name: %s, Manufacturer: %s, Price: %,.2f\n\n", getProductNumber(),
                getName(), getManufacturer(), getPrice());
        res += "Categories: " + printList(getCategory()) + "\n\n";
        res += "Items: " + printList(getItems().stream().map(SItem::getId).collect(Collectors.toList())) + "\n\n";
        res += "Purchases: " + printList(getPurchases()) + "\n\n";
        res += "Discounts: " + printList(getDiscounts());
        return res + "]\n\n";
    }

    private  <T> String printList(List<T> lst){
        StringBuilder res = new StringBuilder("<");
        int i = 0;
        for(T t : lst) {
            res.append(t.toString()).append((i++ == lst.size() - 1) ? "" : "  ,  ");
        }
        return res + ">";
    }
}
