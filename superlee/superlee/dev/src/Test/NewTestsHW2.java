/*
package Test;

import Backend.Logic.Controllers.Supplier.SupplierController;
import Obj.Pair;
import Obj.Parser;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Facades.Callbacks.CallbackCheckProductForShortage;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetDemandOfProduct;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetProductQuantity;
import Backend.ServiceLayer.Services.StockSupplyService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class NewTestsHW2 {

    private StockSupplyService stockSupplyService = new StockSupplyService(System.out::println);
    private SupplierController supplierController;


    @Before
    public void setup() {
        Parser.deleteAllData();
        stockSupplyService = new StockSupplyService(System.out::println);
        CallbackGetDemandOfProduct getDemandOfProduct = productName -> stockSupplyService.getProductDemand(productName).getValue();
        CallbackGetProductQuantity getProductQuantity = productName -> stockSupplyService.getProductQuantity(productName).getValue();
        CallbackCheckProductForShortage checkProductForShortage = productName -> stockSupplyService.checkForShortage("Supply Manager",productName);
        this.supplierController = new SupplierController(getProductQuantity,getDemandOfProduct,checkProductForShortage);
        addSupplier1();
    }

    private Result addSupplierContactForSupplier1(){
        return stockSupplyService.addSupplierContact("1","0524321538","eyalzloof","eyalzloof@gmail.com");
    }

private Result addSupplierContactForSupplier1SameNumberCase(){
        return stockSupplyService.addSupplierContact("1","0524321538","liorIsia","eyalzloof@gmail.com");
    }

    private Result addSupplierContact3ForSupplier1(){
        return stockSupplyService.addSupplierContact("1","0529621238","eitan","eitanshalom@gmail.com");
    }

    private Result addSupplier1(){
        return stockSupplyService.addSupplier("1",
                "liorSupplier",
                "123",
                "EOM",
                "credit",
                "lior@gmail.com",
                "lior",
                "0523456779");
    }

    private Result addSupplier2(){
        return stockSupplyService.addSupplier("2",
                "eyalSupplier",
                "111",
                "EOM",
                "credit",
                "eyal@gmail.com",
                "eyal",
                "0525252252");
    }

    private Result addSupplier3(){
        return stockSupplyService.addSupplier("3",
                "spiderman",
                "3232",
                "EOM",
                "credit",
                "spider@gmail.com",
                "spider",
                "0551548158");
    }
    private Result addSupplier4(){
        return stockSupplyService.addSupplier("4",
                "spiderman",
                "3535",
                "EOM",
                "credit",
                "vodka@gmail.com",
                "vodka",
                "0556948158");
    }

    private Result addProductBambaToSupplier1(){
        return stockSupplyService.addSupplierProduct("1", "bamba", "420", 6.9);
    }

    private Result addProductBambaToSupplier4(){
        return stockSupplyService.addSupplierProduct("4", "bamba", "420", 6.9);
    }

    private Result addProductBambaToSupplier2(){
        return stockSupplyService.addSupplierProduct("2", "bamba", "420", 42.0);
    }
    private Result addProductBambaToSupplier3(){
        return stockSupplyService.addSupplierProduct("3", "bamba", "420", 69.0);
    }

    private Result addProductBambaNugatToSupplier1(){
        return stockSupplyService.addSupplierProduct("1", "bambaNugat", "69", 4.20);
    }

    private Result makeSupplierConsistent(){
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add((day+2)%7);
        days.add((day+4)%7);
        return stockSupplyService.defineSupplyingDays("1",days);
    }
    private Result makeCategories(){
        return stockSupplyService.addCategory("Supply Manager","fruit",new LinkedList<>());
    }
    private Result makeProduct(){
        LinkedList<String> cat = new LinkedList<>();
        cat.add("fruit");
        return stockSupplyService.addProduct("Supply Manager","1","banana", "lior and sons", 6.8,cat,4);
    }
    private Result addItems(){
        stockSupplyService.addItem("Supply Manager", "1", "superlee","STORE",5,Parser.getDate("23/06/2022"));
        stockSupplyService.addItem("Supply Manager", "1", "superlee","STORE",5,Parser.getDate("23/06/2022"));
        stockSupplyService.addItem("Supply Manager", "1", "superlee","STORE",5,Parser.getDate("23/06/2022"));
        return stockSupplyService.addItem("Supply Manager", "1", "superlee","STORE",5,Parser.getDate("23/06/2022"));
    }


    @Test
    public void getBestDealForProductByDemand_noSuppliers(){
        try{
            supplierController.getBestDealForProductByDemand("bamba", 20, new LinkedList<>());
            Assert.fail();
        }

        catch (Exception ignored){}

    }
    @Test
    public void getBestDealForProductByDemand_multipleSuppliers(){
        addSupplier2();
        addSupplier3();
        supplierController.defineSupplyingNotConsistent("1", 1);
        supplierController.defineSupplyingNotConsistent("2", 2);
        supplierController.defineSupplyingNotConsistent("3", 3);
        supplierController.addSupplierProduct("1", "bamba", "420", 6.9);
        supplierController.addSupplierProduct("2", "bamba", "420", 4.2);
        supplierController.addSupplierProduct("3", "bamba", "420", 10.2);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        String[] supplierCns = {"1","2","3"};
        LinkedList<String> supplierCnList = new LinkedList<>(Arrays.asList(supplierCns));
        try{
            Pair<String, Double> p =  supplierController.getBestDealForProductByDemand("bamba", 10, supplierCnList);
            Assert.assertEquals("2", p.getKey());
            Assert.assertEquals((Double)84.0, p.getValue());
        }
        catch (Exception ignore){
            System.out.println(ignore.getMessage());
            Assert.fail(ignore.getMessage());
        }
    }


    @Test
    public void checkCannotSupplyingTimeForNotDefinedProduct(){
        addSupplier1();
        addProductBambaToSupplier1();
        stockSupplyService.defineSupplyingNotConsistent("1",5);
        Assert.assertTrue(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
    }

    @Test
    public void checkGetSupplyingTimeForDefinedProductNotConsistent(){
        addSupplier1();
        addProductBambaToSupplier1();
        stockSupplyService.defineSupplyingNotConsistent("1",5);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        Assert.assertFalse(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
        Assert.assertEquals(5, stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).getValue().intValue());
    }

    @Test
    public void checkGetSupplyingTimeForDefinedProductConsistent(){
        addSupplier1();
        addProductBambaToSupplier1();
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add((day+2)%7);
        days.add((day+4)%7);
        stockSupplyService.defineSupplyingDays("1", days);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        Assert.assertFalse(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
        Assert.assertEquals(2, stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).getValue().intValue());
    }

    @Test
    public void checkCannotGetSupplyingTimeForNotDefinedProductConsistent(){
        addSupplier1();
        addProductBambaToSupplier1();
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add((day+2)%7);
        days.add((day+4)%7);
        stockSupplyService.defineSupplyingDays("1", days);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        Assert.assertTrue(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
    }

    @Test
    public void checkSupplyingTimeForConsistentSupplierTodayCase(){
        addSupplier1();
        addProductBambaToSupplier1();
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add(day);
        stockSupplyService.defineSupplyingDays("1", days);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        //Assert.assertEquals(7,service.getTimeTillNextShipmentIdealSupplierShipment("bamba",5));
    }

    @Test
    public void checkGetSupplyingTimeForDefinedProductNotConsistent2Suppliers(){
        addSupplier1();
        addSupplier2();
        addProductBambaToSupplier1();
        addProductBambaToSupplier2();
        stockSupplyService.defineSupplyingNotConsistent("1",5);
        stockSupplyService.defineSupplyingNotConsistent("2",6);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        Assert.assertFalse(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
        Assert.assertEquals(5, stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).getValue().intValue());
    }

    @Test
    public void checkGetSupplyingTimeForDefinedProductConsistent2Suppliers(){
        addSupplier1();
        addSupplier2();
        addProductBambaToSupplier1();
        addProductBambaToSupplier2();
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add((day+2)%7);
        days.add((day+4)%7);
        stockSupplyService.defineSupplyingDays("1", days);
        days = new LinkedList<>();
        days.add((day+3)%7);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        Assert.assertFalse(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
        Assert.assertEquals(2, stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).getValue().intValue());
    }
    @Test
    public void checkGetSupplyingTimeForDefinedProductConsistent2SuppliersSamePrice(){
        addSupplier1();
        addSupplier4();
        addProductBambaToSupplier1();
        addProductBambaToSupplier4();
        LinkedList<Integer> days = new LinkedList<>();
        Date date = new Date();
        int day = date.getDay();
        days.add((day+2)%7);
        days.add((day+4)%7);
        stockSupplyService.defineSupplyingDays("1", days);
        days = new LinkedList<>();
        days.add((day+3)%7);
        stockSupplyService.defineSupplyingDays("4", days);
        stockSupplyService.addCategory("Supply Manager","snacks",new LinkedList<>());
        LinkedList<String> route = new LinkedList<>();
        route.add("snacks");
        stockSupplyService.addProduct("Supply Manager","1","bamba","eyal und lior",4.20,route,69);
        Assert.assertFalse(stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).errorOccurred());
        Assert.assertEquals(2, stockSupplyService.getTimeTillNextShipmentIdealSupplierShipment("bamba",5).getValue().intValue());
    }

    private void loadGivenData(){

        Result r1 = stockSupplyService.addSupplier("1","supplier1","123", "EOM", "credit",
                "contact11@gmail.com", "contact11", "0523456711");
        if(r1.errorOccurred()){
            printResult(r1);
            return;
        }

        Result r2 = stockSupplyService.addSupplierContact("1","0523456712","contact12","contact12@gmail.com");
        if(r2.errorOccurred()){
            printResult(r2);
            return;
        }

        Result r3 = stockSupplyService.addSupplierContact("1","0523456713","contact13", "contact13@gmail.com");
        if(r3.errorOccurred()){
            printResult(r3);
            return;
        }

        Result r4 = stockSupplyService.addSupplierProduct("1","bamba","10",5.0);
        if(r4.errorOccurred()){
            printResult(r4);
            return;
        }

        Result r5 = stockSupplyService.addSupplierProduct("1","bambaNugat","11",10.0);
        if(r5.errorOccurred()){
            printResult(r5);
            return;
        }

        Result r6 = stockSupplyService.addSupplierProductBillOfQuantitiesRange("1", "bamba", 50, 20.0);
        printResult(r6);

    }
    private static <T> void printResult(Result<T> result){
        if(result.errorOccurred()){
            System.out.println("Error! : " + result.getError());
        }else{
            System.out.println((result.getValue() != null) ? result.getValue() : "finished action successfully!");
        }
    }
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    @Test
    public void testRemoveItem() throws ParseException {
        Parser.deleteAllData();
        stockSupplyService = new StockSupplyService(System.out::println);
        loadGivenData();
        stockSupplyService.addCategory("Supply Manager","t",new LinkedList<>());//"t"
        LinkedList r = new LinkedList<>();
        r.add("t");
        stockSupplyService.defineSupplyingNotConsistent("1",4);
        stockSupplyService.addProduct("Supply Manager","1","bamba","l",6.9,r,10);//"bamba"
        stockSupplyService.addItem("Supply Manager","1","superlee","STORE",1,simpleDateFormat.parse("1-1-2023"));//"0"
        stockSupplyService.removeItem("Supply Manager","1","superlee","STORE",1);//"
    }

    @AfterClass
    public static void tearDown(){
        Parser.deleteAllData();
    }

}*/

