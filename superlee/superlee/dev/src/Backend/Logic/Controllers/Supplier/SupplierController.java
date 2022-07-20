package Backend.Logic.Controllers.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.SupplierDAO;
import Backend.DataAccess.DAOs.SupplierDAOs.SupplierOrdersDAO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierDTO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierOrdersDTO;
import Backend.Logic.Controllers.TransportEmployee.OrderManController;
import Backend.Logic.LogicLambdas.SupplierDays;
import Backend.Logic.LogicObjects.Supplier.*;

import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.Logic.Starters.Starter;
import Obj.Pair;
import Backend.ServiceLayer.Facades.Callbacks.CallbackCheckProductForShortage;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetDemandOfProduct;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetProductQuantity;

import java.util.*;
import java.util.stream.Collectors;

public class SupplierController{
    private HashMap<String, Supplier> suppliers = new HashMap<>();
    private CallbackGetProductQuantity getProductQuantity;
    private CallbackCheckProductForShortage checkProductForShortage;
    private CallbackGetDemandOfProduct getDemandOfProduct;
    private final OrderManController orderManController;
    private static Date checkDate;
    public static void deleteCheckDate(){
        checkDate = null;
    }

    //dal
    private SupplierDAO supplierDAO = new SupplierDAO();

    public void lior(){
        suppliers = new HashMap<>();
    }
    public SupplierController(CallbackGetProductQuantity getProductQuantity, CallbackGetDemandOfProduct getDemandOfProduct, CallbackCheckProductForShortage checkProductForShortage,OrderManController orderManController) {
        this.checkProductForShortage = checkProductForShortage;
        this.getDemandOfProduct = getDemandOfProduct;
        this.getProductQuantity = getProductQuantity;
        this.orderManController = orderManController;
        loadOrderId();
        loadAllSuppliersFromDB();
        if(checkDate==null ||checkDate.getDate() != (new Date()).getDate()){
            checkDate = new Date();
            checkSuppliersForDeliveryDay();}

    }

    /*
    * for each consistent Supplier that can supply the next day:
    *
    * */
    public List<Supplier> getConsistentSuppliers(){
        return supplierDAO.getRowsFromDB().stream().filter(supplier -> supplier.isSupplierConsistent()).collect(Collectors.toList());
    }

    public List<Supplier> getSuppliersList(){
        return supplierDAO.getRowsFromDB();
    }

    public boolean isIdealSupplier(Supplier supplier, SupplierProduct product){
        loadAllSuppliersFromDB();
        String productName = product.getName();
        return supplier.getCn().equals(getBestDealForProductByDemand(productName,getDemandOfProduct.get(productName),new LinkedList(suppliers.keySet())).getKey());
    }
    private boolean checkIfSameDate(Date t1, Date t2){
        return t1.getMonth() == t2.getMonth() && t1.getDate() == t2.getDate() && t1.getYear() == t2.getYear();
    }

    public void checkSuppliersForDeliveryDay() {
        for (Supplier sup : getConsistentSuppliers()) {
            if(sup.getDaysTillNextShipment() == 1 &&sup.getOrders().isEmpty()||!checkIfSameDate(sup.getOrders().getLast().getDate(),new Date())) { // check if its the day before delivery day
                LinkedList<Pair<String,Integer>> products2order = new LinkedList<>();
                for (SupplierProduct pro : sup.getProducts()) {
                    if (isIdealSupplier(sup, pro)) {
                        // add order to that supplier
                        String name = pro.getName();
                        int quantity = getQuantityOfProduct(sup,getDemandOfProduct.get(name),getProductQuantity.get(name));
                        if(quantity!=0) {
                            Pair<String, Integer> pair = new Pair<>(pro.getName(), quantity);
                            products2order.add(pair);
                        }
                    } else {
                        // product check missing
                        checkProductForShortage.check(pro.getName());
                    }
                }
                if(products2order.size() != 0) {
                    String cn = sup.getCn();
                    String phone = sup.getContactList().getFirst().getPhoneNumber();
                    String origin;
                    String dest = "superlee";
                    addOrder(cn,phone, products2order);
                }
            }
        }
    }


