package Backend.Logic.Controllers.Stock;

import Backend.Logic.LogicObjects.Product.Product;
import Backend.Logic.LogicObjects.Report.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportBuilder {
    private ProductController productController;
    private CategoryController categoryController;
    public ReportBuilder(ProductController productController, CategoryController categoryController){
        this.productController = productController;
        this.categoryController = categoryController;
    }
    public List<ProductInfo> getDamagedProductsInfo() throws Exception {
        List<Product> damaged = productController.getDamagedProducts();
        damaged.forEach(p->{p.setGetTimeIdealSupplier((name, demand) -> 1);p.setOrderByDemand(((address, name, demand) -> {String n = "0";}));p.setNotifyCLI((msg) -> {String ms = msg;});});
        List<ProductInfo> productInfos = new LinkedList<>();
        for (Product product: damaged) {
            productInfos.add(new DamagedProductInfo(product));
        }
        productController.getDamagedProducts();
        return productInfos;
    }
    public List<ProductInfo> getStockProductsInfo(List<List<String>> routes) throws Exception {
        List<Category> categories = new LinkedList<>();
        for(List<String> route:routes){
            categories.add(categoryController.getCategory(route));
        }
        List<Product> products = productController.getProductsByCategories(categories);
        products.forEach(p->{p.setGetTimeIdealSupplier((name, demand) -> 1);p.setOrderByDemand(((address, name, demand) -> {int n = 0;}));p.setNotifyCLI((msg) -> {String ms = msg;});});
        List<ProductInfo> productInfos = new LinkedList<>();
        for (Product product: products){
            productInfos.add(new StockProductInfo(product));
        }
        productController.getProductsByCategories(categories);
        return productInfos;
    }

    public Report buildStockReport(List<List<String>> categories) throws Exception {
        return new Report(new Date(),getStockProductsInfo(categories));
    }

    public Report buildMissingProductsReport(){
        return new Report(new Date(), productController.getMissingProducts().stream().map(product -> {
            try {
                return new StockProductInfo(product);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
    }
    public Report buildDamagedReport() throws Exception {
        return new Report(new Date(),getDamagedProductsInfo());
    }
}
