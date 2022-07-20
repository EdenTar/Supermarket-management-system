package Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities;

import Backend.Logic.Controllers.Stock.CategoryController;
import Backend.Logic.Controllers.Stock.ProductController;
import Backend.Logic.LogicObjects.Product.Location;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.ServiceObjects.Product.*;
import Backend.ServiceLayer.ServiceObjects.Report.SCategory;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Result.ValueResult;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public interface StockFunctionality {
    ProductController productController = Starter.getInstance().getProductController();
    CategoryController categoryController = Starter.getInstance().getCategoryController();

    default Result<SDelivery> getDelivery(){
        try{
        return new ValueResult<>(new SDelivery(productController.getDelivery()));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }
    default Result receiveDelivery(List<Integer> quantities, List<Date> dates, List<String> branches, List<String> places, List<Integer> shelfs){
        try{
            productController.receiveDelivery(quantities,dates,branches,places,shelfs);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<List<SProduct>> getAllProducts() {
        try{
            return new ValueResult<>(productController.getProducts().stream().map(SProduct::new).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<List<SProduct>> getAllMissingProducts() {
        try{
            return new ValueResult<>(productController.getMissingProducts().stream().map(SProduct::new).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> getProduct(String productNumber) {
        try{
            return new ValueResult<>(new SProduct(productController.getProduct(productNumber)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> addProduct(String productNumber, String name, String manufacturer, double price, List<String> category, int demand) {
        try{
            return new ValueResult<>(new SProduct(productController.addProduct(productNumber, name, manufacturer, price, categoryController.getCategory(category),demand)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> checkForShortage(String productName){
        try{
            productController.checkForShortage(productName);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> removeProduct(String productNumber) {
        try{
            return new ValueResult<>(new SProduct(productController.removeProduct(productNumber)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> updateProductPrice(String productNumber, double price) {
        try{
            return new ValueResult<>(new SProduct(productController.updateProductPrice(productNumber, price)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> updateProductDemand(String productNumber, int demand) {
        try{
            return new ValueResult<>(new SProduct(productController.updateProductDemand(productNumber, demand)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SProduct> updateProductCategory(String productNumber, List<String> category) {
        try {
            return new ValueResult<>(new SProduct(productController.updateProductCategory(productNumber, categoryController.getCategory(category))));
        } catch (Exception e) {
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SDiscount> addProductDiscount(String productNumber, double discount, Date dateFrom, Date dateTo) {
        try{
            return new ValueResult<>(new SDiscount(productController.getProduct(productNumber).addDiscount(discount, dateFrom, dateTo)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SPurchase> addProductPurchase(String productNumber, double costPrice, double salePrice, double discount, int quantity, String supplier, Date purchaseTime) {
        try{
            return new ValueResult<>(new SPurchase(productController.addPurchase(productNumber, costPrice, salePrice, discount, quantity, supplier, purchaseTime)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SItem> reportItemAsFlawed(String productNumber, String branch, String place, int shelf) {
        try{
            return new ValueResult<>(new SItem(productController.reportItemAsFlawed(productNumber, branch, Location.StoreOrStorage.valueOf(place), shelf)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> addItems(String productNumber, String branch, String place, int shelf, Date expired, int amount) {
        try{
            productController.getProduct(productNumber).addItems(branch, Location.StoreOrStorage.valueOf(place), shelf, expired,amount);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SItem> removeItem(String productNumber, String branch, String place, int shelf) {
        try{
            return new ValueResult<>(new SItem(productController.getProduct(productNumber).removeItem(branch, Location.StoreOrStorage.valueOf(place), shelf)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Integer> getProductDemand(String productName){
        try{
            return new ValueResult<>(productController.getProductDemand(productName));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Integer> getProductQuantity(String productName){
        try{
            return new ValueResult<>(productController.getProductQuantity(productName));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<List<SCategory>> getMainCategories() {
        try{
            return new ValueResult<>(categoryController.getMainCategories().stream().map(SCategory::new).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<List<SCategory>> getSubCategories(List<String> route) {
        try{
            return new ValueResult<>(categoryController.getSubCategories(route).stream().map(SCategory::new).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SCategory> getCategory(List<String> route) {
        try {
            return new ValueResult<>(new SCategory(categoryController.getCategory(route)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SCategory> addCategory(String categoryName, List<String> route) {
        try {
            return new ValueResult<>(new SCategory(categoryController.addCategory(categoryName, route)));
        } catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SDiscount> addCategoryDiscount(List<String> route, double discount, Date from, Date to) {
        try{
            return new ValueResult<>(new SDiscount(categoryController.addDiscount(route,discount, from, to)));
        } catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

}
