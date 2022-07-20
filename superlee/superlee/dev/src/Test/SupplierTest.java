/*
package Test;

import Obj.Pair;
import Obj.Parser;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceContact;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceProductOrder;
import Backend.ServiceLayer.Result.Result;
import org.junit.*;

import java.util.LinkedList;
import static org.junit.Assert.*;

public class SupplierTest {

    private StockSupplyService stockSupplyService = new StockSupplyService(System.out::println);

    @Before
    public void setup(){
        Parser.deleteAllData();
        stockSupplyService = new StockSupplyService(System.out::println);
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

    private Result addProductBambaToSupplier1(){
        return stockSupplyService.addSupplierProduct("1", "bamba", "420", 6.9);
    }

    private Result addProductBambaNugatToSupplier1(){
        return stockSupplyService.addSupplierProduct("1", "bambaNugat", "69", 4.20);
    }

    private Result<ServiceOrder> addOrderToSupplier1(int quantityBamba, int quantityBambaNugat){
        LinkedList<Pair<String, Integer>> products = new LinkedList<>();
        products.add(new Pair<>("bamba", quantityBamba));
        products.add(new Pair<>("bambaNugat", quantityBambaNugat));
        return stockSupplyService.addOrder("1", "bash", "0523456779", products);
    }

    private Result<ServiceOrder> orderBambaAdumaToSupplier1(int quantityBambaAduma){
        LinkedList<Pair<String, Integer>> products = new LinkedList<>();
        products.add(new Pair<>("bambaAduma", quantityBambaAduma));
        return stockSupplyService.addOrder("1", "bash", "0523456779", products);
    }

    private Result<ServiceOrder> orderToSupplier1WrongPhone(){
        LinkedList<Pair<String, Integer>> products = new LinkedList<>();
        products.add(new Pair<>("bamba", 10));
        products.add(new Pair<>("bambaNugat", 10));
        return stockSupplyService.addOrder("1", "bash", "052222222", products);
    }

    @Test
    public void addSupplier() {
        Result<String> getNameResult = stockSupplyService.getSupplierName("1");
        Assert.assertFalse(getNameResult.errorOccurred());
        Assert.assertEquals("liorSupplier" ,getNameResult.getValue());

        Result<String> getBankAccountNumResult = stockSupplyService.getSupplierBankAccountNumber("1");
        Assert.assertFalse(getBankAccountNumResult.errorOccurred());
        Assert.assertEquals("123" ,getBankAccountNumResult.getValue());

        Result<String> getPaymentFrequencyResult = stockSupplyService.getSupplierPaymentFrequency("1");
        Assert.assertFalse(getPaymentFrequencyResult.errorOccurred());
        Assert.assertEquals("EOM" ,getPaymentFrequencyResult.getValue());

        Result<String> getPaymentMethodResult = stockSupplyService.getSupplierPaymentMethod("1");
        Assert.assertFalse(getPaymentMethodResult.errorOccurred());
        Assert.assertEquals("credit" ,getPaymentMethodResult.getValue());

        Result<LinkedList<ServiceContact>> getContactListResult = stockSupplyService.getSupplierContactList("1");
        Assert.assertFalse(getContactListResult.errorOccurred());
        Assert.assertEquals(1 ,getContactListResult.getValue().size());
        Assert.assertEquals("lior" ,getContactListResult.getValue().getFirst().getName());
        Assert.assertEquals("lior@gmail.com" ,getContactListResult.getValue().getFirst().getEmail());
        Assert.assertEquals("0523456779" ,getContactListResult.getValue().getFirst().getPhone());

        //trying to add the same supplier again.
        Assert.assertTrue(addSupplier1().errorOccurred());
    }

    @Test
    public void removeSupplier() {
        stockSupplyService.removeSupplier("1");
        assertTrue(stockSupplyService.getSupplierName("1").errorOccurred());
        assertFalse(addSupplier1().errorOccurred());

    }

    @Test
    public void addSupplierContact() {
        assertFalse(addSupplierContactForSupplier1().errorOccurred());
        assertTrue(addSupplierContactForSupplier1().errorOccurred());
        assertTrue(addSupplierContactForSupplier1SameNumberCase().errorOccurred());
        assertFalse(addSupplierContact3ForSupplier1().errorOccurred());
    }

    @Test
    public void removeSupplierContact() {
        //add check if user trying to remove the only contact.
        assertTrue(stockSupplyService.removeSupplierContact("1", "0523456779").errorOccurred());
        assertTrue(stockSupplyService.removeSupplierContact("1", "0523456789").errorOccurred());
        addSupplierContactForSupplier1();
        assertFalse(stockSupplyService.removeSupplierContact("1", "0523456779").errorOccurred());
        assertTrue(stockSupplyService.removeSupplierContact("1", "0523456779").errorOccurred());
    }

    @Test
    public void addSupplierProduct() {
        assertFalse(addProductBambaToSupplier1().errorOccurred());
        Assert.assertEquals(stockSupplyService.getSupplierProductPrice("1", "bamba").getValue(),6.9,0);
        Assert.assertEquals(stockSupplyService.getSupplierProductCatalogNum("1","bamba").getValue(),"420");
        assertTrue(addProductBambaToSupplier1().errorOccurred());
    }

    @Test
    public void removeSupplierProduct() {
        addProductBambaToSupplier1().errorOccurred();
        assertFalse(stockSupplyService.removeSupplierProduct("1","bamba").errorOccurred());
        assertTrue(stockSupplyService.removeSupplierProduct("1","bamba").errorOccurred());

        addProductBambaNugatToSupplier1().errorOccurred();
        assertFalse(stockSupplyService.removeSupplierProduct("1","bambaNugat").errorOccurred());
        assertTrue(stockSupplyService.removeSupplierProduct("1","bambaNugat").errorOccurred());
    }

    @Test
    public void addSupplierProductBillOfQuantitiesRange() {
        addProductBambaToSupplier1();
        assertFalse(stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",10,50).errorOccurred());
        assertFalse(stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",20,80).errorOccurred());
        assertEquals("[{ [1 -> 10) = 0.0% }, { [10 -> 20) = 50.0% }, { [20 -> inf) = 80.0% }]", stockSupplyService.getBillOfQuantityForAProduct("1","bamba").getValue().toString());
        assertFalse(stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",10,75).errorOccurred());
        assertEquals("[{ [1 -> 10) = 0.0% }, { [10 -> 20) = 75.0% }, { [20 -> inf) = 80.0% }]", stockSupplyService.getBillOfQuantityForAProduct("1","bamba").getValue().toString());
    }

    @Test
    public void getSupplierProductPriceForQuantity() {
        addProductBambaToSupplier1();
        assertFalse(stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",10,50).errorOccurred());
        assertFalse(stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",20,80).errorOccurred());
        assertEquals((6.9)*(15*((100 - 50.0)/100)),
                stockSupplyService.getSupplierProductPriceForQuantity("1", "bamba", 15).getValue(),0.001);
        assertEquals((6.9)*(50*((100 - 80.0)/100)),
                stockSupplyService.getSupplierProductPriceForQuantity("1", "bamba", 50).getValue(),0.001);
    }

    @Test
    public void removeSupplierProductBillOfQuantitiesRange() {
        addProductBambaToSupplier1();
        stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",10,50).errorOccurred();
        stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",20,80).errorOccurred();
        assertFalse(stockSupplyService.removeSupplierProductBillOfQuantitiesRange("1","bamba",10).errorOccurred());
        assertEquals((6.9)*15,
                stockSupplyService.getSupplierProductPriceForQuantity("1", "bamba", 15).getValue(),0.001);
        assertTrue(stockSupplyService.removeSupplierProductBillOfQuantitiesRange("1","bamba",10).errorOccurred());
        assertFalse(stockSupplyService.removeSupplierProductBillOfQuantitiesRange("1","bamba",20).errorOccurred());
        assertEquals((6.9)*20,
                stockSupplyService.getSupplierProductPriceForQuantity("1", "bamba", 20).getValue(),0.001);


    }

    @Test
    public void addOrder() {
        addProductBambaToSupplier1();
        addProductBambaNugatToSupplier1();

        //ordering a negative quantity
        assertTrue(addOrderToSupplier1(-1, 10).errorOccurred());

        //ordering with an empty product list
        assertTrue(addOrderToSupplier1(0, 0).errorOccurred());

        //ordering a product the supplier does not supply
        assertTrue(orderBambaAdumaToSupplier1(10).errorOccurred());

        //ordering with a wrong phone number
        assertTrue(orderToSupplier1WrongPhone().errorOccurred());

        //ordering, expected no errors:
        Result<ServiceOrder> result = addOrderToSupplier1(10, 20);
        assertFalse(result.errorOccurred());
        assertEquals("bash", result.getValue().getAddress());
        assertEquals(0, result.getValue().getId());
        assertEquals("liorSupplier", result.getValue().getSupplierName());
        assertEquals("0523456779", result.getValue().getSupplierContactPhoneNum());

        LinkedList<ServiceProductOrder> products = result.getValue().getProducts();
        assertEquals(2, products.size());
        for(ServiceProductOrder p : products){
            if(p.getName().equals("bamba")){
                assertEquals("420", p.getCatalogNum());
                assertEquals(10, p.getQuantity());
                assertEquals(69, p.getTotalPriceWithoutDiscount(),0);
                assertEquals(0, p.getTotalDiscount(),0);
                assertEquals(69, p.getFinalPrice(),0);
            }
            else if(p.getName().equals("bambaNugat")){
                assertEquals("69", p.getCatalogNum());
                assertEquals(20, p.getQuantity());
                assertEquals(84, p.getTotalPriceWithoutDiscount(),0);
                assertEquals(0, p.getTotalDiscount(),0);
                assertEquals(84, p.getFinalPrice(),0);
            }
            else{
                Assert.fail();
            }
        }

        //with bill of quantities
        stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",10,50).errorOccurred();
        stockSupplyService.addSupplierProductBillOfQuantitiesRange("1","bamba",20,80).errorOccurred();
        Result<ServiceOrder> result2 = addOrderToSupplier1(15, 0);
        assertFalse(result2.errorOccurred());
        assertEquals("bash", result2.getValue().getAddress());
        assertEquals(1, result2.getValue().getId());
        assertEquals("liorSupplier", result2.getValue().getSupplierName());
        assertEquals("0523456779", result2.getValue().getSupplierContactPhoneNum());

        LinkedList<ServiceProductOrder> products2 = result2.getValue().getProducts();
        assertEquals(1, products2.size());
        assertEquals("bamba", products2.getFirst().getName());
        assertEquals("420", products2.getFirst().getCatalogNum());
        assertEquals(15, products2.getFirst().getQuantity());
        assertEquals(15*6.9, products2.getFirst().getTotalPriceWithoutDiscount(),0);
        assertEquals(50, products2.getFirst().getTotalDiscount(),0);
        assertEquals(15*6.9/2, products2.getFirst().getFinalPrice(),0);
    }



    @AfterClass
    public static void tearDown(){
        Parser.deleteAllData();
    }
}*/
