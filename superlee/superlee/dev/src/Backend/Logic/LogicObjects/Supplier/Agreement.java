package Backend.Logic.LogicObjects.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.*;


import Backend.DataAccess.DTOs.SupplierDTOS.SupplierProductsDTO;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.Facades.Callbacks.CallbackCheckProductForShortage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Agreement {
    private String cn;
    private HashMap<String, SupplierProduct> products = new HashMap<>();
    private SupplierSchedule schedule;
    private LinkedList<String> productCatalogNums = new LinkedList<>();
    private CallbackCheckProductForShortage checkProductForShortage;

    //dal
    private final SupplierProductsDAO supplierProductsDAO = new SupplierProductsDAO();
    private final SupplierConsistentScheduleDAO supplierConsistentScheduleDAO = new SupplierConsistentScheduleDAO();
    private final SupplierNotConsistentScheduleDAO supplierNotConsistentScheduleDAO = new SupplierNotConsistentScheduleDAO();
    private final SupplierEmptyScheduleDAO supplierEmptyScheduleDAO = new SupplierEmptyScheduleDAO();

    public Agreement(String cn){
        this.cn = cn;
        this.schedule = new EmptySchedule(cn);

    }

    public Agreement(String cn, CallbackCheckProductForShortage checkProductForShortage){
        this(cn);
        this.setCheckProductForShortage(checkProductForShortage);
    }

    public void setCheckProductForShortage(CallbackCheckProductForShortage checkProductForShortage) {
        this.checkProductForShortage = checkProductForShortage;
    }

    //---------- Schedule ----------//


    public String getCn() {
        return cn;
    }

    public LinkedList<Integer> getSupplyingDays(){
        return this.schedule.getDaysList();
    }

    public boolean isScheduleConsistent(){
        return schedule.isScheduleConsistent();
    }

    public void defineSupplyingNotConsistent(int shipmentTime){
        removeScheduleData();
        this.schedule = new NotConsistentSupplierSchedule(cn, shipmentTime);
        insertSchedule();
        // check for shortage for each product name
        checkAllProductForShortage();
    }

    public void defineSupplyingNotConsistentFromDB(int shipmentTime){
        this.schedule = new NotConsistentSupplierSchedule(cn, shipmentTime);
    }

    public void checkAllProductForShortage(){
        for (SupplierProduct p: getProducts()){
            checkProductForShortage.check(p.getName());
        }
    }

    public void defineSupplyingDays(LinkedList<Integer> days){
        removeScheduleData();
        schedule = new ConsistentSupplierSchedule(days);
        insertSchedule();
        // check for shortage for each product name
        checkAllProductForShortage();
    }

    //need to throw exception if the day is already in the list
    public void addSupplyingDay(int day){
        if(!schedule.isScheduleConsistent())
            schedule = new ConsistentSupplierSchedule(cn);

        removeScheduleData();
        ((ConsistentSupplierSchedule)schedule).addSupplyingDay(day);
        insertSchedule();

        // check for shortage for each product name
        checkAllProductForShortage();
    }

    public void addSupplyingDayFromDB(int day){
        if(!schedule.isScheduleConsistent())
            schedule = new ConsistentSupplierSchedule(cn);

        ((ConsistentSupplierSchedule)schedule).addSupplyingDay(day);
    }

    //need to throw exception if the day isnt in the list
    public void removeSupplyingDay(int day){
        if(!schedule.isScheduleConsistent())
            throw new IllegalStateException("this supplier does not have a consistent supplying schedule");

        removeScheduleData();
        ((ConsistentSupplierSchedule)schedule).removeSupplyingDay(day);
        insertSchedule();

        // check for shortage for each product name
        checkAllProductForShortage();
    }

    public int getDaysTillNextShipment(){
        return schedule.getDaysTillNextShipment();
    }

    //---------- Products ----------//

    //this function will also update the product list from the IM and database if needed.
    private boolean doesProductExist(String productName){
        if(products.containsKey(productName))
            return true;

        SupplierProduct sp = supplierProductsDAO.getRow(SupplierProductsDTO.getPK(cn, productName));
        if(sp != null)
            products.put(productName, sp);

        return products.containsKey(productName);

    }

    public void addProduct(String productName, String productCatalogNum, double productPrice){
        if(doesProductExist(productName))
            throw new IllegalStateException("The product " + productName + " already in the agreement");

        SupplierProduct sp = new SupplierProduct(productName, productCatalogNum, productPrice, cn);

        this.products.put(productName, sp);
        this.productCatalogNums.add(productCatalogNum);

        insertProduct(sp);
        // check for shortage for product name
        checkProductForShortage.check(productName);
    }

    public SupplierProduct getProduct(String productName){
        if(!doesProductExist(productName))
            throw new IllegalStateException("The product " + productName + " does not exist in the agreement");

        return this.products.get(productName);
    }

    public void removeProduct(String productName){
        if(!doesProductExist(productName))
            throw new IllegalStateException("The product " + productName + "does not exist in the agreement");

        SupplierProduct sp = getProduct(productName);
        removeProductData(sp);

        this.productCatalogNums.remove(products.remove(productName).getCatalogNum());
    }

    public void editProductCatalogNum(String productName, String newProductCatalogNum){
        if(productCatalogNums.contains(newProductCatalogNum))
            throw new IllegalStateException("The catalog number: " + newProductCatalogNum + " already exists.");

        SupplierProduct sp = getProduct(productName);
        sp.setCatalogNum(newProductCatalogNum);
        updateProductData(sp);
    }

    public void editProductName(String productName, String newProductName){
        if(doesProductExist(newProductName))
            throw new IllegalStateException("The product " + newProductName + "already exists.");

        SupplierProduct sp = getProduct(productName);
        sp.setName(newProductName);
        updateProductData(sp);

        // check for shortage for product name
        checkProductForShortage.check(productName);
    }

    public void editProductPrice(String productName, double newProductPrice){
        SupplierProduct sp = getProduct(productName);
        sp.setPrice(newProductPrice);
        updateProductData(sp);
        // check for shortage for product name
        checkProductForShortage.check(productName);
    }

    public void editCn(String newCn){
        this.cn = newCn;
        for(SupplierProduct sp : getProducts()){
            sp.editCn(newCn);
            updateProductData(sp);
        }
    }

    public void addProductBillOfQuantitiesRange(String productName, int startOfRange, double discountPercentage){
        getProduct(productName).addBillOfQuantitiesRange(startOfRange, discountPercentage);
        // check for shortage for product name
        checkProductForShortage.check(productName);
    }

    public void removeProductBillOfQuantitiesRange(String productName, int startOfRage){
        getProduct(productName).removeBillOfQuantitiesRange(startOfRage);
        // check for shortage for product name
        checkProductForShortage.check(productName);
    }

    public double getProductBillOfQuantitiesDiscount(String productName, int quantity){
        return getProduct(productName).getBillOfQuantitiesDiscount(quantity);
    }

    public LinkedList<SupplierProduct> getProducts(){
        LinkedList<SupplierProduct> products = new LinkedList<>();
        loadAllProducts();
        for(Map.Entry<String, SupplierProduct> entry: this.products.entrySet()){
            products.add(entry.getValue());
        }
        return products;
    }

    public String getProductCatalogNum(String productName){
        return getProduct(productName).getCatalogNum();
    }

    public double getProductPrice(String productName){
        return getProduct(productName).getPrice();
    }

    public double getProductPriceForQuantity(String productName, int quantity){
        return getProduct(productName).getProductPriceForQuantity(quantity);
    }

    public void editProductDiscountForQuantity(String productName, int startOfRange,double newDiscount){
        getProduct(productName).editPriceForQuantities(startOfRange,newDiscount);
        // check for shortage for each product name
        checkAllProductForShortage();
    }

    public BillOfQuantities getProductBillOfQuantities(String productName){
        return products.get(productName).getBillOfQuantities();
    }

    //--------------hw 2 ------------

    public boolean isSupplyingInDay(int day){
        return this.schedule.isShippingInDay(day);
    }

    public int getTimeForTillNextNextShipment(){
        return this.schedule.getTimeForTillNextNextShipment();
    }

    public boolean isSupplierScheduleEmpty(){
        return schedule.isSupplierWithEmptySchedule();
    }

    public void insertData(){
        insertSchedule();
        insertProducts();
    }

    private void insertProducts() {
        for(SupplierProduct p : products.values()){
            insertProduct(p);
        }
    }

    private void insertSchedule() {
        SupplierAgreementDAO supplierAgreementDAO = new SupplierAgreementDAO();
        if(isScheduleConsistent()){
            supplierConsistentScheduleDAO.insertConsistentSchedule((ConsistentSupplierSchedule)schedule);
            supplierAgreementDAO.update(this);
        }

        else if(isSupplierScheduleEmpty()){
            supplierEmptyScheduleDAO.insert((EmptySchedule) schedule);
            supplierAgreementDAO.update(this);
        }


        else{
            supplierNotConsistentScheduleDAO.insert((NotConsistentSupplierSchedule) schedule);
            supplierAgreementDAO.update(this);
        }
    }
    private void insertProduct(SupplierProduct sp){
        supplierProductsDAO.insert(sp);
    }


    public void removeData() {
        removeScheduleData();
        removeProductsData();
    }

    private void removeProductsData() {
        for(SupplierProduct sp : getProducts()){
            removeProduct(sp.getName());
        }
    }

    private void removeProductData(SupplierProduct sp){
        sp.removeData();
        supplierProductsDAO.deleteRow(sp);

    }

    private void removeScheduleData() {
        if(isScheduleConsistent()){
            supplierConsistentScheduleDAO.deleteRows("cn = '" + cn + "'");
        }
        else if(isSupplierScheduleEmpty())
            supplierEmptyScheduleDAO.deleteRow((EmptySchedule) schedule);

        else{
            supplierNotConsistentScheduleDAO.deleteRow((NotConsistentSupplierSchedule) schedule);
        }
    }

    private void updateProductData(SupplierProduct sp){
        supplierProductsDAO.update(sp);
    }

    private void loadAllProducts(){
        for(SupplierProduct sp : supplierProductsDAO.selectAllUnderConditionToBusiness("cn = '" + cn + "'")){
            if(!this.products.containsKey(sp.getName()))
                this.products.put(sp.getName(), sp);
        }
    }
}