    public void addSupplier(String supplierCn, String supplierName, String bankAccountNum, String paymentFrequency, String paymentMethod
            , String contactEmail, String contactName, String contactPhoneNumber,String address, CallbackCheckProductForShortage checkProductForShortage) {

        addSupplier(supplierCn, new Supplier(supplierCn, supplierName, bankAccountNum, paymentMethod, paymentFrequency, contactEmail, contactName, contactPhoneNumber,address, checkProductForShortage));
    }

    private void addSupplier(String supplierCn, Supplier supplier) {
        if (doesSupplierExists(supplierCn)) {
            throw new IllegalArgumentException("this supplier already in the system!");
        }
        insertSupplier(supplier);
        suppliers.put(supplierCn, supplier);
    }

    public LinkedList<Order> removeSupplier(String supplierCn) {
        if (!doesSupplierExists(supplierCn))
            throw new IllegalArgumentException("the supplier " + supplierCn + " supplier cn wasn't in the system !");

        Supplier s = getSupplier(supplierCn);
        LinkedList<Order> deletedOrders = new LinkedList<>();
        for(Order o : s.getOrders()){
            try{
                deletedOrders.add(deleteOrder(o));
            }
            catch(Exception ignore){}


        }
        suppliers.remove(supplierCn);
        removeSupplierData(s);
        return deletedOrders;
    }


    public void addSupplierContact(String supplierCn, String contactNumber, String contactName, String contactEmail) {
        getSupplier(supplierCn).addContact(contactNumber, contactName, contactEmail);
    }


    public void removeSupplierContact(String supplierCn, String contactNumber) {
        getSupplier(supplierCn).removeContact(contactNumber);
    }

    public void editSupplierContactName(String supplierCn, String contactNumber, String newContactName) {
        getSupplier(supplierCn).editContactName(contactNumber, newContactName);
    }

    public void editSupplierContactNumber(String supplierCn, String contactNumber, String newContactNumber) {
        getSupplier(supplierCn).editContactNumber(contactNumber, newContactNumber);
    }

    public void editSupplierContactEmail(String supplierCn, String contactNumber, String newContactEmail) {
        getSupplier(supplierCn).editContactEmail(contactNumber, newContactEmail);
    }

    public void editSupplierCn(String supplierCn, String newCn) {
        Supplier supplier = getSupplier(supplierCn);
        removeSupplier(supplierCn);
        supplier.editSupplierCn(newCn);
        addSupplier(newCn, supplier);
    }

    public void editSupplierBankAccountNum(String supplierCn, String newBankAccountNum) {
        getSupplier(supplierCn).editBankAccountNum(newBankAccountNum);
    }


    public void editSupplierPaymentMethod(String supplierCn, String newPaymentMethod) {
        getSupplier(supplierCn).editPaymentMethod(newPaymentMethod);
    }

    public void editSupplierPaymentFrequency(String supplierCn, String newPaymentFrequency) {
        getSupplier(supplierCn).editPaymentFrequency(newPaymentFrequency);
    }

    public void defineSupplyingNotConsistent(String supplierCn, int shipmentTime) {
        getSupplier(supplierCn).defineSupplyingNotConsist(shipmentTime);
    }

    public void defineSupplyingDays(String supplierCn, LinkedList<Integer> days) {
        getSupplier(supplierCn).defineSupplyingDays(days);
    }

    public void addSupplyingDay(String supplierCn, int day) {
        getSupplier(supplierCn).addSupplyingDay(day);
    }

    public void removeSupplyingDay(String supplierCn, int day) {
        getSupplier(supplierCn).removeSupplyingDay(day);
    }

    public void addSupplierProduct(String supplierCn, String productName, String productCatalogNum, double productPrice) {
        getSupplier(supplierCn).addProduct(productName, productCatalogNum, productPrice);
    }

    public void removeSupplierProduct(String supplierCn, String productName) {
        getSupplier(supplierCn).removeProduct(productName);
    }

    public void editSupplierProductCatalogNum(String supplierCn, String productName, String newProductCatalogNum) {
        getSupplier(supplierCn).editProductCatalogNum(productName, newProductCatalogNum);
    }

