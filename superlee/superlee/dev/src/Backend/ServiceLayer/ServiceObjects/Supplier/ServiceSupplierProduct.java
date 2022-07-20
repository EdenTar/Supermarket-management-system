package Backend.ServiceLayer.ServiceObjects.Supplier;

import Backend.Logic.LogicObjects.Supplier.SupplierProduct;

public class ServiceSupplierProduct {
    private String name;
    private String catalogNum;
    private double priceFor1Unit;

    public ServiceSupplierProduct(String name, String catalogNum, double priceFor1Unit){
        this.name = name;
        this.catalogNum = catalogNum;
        this.priceFor1Unit = priceFor1Unit;
    }
    
    public ServiceSupplierProduct(SupplierProduct product){
        this.name = product.getName();
        this.catalogNum = product.getCatalogNum();
        this.priceFor1Unit = product.getPrice();
    }

    public String getName() {
        return name;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    public double getPriceFor1Unit() {
        return priceFor1Unit;
    }

    @Override
    public String toString() {
        return "{ " +
                "name='" + name + '\'' +
                ", catalogNum='" + catalogNum + '\'' +
                ", priceFor1Unit=" + priceFor1Unit +
                " }";
    }
}
