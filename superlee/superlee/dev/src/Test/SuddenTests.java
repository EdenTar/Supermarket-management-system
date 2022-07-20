package Test;

import Backend.DataAccess.DAOs.TransportDAOs.PointDAO;
import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.Points.Supplier;
import Backend.Logic.Points.Zone;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.HRService;
import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StockKeeperService;
import Backend.ServiceLayer.Facades.ServicePerJob.Supplier.SupplierManagerService;
import Backend.ServiceLayer.Facades.ServicePerJob.Transport.TransportManagerService;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;
import Obj.Parser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class SuddenTests {
    private Starter starter;
    private StockKeeperService stockKeeperService;
    private HRService hrService;
    private SupplierManagerService supplierManagerService;
    private TransportManagerService transportManagerService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    @After
    public void reset(){
        starter.restart();
        starter = Starter.getInstance();
    }
    @Before
    public void setUp(){

    }
    /*
    need to check if user is notified, if order had been added and if supply had been added
     */

    @Test
    public void testShortageNotConsistent(){
        new PointDAO().deleteRow(new Supplier("address","0502142145","non consistent", Zone.HA_SHARON));
        Parser.deleteAllData();
        starter = Starter.getInstance();
        starter.restart();
        starter = Starter.getInstance();
        stockKeeperService = starter.getStockKeeperService();
        hrService = starter.getHrService();
        supplierManagerService = starter.getSupplierManagerService();
        transportManagerService = starter.getManagerService();
        // adds product and category
        stockKeeperService.addCategory("category",new LinkedList<>());
        LinkedList<String> category = new LinkedList<>();
        category.add("category");
        stockKeeperService.addProduct("1","product","manufacturer",1,category,0);
        // adds supplier consistent and non-consistent
        supplierManagerService.addSupplier("1","non consistent","1","EOM","cash","mail@mail.com","non consistent", "0502142145","address");
        supplierManagerService.addSupplier("2","consistent","2","EOM","cash","mail@mail.com","consistent", "0502142145","address");
        supplierManagerService.addSupplierProduct("1","product","1",1);
        supplierManagerService.addSupplierProduct("2","product","1",1);
        // add items
        stockKeeperService.addItems("1","superlee","STORE",1, Parser.getDate("1/1/2030"),10);
        // orders
        UserController userController = starter.getUserController();
        try {
            userController.login(14, "14", "TRANSPORT_MANAGER");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        transportManagerService.insertSupplier("address","0502142145","non consistent", "HA_SHARON");
        //transportManagerService.addTruck();

        supplierManagerService.defineSupplyingNotConsistent("1",3);
        stockKeeperService.updateProductDemand("1",100);
        Assert.assertEquals("[product is under the minimal quantity]\r\n",outContent.toString());
        Result<LinkedList<ServiceOrder>> res1 = stockKeeperService.getSupplierOrders("1");
        if(res1.errorOccurred()){
            System.out.println(res1.getError());
        }
        Assert.assertFalse(res1.errorOccurred());
        Assert.assertEquals(1, res1.getValue().size());
        Assert.assertEquals(1, res1.getValue().get(0).getProducts().size());
        Assert.assertEquals(300, res1.getValue().get(0).getProducts().get(0).getQuantity());
    }






    @Test
    public void testShortageBestPrice(){
        new PointDAO().deleteRow(new Supplier("address","0502142145","non consistent", Zone.HA_SHARON));
        Parser.deleteAllData();
        starter = Starter.getInstance();
        starter.restart();
        starter = Starter.getInstance();
        stockKeeperService = starter.getStockKeeperService();
        hrService = starter.getHrService();
        supplierManagerService = starter.getSupplierManagerService();
        transportManagerService = starter.getManagerService();
        // adds product and category
        stockKeeperService.addCategory("category",new LinkedList<>());
        LinkedList<String> category = new LinkedList<>();
        category.add("category");
        stockKeeperService.addProduct("1","product","manufacturer",1,category,0);
        // adds supplier consistent and non-consistent
        supplierManagerService.addSupplier("1","non consistent","1","EOM","cash","mail@mail.com","non consistent", "0502142145","address");
        supplierManagerService.addSupplier("2","consistent","2","EOM","cash","mail@mail.com","consistent", "0502142145","address");
        supplierManagerService.addSupplierProduct("1","product","1",1);
        supplierManagerService.addSupplierProduct("2","product","1",1);
        // add items
        stockKeeperService.addItems("1","superlee","STORE",1, Parser.getDate("1/1/2030"),10);
        // orders
        UserController userController = starter.getUserController();
        try {
            userController.login(14, "14", "TRANSPORT_MANAGER");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        transportManagerService.insertSupplier("address","0502142145","non consistent", "HA_SHARON");
        //transportManagerService.addTruck();

        supplierManagerService.defineSupplyingNotConsistent("1",3);
        supplierManagerService.defineSupplyingNotConsistent("2",3);
        supplierManagerService.editSupplierProductPrice("2","product",0.5);
        stockKeeperService.updateProductDemand("1",100);
        Assert.assertEquals("[product is under the minimal quantity]\r\n",outContent.toString());
        Result<LinkedList<ServiceOrder>> res = stockKeeperService.getSupplierOrders("2");
        Assert.assertTrue(!res.errorOccurred());
        Assert.assertEquals(res.getValue().size(),1);
        Assert.assertEquals(res.getValue().get(0).getProducts().size(),1);
        Assert.assertEquals(res.getValue().get(0).getProducts().get(0).getQuantity(),300);

    }



}
