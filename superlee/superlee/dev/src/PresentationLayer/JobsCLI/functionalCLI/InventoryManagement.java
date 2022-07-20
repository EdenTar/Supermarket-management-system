package PresentationLayer.JobsCLI.functionalCLI;


import Backend.Logic.LogicObjects.Product.SortByDatePurchase;

import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.StockFunctionality;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Product.*;
import Backend.ServiceLayer.ServiceObjects.Report.SCategory;
import Backend.ServiceLayer.ServiceObjects.Transport.TransportItemService;
import Obj.Action;
import Obj.Parser;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


public class InventoryManagement {
    private final StockFunctionality service;
    private final Scanner scanner;

    public InventoryManagement(StockFunctionality service, Scanner scanner){
        this.service = service;
        this.scanner = scanner;
    }
    private void getProductInfo(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        SProduct product = service.getProduct(productNumber).getValue();
        System.out.printf("id: %s" +
                        "\nname: %s" +
                        "\nprice: %f" +
                        "\ncategory: %s" +
                        "\nmanufacturer: %s" +
                        "\nin order to continue press 'c'" +
                        "\nfor more info about the product's items, discounts and purchases press 'i'%n"
                ,productNumber
                ,product.getName()
                ,product.getPrice()
                ,product.getCategory().toString().substring(1, 3 * product.getCategory().size() - 1).replaceAll(", ", "")
                ,product.getManufacturer());

        String comm = Parser.getStrInput();
        while(!(comm.equals("c") || comm.equals("i"))){
            comm = Parser.getStrInput();
        }
        if(comm.equals("i")){
            additionalInfoProduct(product,comm);
        }

    }
    private void additionalInfoProduct(SProduct product,String comm){
        System.out.println("1 - product's items");
        System.out.println("2 - product's discounts");
        System.out.println("3 - product's purchases");
        System.out.println("else - cancel");
        comm = Parser.getStrInput();
        switch (comm) {
            case "1":
                // print product items
                for (SItem item : product.getItems()) {
                    System.out.printf("" +
                                    "%d) branch: %s, loc: %s, shelf: %d" +
                                    "\n   expired at: %s" +
                                    "\n   flawed: %b%n",
                            item.getId(),
                            item.getLocation().getBranch(),
                            item.getLocation().getPlace(),
                            item.getLocation().getShelf(),
                            Parser.getStrDate(item.getExpired())
                            , item.isFlaw());
                }
                break;
            case "2":
                // print products discount
                System.out.println("-----discounts-----");
                for (SDiscount discount : product.getDiscounts()) {
                    System.out.printf("discount: %f%%, from: %s, to: %s%n",
                            discount.getDiscount(),
                            Parser.getStrDate(discount.getDateFrom()),
                            Parser.getStrDate(discount.getDateTo()));
                }
                System.out.println("-----discounts-----");
                break;
            case "3":
                // print products purchases
                System.out.println("-----purchases-----");
                List<SPurchase> purchases = product.getPurchases();
                purchases.sort(new SortByDatePurchase());
                for (SPurchase purchase : purchases) {//add comparator by last date.
                    System.out.printf(
                            "purchase time %s\n" +
                                    "quantity: %d\n" +
                                    "supplier: %s\n" +
                                    "cost price: %f\n" +
                                    "sale price: %f\n" +
                                    "discount: %f%n",
                            Parser.getStrDate(purchase.getPurchaseTime()),
                            purchase.getQuantity(),
                            purchase.getSupplier(),
                            purchase.getCostPrice(),
                            purchase.getSalePrice(),
                            purchase.getDiscount()
                    );
                }
                System.out.println("-----purchases-----");
                break;
        }
        System.out.println("in order to search other type of additional info press 'i'");
        System.out.println("in order to continue press 'c'");
        comm = Parser.getStrInput();
        while(!(comm.equals("c") || comm.equals("i"))){
            comm = Parser.getStrInput();
        }
        if(comm.equals("i")){
            additionalInfoProduct(product,comm);
        }
    }
    public void addProduct(){
        System.out.println("in order to add a product we need to get the new products info:");
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        String name = Parser.getStrInput("enter name:");
        String manufacturer = Parser.getStrInput("enter manufacturer");
        double price = Parser.getDoubleInput("enter price:");
        List<String> category = getCategoryInput();
        int demand = Parser.getIntInput("enter demand:");
        Result<SProduct> res = service.addProduct(productNumber,name,manufacturer,price,category,demand);
        if(res.errorOccurred()){
            System.out.println(res.getError());
        }
        else{
            System.out.printf("added %s successfully %n",name);
        }
    }