    public void editSupplierProductName(String supplierCn, String productName, String newProductName) {
        getSupplier(supplierCn).editProductName(productName, newProductName);
    }

    public void editSupplierProductPrice(String supplierCn, String productName, double newProductPrice) {
        getSupplier(supplierCn).editProductPrice(productName, newProductPrice);
    }

    public void addSupplierProductBillOfQuantitiesRange(String supplierCn, String productName, int startOfRange, double discountPercentage) {
        getSupplier(supplierCn).addProductBillOfQuantitiesRange(productName, startOfRange, discountPercentage);
    }

    public void removeSupplierProductBillOfQuantitiesRange(String supplierCn, String productName, int startOfRange) {
        getSupplier(supplierCn).removeProductBillOfQuantitiesRange(productName, startOfRange);
    }

    public LinkedList<SupplierProduct> getSupplierProducts(String supplierCn) {
        return getSupplier(supplierCn).getProducts();
    }

    public String getSupplierProductCatalogNum(String supplierCn, String productName) {
        return getSupplier(supplierCn).getProductCatalogNum(productName);
    }

    public double getSupplierProductPrice(String supplierCn, String productName) {
        return getSupplier(supplierCn).getProductPrice(productName);
    }

    public double getSupplierProductPriceForQuantity(String supplierCn, String productName, int quantity) {
        return getSupplier(supplierCn).getProductPriceForQuantity(productName, quantity);
    }
    //TODO: change origin and destination: Ido Mandel


    public Order addOrder(String supplierCn , String supplierContactPhoneNum, LinkedList<Pair<String, Integer>> products2order) {
        //create transport request
        int transportID = orderManController.createTransportRequest(getSupplier(supplierCn).getAddress(),"branch1",products2order.stream()
                .map(productPair ->
                        new TransportItem(productPair.getKey(),
                                productPair.getValue(),0,""))
                                .collect(Collectors.toList()),supplierCn);

        return getSupplier(supplierCn).addOrder(supplierContactPhoneNum, products2order,transportID);
    }

    public boolean didOrderFromSupplierToday(String supplierCn, String product){
        for(Order o : getSupplier(supplierCn).getOrders()) {
            if (o.getDate().getDate() == new Date().getDate() &&
                    o.getProducts().stream().map(ProductOrder::getName).collect(Collectors.toList()).contains(product)) {

                return true;
            }
        }
        return false;
    }

    private Supplier getSupplier(String supplierCn) {
        if (!doesSupplierExists(supplierCn))
            throw new IllegalStateException("the given supplierCn:" +
                    supplierCn + " is not a registered in the system.");

        return suppliers.get(supplierCn);
    }

    public void editDiscountForRangeForSupplier(String supplierName, String productName, int startOfRange, double newDiscount) {
        getSupplier(supplierName).editDiscountForRange(productName, startOfRange, newDiscount);
    }

    public LinkedList<Order> getSupplierOrders(String supplierCn) {
        return getSupplier(supplierCn).getOrders();
    }

    public BillOfQuantities getSupplierProductBillOfQuantities(String supplierCn, String productName) {
        return getSupplier(supplierCn).getProductBillOfQuantities(productName);
    }

    public LinkedList<Integer> getSupplyingDays(String supplierCn) {
        return this.getSupplier(supplierCn).getSupplyingDays();
    }

    public String getSupplierBankAccountNumber(String supplierCn) {
        return getSupplier(supplierCn).getBankAccountNum();
    }

    public String getSupplierName(String supplierCn) {
        return getSupplier(supplierCn).getSupplierName();
    }

    public String getSupplierPaymentMethod(String supplierCn) {
        return getSupplier(supplierCn).getPaymentMethod();
    }

    public String getSupplierPaymentFrequency(String supplierCn) {
        return getSupplier(supplierCn).getPaymentFrequency();
    }

    public Contact getSupplierContact(String supplierCn, String contactPhoneNumber) {
        return getSupplier(supplierCn).getSupplierContact(contactPhoneNumber);
    }

    public LinkedList<Contact> getSupplierContactList(String supplierCn) {
        return getSupplier(supplierCn).getContactList();
    }

