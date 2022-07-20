package Backend.Logic.LogicObjects.Product;

import Backend.DataAccess.DAOs.StockDAOs.*;
import Backend.DataAccess.DTOs.StockDTOS.ItemsDTO;
import Backend.Logic.Controllers.Stock.DiscountHolder;
import Backend.Logic.LogicObjects.Report.Category;


import Backend.ServiceLayer.Facades.Callbacks.CallbackAddOrderByDemand;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetTimeIdealSupplier;
import Backend.ServiceLayer.Facades.Callbacks.CallbackNotifyCLI;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Product {

    private CallbackNotifyCLI notifyCLI;
    private CallbackGetTimeIdealSupplier getTimeIdealSupplier;
    private CallbackAddOrderByDemand orderByDemand;
    private String productNumber;
    private String name;
    private String manufacturer;
    private double price;
    private Category category;
    private int demandPerDay;
    private DiscountHolder discountHolder;
    private int itemID;

    private int discountID;

    private ProductsDAO productsDAO = new ProductsDAO();
    private PurchasesDAO purchasesDAO = new PurchasesDAO();
    private ItemsDAO itemsDAO = new ItemsDAO();
    private DiscountsDAO discountsDAO = new DiscountsDAO();

    public Product(String productNumber, String name, String manufacturer, double price, Category category, int demandPerDay, CallbackGetTimeIdealSupplier getTimeIdealSupplier, CallbackAddOrderByDemand addOrderByDemand, CallbackNotifyCLI notify) throws Exception {
        this.notifyCLI = notify;
        this.getTimeIdealSupplier = getTimeIdealSupplier;
        this.productNumber = productNumber;
        this.name = name;
        this.manufacturer = manufacturer;
        checkValidity(price <= 0, "price can't be negative");
        this.price = price;
        this.category = category;
        this.demandPerDay = demandPerDay;
        this.discountHolder = new DiscountHolder();
        this.orderByDemand = addOrderByDemand;
        this.itemID = 0;
        this.discountID = 0;
    }
    public Product(String productNumber, String name, String manufacturer, double price, Category category, int demandPerDay, int itemID) throws Exception {
        this.productNumber = productNumber;
        this.name = name;
        this.manufacturer = manufacturer;
        checkValidity(price <= 0, "price can't be negative");
        this.price = price;
        this.category = category;
        this.demandPerDay = demandPerDay;
        this.discountHolder = new DiscountHolder();
        this.itemID = itemID;
        discountsDAO.getRowsFromDB("product_ID = " + Long.parseLong(productNumber)).forEach(d -> discountID = Math.max(Integer.parseInt(("" +d.getId()).split("p")[0]) + 1,discountID));
    }

    public CallbackGetTimeIdealSupplier getGetTimeIdealSupplier() {
        return getTimeIdealSupplier;
    }

    public void setGetTimeIdealSupplier(CallbackGetTimeIdealSupplier getTimeIdealSupplier) {
        this.getTimeIdealSupplier = getTimeIdealSupplier;
    }

    public CallbackAddOrderByDemand getOrderByDemand() {
        return orderByDemand;
    }

    public void setOrderByDemand(CallbackAddOrderByDemand orderByDemand) {
        this.orderByDemand = orderByDemand;
    }

    public CallbackNotifyCLI getNotifyCLI() {
        return notifyCLI;
    }

    public void setNotifyCLI(CallbackNotifyCLI notifyCLI) {
        this.notifyCLI = notifyCLI;
    }

    public int getItemID() {
        return itemID;
    }

    public DiscountHolder getDiscountHolder() {
        return discountHolder;
    }

    public List<Purchase> getPurchases() {
        return purchasesDAO.getRowsFromDB("product_ID = " + Long.parseLong(productNumber));
    }


    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
        productsDAO.update(this);
    }

    public String getManufacturer() {return manufacturer;}

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        productsDAO.update(this);
    }

    public double getPrice() {
        return price * discountHolder.getMaxActiveDiscount().getDiscount()/100;
    }

    public void setPrice(double price) throws Exception {
        checkValidity(price <= 0, "price can't be negative");
        this.price = price;
        productsDAO.update(this);
    }

    public Category getCategory() {return category;}

    public List<Item> getItems(){return itemsDAO.getRowsFromDB("product_ID = " + Long.parseLong(productNumber));}

    public void setCategory(Category category) {
        this.category = category;
        productsDAO.update(this);
    }

    public void addItems(String branch, Location.StoreOrStorage place, int shelf, Date expired, int amount) {
        for (int i = 0; i < amount; i++) {
            try {
                Location location = new Location(productNumber, itemID, branch, place, shelf);
                Item toAdd = new Item(productNumber, itemID, location, expired);
                itemID++;
                productsDAO.update(this);
                itemsDAO.insert(toAdd);
                new LocationsDAO().insert(location);
            } catch (Exception e){
                if(i>0){
                    throw new RuntimeException(String.format("%d items had been added successfully, ",i)+e.getMessage());
                }
                else{
                    throw e;
                }
            }
        }
    }

    public Item removeItem(String branch, Location.StoreOrStorage place, int shelf) throws Exception {
        Item res = getItemByPredicate((item -> new Location("", 0, branch, place, shelf).equals(item.getLocation())));
        itemsDAO.deleteRow(res);
        checkMissing();
        return res;
    }

    public Purchase addPurchase(double costPrice, double salePrice, double discount, int quantity, String supplier, Date purchaseTime) throws Exception {
        Purchase toAdd = new Purchase(productNumber, costPrice, salePrice, discount, quantity, supplier, purchaseTime);
        purchasesDAO.insert(toAdd);
        return toAdd;
    }

    private Item getItemByPredicate(Predicate<Item> predicate) throws Exception {
        Item res = getItems().stream().filter(predicate).findFirst().orElse(null);
        checkValidity(res == null, "such item doesn't exist");
        return res;
    }

    public Item getItem(String branch, Location.StoreOrStorage place, int shelf) throws Exception {
        return getItemByPredicate((item) -> (new Location("", 0, branch, place, shelf).equals(item.getLocation())));
    }

    public Item getNotFlawedItem(String branch, Location.StoreOrStorage place, int shelf) throws Exception {
        return getItemByPredicate((item) -> (!item.isFlaw() && new Location("", 0, branch, place, shelf).equals(item.getLocation())));
    }

    public List<Item> getFlawedItems() {
        return getItems().stream().filter((item) -> item.isFlaw()).collect(Collectors.toList());
    }

    public String getProductNumber() {return productNumber;}

    public int getDemandPerDay() { return demandPerDay; }

    public void setDemand(int demandPerDay){
        this.demandPerDay = demandPerDay;
        productsDAO.update(this);
        checkMissing();
    }

    public void checkMissing(){
        if(isMissing()){
            this.notifyCLI.notifyCLI("["+this.getName() + " is under the minimal quantity]");
            this.orderByDemand.order("superlee", this.name, this.demandPerDay);
        }
    }

    public boolean isMissing(){
        int time =this.getTimeIdealSupplier.getTime(this.name, this.demandPerDay);
        return time != -1 && time*this.demandPerDay>= (this.getItems().size()-this.getFlawedQuantity());
    }



    private List<Location> getLocationByPredicate(Predicate<Item> predicate) {
        return getItems().stream().filter(predicate).map(Item::getLocation).distinct().collect(Collectors.toList());
    }

    public List<Location> getLocations() {
        return getLocationByPredicate(item -> true);
    }

    public List<Location> getLocationsOfFlawedItems() {
        return getLocationByPredicate(Item::isFlaw);
    }

    public List<Location> getLocationOfExpiredItems() {
        return getLocationByPredicate(Item::isExpired);
    }

    public List<Location> getLocationOfDamagedItems() {
        return getLocationByPredicate(item -> item.isExpired() || item.isFlaw());
    }

    private int getQuantityByPredicate(Predicate<Item> predicate) {
        return getItems().stream().filter(predicate).collect(Collectors.toList()).size();
    }
    public int getQuantity() {
        return getQuantityByPredicate(item -> true);
    }

    public int getWarehouseQuantity() {
        return getQuantityByPredicate(item -> item.getLocation().getPlace() == Location.StoreOrStorage.STORAGE);
    }

    public int getStoreQuantity() {
        return getQuantityByPredicate(item -> item.getLocation().getPlace() == Location.StoreOrStorage.STORE);
    }

    public int getExpiredQuantity() {
        return getQuantityByPredicate(item -> item.isExpired());
    }

    public int getFlawedQuantity() {
        return getQuantityByPredicate(item -> item.isFlaw());
    }

    public int getDamagedQuantity() {
        return getQuantityByPredicate(item -> item.isFlaw() || item.isExpired());
    }

    public Discount addDiscount(double discount, Date dateFrom, Date dateTo) throws Exception {
        checkValidity(discount < 0 || discount > 100 , "discount can't be negative or over 100%");
        Discount toAdd = new Discount("" + (discountID++) + "p" + Long.parseLong(productNumber), null, productNumber,discount, dateFrom, dateTo);
        discountHolder.add(toAdd);
        return toAdd;
    }



    private void checkValidity(boolean exp, String errorMessage) throws Exception {
        if(exp){
            throw new Exception(errorMessage);
        }
    }

}
