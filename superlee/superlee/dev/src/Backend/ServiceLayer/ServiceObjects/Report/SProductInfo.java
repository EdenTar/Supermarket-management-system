package Backend.ServiceLayer.ServiceObjects.Report;

import Backend.Logic.LogicObjects.Report.ProductInfo;

public class SProductInfo {

    private String productNumber;
    private String name;
    private String manufacturer;

    public SProductInfo(String productNumber, String name, String manufacturer) {
        this.productNumber = productNumber;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public SProductInfo(ProductInfo productInfo){
        this(productInfo.getProductNumber(), productInfo.getName(), productInfo.getManufacturer());
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

    @Override
    public String toString() {
        return "SProductInfo{" +
                "productNumber='" + productNumber + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