    public void editSupplierName(String supplierCn, String newSupplierName) {
        getSupplier(supplierCn).editSupplierName(newSupplierName);
    }


    // ---------- hw2 ---------- //
    public int getQuantityOfProduct(Supplier supplier, int demand, int quantity){
        int quantityResult = 0;
        int nextNext = supplier.getNextNextShipmentTime();
        int next = supplier.getDaysTillNextShipment();
        if(supplier.isSupplierConsistent()){
            // if shortage
            if(quantity-demand*next<=0){
                quantityResult = demand*(nextNext - next);
            }
            // else if almost shortage
            else if(quantity-demand*nextNext <=0){
                quantityResult = demand*(nextNext)-quantity;
            }
            // no shortage**
            else{
                quantityResult = 0;
            }
        }
        else{
            quantityResult = demand * nextNext;
        }
        return quantityResult;
    }

    public Pair<String, Double> getBestDealForProductByDemand(String productName, int demand, LinkedList<String> productSuppliers) {
        if (productSuppliers.isEmpty())
            throw new IllegalStateException("no Supplier supplies this product");
        productSuppliers.removeIf(cn -> getSupplier(cn).isSupplierScheduleEmpty());


        String minSupplier = productSuppliers.getFirst();
        Supplier supplier = getSupplier(minSupplier);
        int time = supplier.getNextNextShipmentTime();
        double minPrice = supplier.getProductPriceForQuantity(productName,demand * time);
        double minPricePerProduct = minPrice/(demand * time);
        for(String s : productSuppliers) {
            if (!isSupplierScheduleEmpty(s)) {
                //--
                Supplier sSupplier = getSupplier(s);
                int preQuantity = getProductQuantity.get(productName);
                int quantity = getQuantityOfProduct(sSupplier,demand,preQuantity);//demand * getSupplier(s).getNextNextShipmentTime();
                double price = sSupplier.getProductPriceForQuantity(productName, quantity);
                if (price/quantity < minPricePerProduct) {
                    minPricePerProduct = price/quantity;
                    minPrice = price;
                    minSupplier = s;
                }
                if (price / quantity == minPricePerProduct) { //choose the best delivery time
                    if (getSupplier(s).getDaysTillNextShipment() < getSupplier(minSupplier).getDaysTillNextShipment())
                        minSupplier = s;
                }
            }
        }
        return new Pair<>(minSupplier, minPrice);
    }


    private boolean isSupplierScheduleEmpty(String supplierCn) {
        return getSupplier(supplierCn).isSupplierScheduleEmpty();
    }

    @Deprecated
    public Pair<String, Double> getBestDealForProduct(String productName, int quantity, LinkedList<String> productSuppliers) {
        if (productSuppliers.isEmpty())
            throw new IllegalStateException("no Supplier supplies this product");

        String minSupplier = productSuppliers.getFirst();
        double minPrice = getSupplier(minSupplier).getProductPriceForQuantity(productName, quantity);

        for (String s : productSuppliers) {
            if (!isSupplierScheduleEmpty(s)) {
                double price = getSupplier(s).getProductPriceForQuantity(productName, quantity);
                if (price < minPrice) {
                    minPrice = price;
                    minSupplier = s;
                }
                if (price == minPrice) { //choose the best delivery time
                    if (getSupplier(s).getDaysTillNextShipment() < getSupplier(minSupplier).getDaysTillNextShipment())
                        minSupplier = s;
                }
            }
        }
        return new Pair<>(minSupplier, minPrice);
    }

    @Deprecated
    public Pair<String, Double> getBestDealForProductForDay(int day, String productName, int quantity, LinkedList<String> productSuppliers) {
        if (productSuppliers.isEmpty())
            throw new IllegalStateException("no Supplier supplies this product");
        String minSupplier = productSuppliers.getFirst();
        Double minPrice = null;
        for (String s : productSuppliers) {
            if (getSupplier(s).isSupplyingInDay(day)) {
                double price = getSupplier(s).getProductPriceForQuantity(productName, quantity);
                if (minPrice == null) {
                    minPrice = price;
                    minSupplier = s;
                } else if (price < minPrice) {
                    minPrice = price;
                    minSupplier = s;
                }
            }
        }
        if (minPrice != null) {
            return new Pair<>(minSupplier, minPrice);
        }
        return null;
    }

