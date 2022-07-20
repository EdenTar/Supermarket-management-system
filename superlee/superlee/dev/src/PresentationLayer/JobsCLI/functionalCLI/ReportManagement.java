package PresentationLayer.JobsCLI.functionalCLI;

import Backend.Logic.LogicObjects.Report.StockProductInfo;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.ReportsFunctionality;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Product.SLocation;
import Backend.ServiceLayer.ServiceObjects.Report.SDamagedProductInfo;
import Backend.ServiceLayer.ServiceObjects.Report.SProductInfo;
import Backend.ServiceLayer.ServiceObjects.Report.SReport;
import Backend.ServiceLayer.ServiceObjects.Report.SStockProductInfo;
import Obj.Parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ReportManagement {
    private Scanner scanner;
    private ReportsFunctionality service;

    public ReportManagement(ReportsFunctionality service, Scanner scanner){
        this.service = service;
        this.scanner = scanner;
    }

    private void printReportManagement(){
        System.out.println("enter action number:\n" +
                "0 - to go back\n" +
                "1 - get stock report\n" +
                "2 - get damaged report\n" +
                "3 - get expired report");
    }

    public void run() {
        String userInput;
        printReportManagement();
        while (!(userInput = scanner.nextLine()).equals("0")) {
            switch (userInput) {
                case "1":
                    getStockReport();
                    break;
                case "2":
                    getDamagedReport();
                    break;
                case "3":
                    getExpiredReport();
                    break;
                default:
                    System.out.println("invalid input. try again");
            }
            printReportManagement();
        }
    }


    public void getStockReport(){
        List<List<String>> categories = new LinkedList<>();
        String category = Parser.getStrInput("enter category route with ',' between the category names\n" +
                "to finish enter finish");
        while(!category.equals("finish")){
            categories.add(Arrays.asList(category.split(",")));
            category = Parser.getStrInput("enter category route with ',' between the category names\n" +
                    "to finish enter finish");
        }
        if(categories.isEmpty()){
            System.out.println("no categories have been entered");
            return;
        }
        Result<SReport> res = service.getStockReport(categories);
        if(res.errorOccurred()){
            System.out.println(res.getError());
        }
        else{
            System.out.printf("Stock Report date: %s%n", Parser.getStrDate(res.getValue().getDate()));
            for(SProductInfo product: res.getValue().getProducts()){
                if(product instanceof SStockProductInfo) {
                    SStockProductInfo stockProduct = (SStockProductInfo)product;
                    System.out.printf("product number: %s,name: %s,manufacturer: %s,quantity: %d,store: %d,warehouse: %d%n", product.getProductNumber(), product.getName(), product.getManufacturer(),stockProduct.getQuantity(),stockProduct.getStoreQuantity(),stockProduct.getWarehouseQuantity());
                    System.out.println("[LOCATIONS]:");
                    for(SLocation location: stockProduct.getLocations()){
                        System.out.printf("place: %s, shelf: %d%n",location.getPlace(),location.getShelf());
                    }
                }
            }
        }
    }


    public void getDamagedReport(){
        Result<SReport> res = service.getDamageReport();
        if(res.errorOccurred()){
            System.out.println(res.getError());
        }
        else{
            System.out.printf("Damaged Report date: %s%n", Parser.getStrDate(res.getValue().getDate()));
            for(SProductInfo product: res.getValue().getProducts()){
                if(product instanceof SDamagedProductInfo) {
                    SDamagedProductInfo damagedProduct = (SDamagedProductInfo)product;
                    System.out.printf("product number: %s,name: %s,manufacturer: %s,quantity: %d%n", product.getProductNumber(), product.getName(), product.getManufacturer(),damagedProduct.getFlawedQuantity());
                    System.out.println("[LOCATIONS]:");
                    for(SLocation location: damagedProduct.getLocations()){
                        System.out.printf("place: %s, shelf: %d%n",location.getPlace(),location.getShelf());
                    }
                }
            }
        }
    }

    public void getExpiredReport(){
        Result<SReport> res = service.getDamageReport();
        if(res.errorOccurred()){
            System.out.println(res.getError());
        }
        else{
            System.out.printf("Expired Report date: %s", Parser.getStrDate(res.getValue().getDate()));
            for(SProductInfo product: res.getValue().getProducts()){
                if(product instanceof SDamagedProductInfo) {
                    SDamagedProductInfo damagedProduct = (SDamagedProductInfo)product;
                    System.out.printf("product number: %s,name: %s,manufacturer: %s,quantity: %d", product.getProductNumber(), product.getName(), product.getManufacturer(),damagedProduct.getExpiredQuantity());
                    System.out.println("[LOCATIONS]:");
                    for(SLocation location: damagedProduct.getLocations()){
                        System.out.printf("place: %s, shelf: %d",location.getPlace(),location.getShelf());
                    }
                }
            }
        }
    }


}
