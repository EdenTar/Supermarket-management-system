package Test;

import Backend.DataAccess.DAOs.StockDAOs.ProductsDAO;
import Backend.Logic.LogicObjects.Product.Item;
import Backend.Logic.LogicObjects.Product.Location;
import Backend.Logic.LogicObjects.Product.Product;
import Backend.Logic.LogicObjects.Report.Category;
import Obj.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProductTest {

    private Product product1, product2;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    @Before
    public void setUp() throws Exception {
        product1 = new Product("1","milk","ronaldo", 4.4,new Category(1,"milk products", null),2,(name, demand) -> 1, (add, dem, name) -> {String d = "" + dem;}, (n) -> {n = n;});
        product2 = new Product("2","milk","ronaldo", 4.4,new Category(2,"milk products", null), 4,(name, demand) -> 1, (add, dem, name) -> {String d = "" + dem;}, (n) -> {n = n;});
        new ProductsDAO().insert(product1);
        new ProductsDAO().insert(product2);
    }

    @After
    public void tearDown() throws Exception {
        Parser.deleteAllData();
    }

    private Date getDate(String date){
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }


    @Test
    public void getDemand() {
        try {
            assertEquals("demand shouldn't be this", 2, product1.getDemandPerDay());
            assertEquals("demand shouldn't be this", 4, product2.getDemandPerDay());
        }catch (Exception e){
            assertEquals("error " + e.getMessage(),false, true );
        }
    }

    @Test
    public void setDemand() {
        try {
            product1.setDemand(5);
            assertEquals("demand shouldn't be this", 5, product1.getDemandPerDay());
            product1.setDemand(3);
            assertEquals("demand shouldn't be this", 3, product1.getDemandPerDay());
        }
        catch (Exception e){
            assertEquals("fail " + e.getMessage(), false, true);
        }
    }

    @Test
    public void getFlawedItems() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate("1-1-1999"),1);
                if(i%2 == 0) {
                    product1.getNotFlawedItem("c",Location.StoreOrStorage.STORE, 1).setFlaw(true);
                }
            }
            assertEquals("flawed items size should be 50", 50, product1.getFlawedItems().size());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getLocationsOfFlawedItems() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate("1-1-1999"),1);
                if(i%2 == 0) {
                    product1.getNotFlawedItem("c",Location.StoreOrStorage.STORE, 1).setFlaw(true);
                }
            }
            assertEquals("flawed items locations should be only (c, STORE, 1)", product1.getLocationsOfFlawedItems().size(), product1.getLocationsOfFlawedItems().stream().filter(t -> t.equals(new Location("1" , 0,"c", Location.StoreOrStorage.STORE, 1))).collect(Collectors.toList()).size());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getLocationOfExpiredItems() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
            }
            assertEquals("expired items locations should be only (c, STORE, 1)", product1.getLocationOfExpiredItems().size(), product1.getLocationOfExpiredItems().stream().filter(t -> t.equals(new Location("1", 0 ,"c", Location.StoreOrStorage.STORE, 1))).collect(Collectors.toList()).size());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getLocationOfDamagedItems() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
            }
            assertEquals("damaged items locations should be only (c, STORE, 1)", product1.getLocationOfDamagedItems().size(), product1.getLocationOfDamagedItems().stream().filter(t -> t.equals(new Location("1", 0,"c", Location.StoreOrStorage.STORE, 1))).collect(Collectors.toList()).size());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getWarehouseQuantity() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", (i%2 == 0) ? Location.StoreOrStorage.STORE : Location.StoreOrStorage.STORAGE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
            }
            assertEquals("warehouse quantity isn't right", 50, product1.getWarehouseQuantity());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getStoreQuantity() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", (i%2 == 0) ? Location.StoreOrStorage.STORE : Location.StoreOrStorage.STORAGE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
            }
            assertEquals("store quantity isn't right", 50, product1.getStoreQuantity());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getExpiredQuantity() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
            }
            assertEquals("expired quantity isn't right", 50, product1.getExpiredQuantity());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getFlawedQuantity() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate((i%2 == 0) ? "1-1-1999" : "1-1-2023"),1);
                if(i%2 == 0){
                    product1.getNotFlawedItem("c", Location.StoreOrStorage.STORE, 1).setFlaw(true);
                }
            }
            assertEquals("expired quantity isn't right", 50, product1.getFlawedQuantity());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }

    @Test
    public void getDamagedQuantity() {
        try {
            for (int i = 0; i < 100; i++) {
                product1.addItems("c", Location.StoreOrStorage.STORE, 1, getDate((i % 2 == 0) ? "1-1-1999" : "1-1-2023"),1);
                if (i % 2 == 1){
                    product1.getNotFlawedItem("c", Location.StoreOrStorage.STORE, 1).setFlaw(true);
                }
            }
            assertEquals("damaged quantity isn't right", 75, product1.getDamagedQuantity());
        }
        catch (Exception e){
            fail("fail " + e.getMessage());
        }
    }
}