    private int getIntCLI(){
        while(!scanner.hasNextInt()){
            Parser.getStrInput();
            System.out.println("please enter a number!");
        }
        return scanner.nextInt();
    }

    private List<String> getCategoryInput(){
        try {
            List<SCategory> res = service.getMainCategories().getValue();
            List<String> route = new LinkedList<>();
            System.out.println("enter category based on number");
            AtomicInteger i = new AtomicInteger(0);
            res.forEach(r -> System.out.println("" + i.getAndIncrement() + ": " + r.getName()));
            int input = Parser.getIntInput("enter number, to stop with category path, enter -1");
            List<String> result = new LinkedList<>();
            SCategory choose = null;
            if(input != -1) {
                choose = res.get(input);
                route.add(choose.getName());
                result = choose.getSubCategories();
            }
            while (!result.isEmpty() && input != -1) {
                i.set(0);
                System.out.println("enter category based on number");
                result.forEach(r -> System.out.println("" + i.getAndIncrement() + ": " + r));
                input = Parser.getIntInput("enter number, to stop with category path, enter -1");
                if(input != -1) {
                    String c = result.get(input);
                    route.add(c);
                    choose = service.getCategory(route).getValue();
                    result = choose.getSubCategories();
                }
            }
            return route;
        }
        catch (Exception e){
            System.out.println("Error, try again");
            return getCategoryInput();
        }
    }

    private void updateProductDemand(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        int demand = Parser.getIntInput("enter product's new demand:");
        Result<SProduct> res = service.updateProductDemand(productNumber,demand);
        Parser.printResult(res, val -> String.format("changed %s demand to - %d successfully%n",val.getName(), demand));
    }

    private void updateProductPrice(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        double price = Parser.getDoubleInput("enter product's new price:");
        Result<SProduct> res = service.updateProductPrice(productNumber,price);
        Parser.printResult(res, val -> String.format("changed %s price to - %f successfully%n", val.getName(),price));
    }

    private void updateProductCategory(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        List<String> route = getCategoryInput();
        Result<SProduct> res = service.updateProductCategory(productNumber,route);
        Parser.printResult(res, val -> String.format("changed %s category to - %s&n", val.getName(), Parser.printList(route)));
    }

    private  void removeProduct(){
        Result<SProduct> res = service.removeProduct(Parser.getStrInput("enter product ID(product number):"));
        Parser.printResult(res, val -> String.format("remove %s%n",val.getName()));

    }
    private void allProductsIDs(){
        Result<List<SProduct>> products = service.getAllProducts();
        Parser.printResult(products, val -> Parser.printList(val, v -> String.format("%s - %s",v.getName(),v.getProductNumber())));
    }

    private void addItems(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        String branch = Parser.getStrInput("enter branch location:");
        String location = Parser.chooseStoreStorage();
        int shelf = Parser.getIntInput("enter shelf number:");
        Date date = Parser.getDateInput("enter expired date(dd-mm-yyyy):");
        int amount = Parser.getIntInput("enter the amount of items to add:");
        Result<Void> res = service.addItems(productNumber,branch,location,shelf,date,amount);
        if(!res.errorOccurred()){
            System.out.printf("%d items of %s add been added successfully%n",amount,service.getProduct(productNumber).getValue().getName());
        }else{
            System.out.println(res.getError());
        }
    }

    private void removeItems(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        String branch = Parser.getStrInput("enter branch location:");
        String location = Parser.chooseStoreStorage();
        int shelf = Parser.getIntInput("enter shelf number:");
        int amount = Parser.getIntInput("enter amount of items to remove:");
        Result<SItem> res = null;
        for (int i = 0; i < amount; i++) {
            res = service.removeItem(productNumber,branch,location,shelf);
            if(res.errorOccurred()){
                System.out.printf("%d items had been removed%n",i);
                System.out.println(res.getError());
                break;
            }
        }
        if(res!= null && !res.errorOccurred()){
            System.out.printf("%d items of %s add been removed successfully%n",amount,service.getProduct(productNumber).getValue().getName());
        }
    }