    //will return null if no need to make any special order
    public Order addOrder_bestPrice(String productName, int demand, LinkedList<String> productSuppliers) {
        String bestCn = getBestDealForProductByDemand(productName, demand, productSuppliers).getKey();
        if(didOrderFromSupplierToday(bestCn, productName))
            return null;

        LinkedList<Pair<String, Integer>> products2order = new LinkedList<>();
        products2order.add(new Pair<>(productName, getSupplier(bestCn).getNextNextShipmentTime() * demand));
        //always giving the first contact on the list to the order - probably the manager of the
        // relevant part of the company as usual
        if (!getSupplier(bestCn).isSupplierConsistent())
            return addOrder(bestCn, getSupplierContactList(bestCn).getFirst().getPhoneNumber(), products2order);
        return null;
    }


    public SupplierCard getAllSupplierInfo(String supplierCn) {
        return getSupplier(supplierCn).getSupplierCard();
    }

    public SupplierProduct getAllSupplierProductInfo(String supplierCn, String productName) {
        return getSupplier(supplierCn).getProduct(productName);

    }

    public Integer getTimeTillNextShipmentIdealSupplierShipment(String productName, int demand, LinkedList<String> productSuppliers) {
        for (String supCn: productSuppliers) {
            Supplier sup = getSupplier(supCn);
            if(!sup.isSupplierScheduleEmpty()){
                String supplierCn = getBestDealForProductByDemand(productName, demand, productSuppliers).getKey();
                return getSupplier(supplierCn).getDaysTillNextShipment();
            }
        }
        return -1;
    }


    //hw3
    public Order deleteOrder(int orderId){
        String supplierCn = new SupplierOrdersDAO().getRow(SupplierOrdersDTO.getPK(orderId)).getSupplierCn();
        Order order = getSupplier(supplierCn).getOrder(orderId);
        return deleteOrder(order);
    }

    private Order deleteOrder(Order order){
        orderManController.deleteTransportRequest(order.getTransportID());
        return getSupplier(order.getSupplierCn()).deleteOrder(order);
    }


    //dal
    public void loadAllSuppliersFromDB() {
        List<Supplier> suppliersDB = supplierDAO.getRowsFromDB();
        for (Supplier s : suppliersDB) {
            suppliers.put(s.getCn(), s);
            s.setAgreementCheckProductForShortage(this.checkProductForShortage);
        }
    }

    public void checkAllProductsForShortage(){
        for(Supplier s : suppliers.values()){
            s.getAgreement().checkAllProductForShortage();
        }
    }

    //this function will also load the supplier data from the database if needed
    private boolean doesSupplierExists(String supplierCn) {
        if (suppliers.containsKey(supplierCn))
            return true;

        Supplier s = supplierDAO.getRow(SupplierDTO.getPK(supplierCn));
        if (s != null)
            suppliers.put(supplierCn, s);

        return suppliers.containsKey(supplierCn);
    }

    private void removeSupplierData(Supplier s) {
        s.removeData();
        supplierDAO.deleteRow(s);
    }

    private void insertSupplier(Supplier s) {
        supplierDAO.insert(s);
        s.insertData();
    }

    public void loadOrderId() {
        Order.setNextOrderId(new SupplierOrdersDAO().getNextOrderId());
    }

    public boolean isSupplierConsistent(String supplierCn)
    {
        return getSupplier(supplierCn).isSupplierConsistent();
    }

    public Integer getTimeTillNextShipment(String supplierCn) {
        return getSupplier(supplierCn).getDaysTillNextShipment();
    }

    public SupplierDays getSupplierDaysLambda(){
        return new SupplierDays() {
            @Override
            public boolean isConstSupplier(String supplierId) {
                return isSupplierConsistent(supplierId);
            }

            @Override
            public List<Integer> supplierDays(String supplierId) {
                return getSupplyingDays(supplierId);
            }

            @Override
            public Integer preparationTime(String supplierId) {
                return getTimeTillNextShipment(supplierId);
            }
        };
    }

}
