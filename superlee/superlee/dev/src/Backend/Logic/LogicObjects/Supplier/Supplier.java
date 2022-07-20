package Backend.Logic.LogicObjects.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.SupplierAgreementDAO;
import Backend.DataAccess.DAOs.SupplierDAOs.SupplierCardDAO;
import Backend.DataAccess.DAOs.SupplierDAOs.SupplierOrdersDAO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierAgreementDTO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierCardDTO;
import Obj.Pair;
import Backend.ServiceLayer.Facades.Callbacks.CallbackCheckProductForShortage;

import java.util.LinkedList;

public class Supplier {
    private SupplierCard supplierCard;
    private Agreement agreement;
    private LinkedList<Order> orders = new LinkedList<>();

    //dal
    private SupplierCardDAO scDAO = new SupplierCardDAO();
    private SupplierAgreementDAO saDAO = new SupplierAgreementDAO();
    private SupplierOrdersDAO soDAO = new SupplierOrdersDAO();

    public Supplier(String cn) {
        this.supplierCard = scDAO.getRow(SupplierCardDTO.getPK(cn));
        this.agreement = saDAO.getRow(SupplierAgreementDTO.getPK(cn));
    }

    public Supplier(String cn, String supplierName, String bankAccountNum, String paymentMethod, String paymentFrequency, String contactEmail
            , String contactName, String contactPhoneNumber,String address, CallbackCheckProductForShortage checkProductForShortage) {


        this.agreement = new Agreement(cn, checkProductForShortage);
        this.supplierCard = new SupplierCard(cn, supplierName, bankAccountNum, paymentMethod, paymentFrequency,address);
        this.supplierCard.putNewContact(contactPhoneNumber, contactName, contactEmail);
    }

    public void setAgreementCheckProductForShortage(CallbackCheckProductForShortage checkProductForShortage){
        this.agreement.setCheckProductForShortage(checkProductForShortage);
    }


    public Agreement getAgreement() {
        return agreement;
    }

    //need to make sure this function is needed

    public LinkedList<Integer> getSupplyingDays() {
        return this.agreement.getSupplyingDays();
    }


    public SupplierCard getSupplierCard() {
        return supplierCard;
    }

    public void addContact(String contactNumber, String contactName, String contractEmail) {
        supplierCard.addContact(contactNumber, contactName, contractEmail);
    }

    public void removeContact(String contactNumber) {
        supplierCard.removeContact(contactNumber);
    }

    public void editContactName(String contactNumber, String newContactName) {
        supplierCard.editContactName(contactNumber, newContactName);
    }

    public void editContactEmail(String contactNumber, String newContactEmail) {
        supplierCard.editContactEmail(contactNumber, newContactEmail);
    }

    public void editContactNumber(String contactNumber, String newContactNum) {
        supplierCard.editContactNum(contactNumber, newContactNum);

    }
    //TODO MAKE SURE DB UPDATE
    public void editAddress(String address){
        this.supplierCard.setAddress(address);
        scDAO.update(this.supplierCard);
    }

    public void editBankAccountNum(String newBankAccountNum) {
        this.supplierCard.setBankAccountNum(newBankAccountNum);
        scDAO.update(this.supplierCard);
    }

    public void editPaymentMethod(String newPaymentMethod) {
        supplierCard.setPaymentMethod(newPaymentMethod);
        scDAO.update(this.supplierCard);
    }

    public void editPaymentFrequency(String newPaymentFrequency) {
        supplierCard.setPaymentFrequency(newPaymentFrequency);
        scDAO.update(this.supplierCard);
    }

    public void editSupplierName(String newSupplierName) {
        supplierCard.setSupplierName(newSupplierName);
        scDAO.update(this.supplierCard);
    }

    public void editSupplierCn(String newCn) {
        supplierCard.setCn(newCn);
        agreement.editCn(newCn);
    }


    public void defineSupplyingNotConsist(int shipmentTime) {
        this.agreement.defineSupplyingNotConsistent(shipmentTime);
    }


    public void defineSupplyingDays(LinkedList<Integer> days) {
        this.agreement.defineSupplyingDays(days);
    }

    public void addSupplyingDay(int dayIndex) {
        agreement.addSupplyingDay(dayIndex);
    }

    public void removeSupplyingDay(int dayIndex) {
        agreement.removeSupplyingDay(dayIndex);
    }

    public void addProduct(String productName, String productCatalogNum, Double productPrice) {
        agreement.addProduct(productName, productCatalogNum, productPrice);
    }

    public void removeProduct(String productName) {
        agreement.removeProduct(productName);

    }


    public void editProductCatalogNum(String productName, String newProductCatalogNum) {
        agreement.editProductCatalogNum(productName, newProductCatalogNum);
    }

    public void editProductName(String productName, String newProductName) {
        agreement.editProductName(productName, newProductName);
    }

    public void editProductPrice(String productName, double newProductPrice) {
        agreement.editProductPrice(productName, newProductPrice);
    }

    public void addProductBillOfQuantitiesRange(String productName, int startOfRage, double discountPercentage) {
        agreement.addProductBillOfQuantitiesRange(productName, startOfRage, discountPercentage);
    }

    public void removeProductBillOfQuantitiesRange(String productName, int startOfRange) {
        agreement.removeProductBillOfQuantitiesRange(productName, startOfRange);
    }