    private void reportItemAsFlawed(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        String branch = Parser.getStrInput("enter branch location:");
        String location = Parser.chooseStoreStorage();
        int shelf = Parser.getIntInput("enter shelf number:");
        Result<SItem> res = service.reportItemAsFlawed(productNumber,branch,location,shelf);
        Parser.printResult(res, val -> String.format("item of %s reported as flawed successfully", service.getProduct(productNumber).getValue().getName()));
    }
    private void addCategory(){
        String name = Parser.getStrInput("enter name:");
        List<String> route = getCategoryInput();
        Result<SCategory> res = service.addCategory(name, route);
        Parser.printResult(res, val -> String.format("%s category had been added", name));
    }
    private void addDiscountForProduct(){
        String productNumber = Parser.getStrInput("enter product ID(product number):");
        double discount = Parser.getDoubleInput("enter discount:");
        Date from = Parser.getDateInput("enter start date:");
        Date to = Parser.getDateInput("enter end date:");
        Result<SDiscount> res = service.addProductDiscount(productNumber, discount, from, to);
        Parser.printResult(res, val -> String.format("new %f discount for %s from %t to %t",discount, service.getProduct(productNumber).getValue().getName(),from,to));
    }
    private void addDiscountForCategory(){
        List<String> categoryInput = getCategoryInput();
        double discount = Parser.getDoubleInput("enter discount:");
        Date from = Parser.getDateInput("enter start date:");
        Date to = Parser.getDateInput("enter end date:");
        Result<SDiscount> res = service.addCategoryDiscount(categoryInput, discount, from, to);
        Parser.printResult(res, val -> String.format("new %f discount for %s from %t to %t",discount, service.getCategory(categoryInput).getValue().getName(),from,to));
    }

    public void run() {
        String userInput;
        this.printInventoryManagement();
        while (!(userInput = Parser.getStrInput()).equals("0")) {
            switch (userInput) {
                case "1":
                    getProductInfo();
                    break;
                case "2":
                    addProduct();
                    break;
                case "3":
                    updateProductDemand();
                    break;
                case "4":
                    updateProductPrice();
                    break;
                case "5":
                    updateProductCategory();
                    break;
                case "6":
                    removeProduct();
                    break;
                case "7":
                    addItems();
                    break;
                case "8":
                    removeItems();
                    break;
                case "9":
                    reportItemAsFlawed();
                    break;
                case "10":
                    addCategory();
                    break;
                case "11":
                    addDiscountForProduct();
                    break;
                case "12":
                    addDiscountForCategory();
                    break;
                case "13":
                    receiveDelivery();
                default:

            }
            this.printInventoryManagement();
        }
    }
    public void receiveDelivery(){
        Result<SDelivery> delivery = service.getDelivery();
        if(delivery.errorOccurred()){
            System.out.println("no new deliveries arrived yet!!");
        }
        else {
            List<Integer> quantities = new LinkedList<>();
            List<Date> dates = new LinkedList<>();
            List<String> branches = new LinkedList<>();
            List<String> places = new LinkedList<>();
            List<Integer> shelfs = new LinkedList<>();
            for (TransportItemService product : delivery.getValue().getTransportItemServices()) {
                System.out.printf("product name: %s\n" +
                                "product expected quantity: %d\n",
                        product.getItemName(),
                        product.getQuantity());
                quantities.add(Parser.getIntInput("enter product actual quantity:"));
                dates.add(Parser.getDateInput("enter product expired date:"));
                branches.add(Parser.getStrInput("enter branch:"));
                String place = Parser.chooseStoreStorage();
                places.add(place);
                shelfs.add(Parser.getIntInput("enter shelf:"));

            }
            service.receiveDelivery(quantities, dates, branches, places, shelfs);
        }
    }

    public void printInventoryManagement() {
        System.out.println("choose the action:");
        System.out.println("0 - exit(return)");
        System.out.println("[product]");
        System.out.println("1 - get product info");
        System.out.println("2 - add product");
        System.out.println("3 - update product demand");
        System.out.println("4 - update product price");
        System.out.println("5 - update product category");
        System.out.println("6 - remove product");
        System.out.println("[item]");
        System.out.println("7 - add items");
        System.out.println("8 - remove items");
        System.out.println("9 - report item as flawed");
        System.out.println("[category]");
        System.out.println("10 - add category");
        System.out.println("[discount]");
        System.out.println("11 - add discount for product");
        System.out.println("12 - add discount for category");
        System.out.println("[delivery]");
        System.out.println("13 - receive delivery");
    }
}
