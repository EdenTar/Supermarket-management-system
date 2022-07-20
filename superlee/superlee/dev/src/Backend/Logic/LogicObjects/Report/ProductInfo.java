package Backend.Logic.LogicObjects.Report;

import Backend.Logic.LogicObjects.Product.Product;

public class ProductInfo {
    private String productNumber;
    private String name;
    private String manufacturer;

    public ProductInfo(Product product) throws Exception {
        if(product == null){
            throw new Exception("product is null ");
        }
        this.name = product.getName();
        this.productNumber = product.getProductNumber();
        this.manufacturer = product.getManufacturer();
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

}