    public double getProductBillOfQuantitiesDiscount(String productName, int quantity) {
        return agreement.getProductBillOfQuantitiesDiscount(productName, quantity);
    }

    public LinkedList<SupplierProduct> getProducts() {
        return agreement.getProducts();
    }

    public String getProductCatalogNum(String productName) {
        return agreement.getProductCatalogNum(productName);
    }

    public double getProductPrice(String productName) {
        return agreement.getProductPrice(productName);
    }

    public double getProductPriceForQuantity(String productName, int quantity) {
        return agreement.getProductPriceForQuantity(productName, quantity);
    }

    public Order addOrder(String supplierContactPhoneNum, LinkedList<Pair<String, Integer>> products2order,int transportID) {
        //checking if the contact is valid;
        getSupplierContact(supplierContactPhoneNum);
        LinkedList<ProductOrder> product2orderInfo = new LinkedList<>();
        Order order = new Order(
                getSupplierName(),
                getCn(),
                getAddress(),
                supplierContactPhoneNum,
                product2orderInfo,transportID);
        for (Pair<String, Integer> p : products2order) {
            if (p.getValue() < 0) {
                Order.setNextOrderId(Order.getNextOrderId() - 1);
                throw new IllegalArgumentException("quantity can't be negative");

            }
            if (p.getValue() != 0) {
                try {
                    product2orderInfo.add(new ProductOrder(
                            order.getId(),
                            getProductCatalogNum(p.getKey()), //catalogNum
                            p.getKey(), //name
                            p.getValue(), //quantity,
                            getProductPrice(p.getKey()) * p.getValue(), //totalPriceWithoutDiscount
                            getProductBillOfQuantitiesDiscount(p.getKey(), p.getValue()), //discountPercentage
                            getProductPriceForQuantity(p.getKey(), p.getValue()))); //finalPrice
                } catch (Exception e) {
                    Order.setNextOrderId(Order.getNextOrderId() - 1);
                    throw e;
                }
            }
        }
        if (product2orderInfo.isEmpty()) {
            Order.setNextOrderId(Order.getNextOrderId() - 1);
            throw new IllegalArgumentException("the list of products that you are trying to order is empty");
        }

        insertOrder(order);
        orders.add(order);
        return order;
    }

    public Contact getSupplierContact(String contactPhoneNumber) {
        return supplierCard.getContact(contactPhoneNumber);
    }

    public String getCn() {
        return supplierCard.getCn();
    }

    public String getBankAccountNum() {
        return supplierCard.getBankAccountNum();
    }

    public String getPaymentFrequency() {
        return supplierCard.getPaymentFrequency();
    }

    public String getPaymentMethod() {
        return supplierCard.getPaymentMethod();
    }

    public String getSupplierName() {
        return supplierCard.getSupplierName();
    }

    public void editDiscountForRange(String productName, int startOfRange, double newDiscount) {
        agreement.editProductDiscountForQuantity(productName, startOfRange, newDiscount);
    }

    public BillOfQuantities getProductBillOfQuantities(String productName) {
        return agreement.getProductBillOfQuantities(productName);
    }

    public LinkedList<Order> getOrders() {
        loadAllOrders();
        return orders;
    }

    public LinkedList<Contact> getContactList() {
        return supplierCard.getContactList();
    }

    public boolean isSupplierConsistent() {
        return agreement.isScheduleConsistent();
    }

    //-------------- hw 2 -----------------
    public int getDaysTillNextShipment() {
        return agreement.getDaysTillNextShipment();
    }

    public SupplierProduct getProduct(String productName) {
        return agreement.getProduct(productName);
    }

    public boolean isSupplyingInDay(int day) {
        return agreement.isSupplyingInDay(day);
    }

    public int getNextNextShipmentTime() {
        return this.agreement.getTimeForTillNextNextShipment();
    }

    public boolean isSupplierScheduleEmpty() {
        return agreement.isSupplierScheduleEmpty();
    }

    //dal
    public void removeData() {
        removeCardData();
        removeAgreementData();
        //we are not removing orders data.
    }

    private void removeAgreementData() {
        agreement.removeData();
        saDAO.deleteRow(agreement);
    }

    private void removeCardData() {
        supplierCard.removeData();
        scDAO.deleteRow(supplierCard);
    }

    public void insertData() {
        insertCard();
        insertAgreement();
        insertOrders();
    }

    private void insertOrders() {
        for (Order o : orders) {
            insertOrder(o);
        }
    }

    private void insertOrder(Order o) {
        soDAO.insert(o);
        o.insertOrderProducts();
    }

    private void insertAgreement() {
        saDAO.insert(agreement);
        agreement.insertData();
    }

    private void insertCard() {
        scDAO.insert(supplierCard);
        supplierCard.insertData();
    }

    private void loadAllOrders() {
        orders = new LinkedList<>();
        orders.addAll(soDAO.selectAllUnderConditionToBusiness("cn = " + getCn()));
    }

    ///////////////////

    public String getAddress(){
        return supplierCard.getAddress();
    }

    public Order deleteOrder(Order order){
        orders.remove(order);
        deleteOrderData(order);
        return order;
    }

    public Order getOrder(int id){
        for (Order order: orders) {
            if(order.getId() == id)
                return order;
        }
        throw new IllegalArgumentException("invalid supplier for the given order id");
    }

    public void deleteOrderData(Order order) {
        order.deleteData();
        soDAO.deleteRow(order);
    }





}