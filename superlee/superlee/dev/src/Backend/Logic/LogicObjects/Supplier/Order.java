package Backend.Logic.LogicObjects.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.ProductOrderDAO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierOrdersDTO;
import Obj.Parser;

import java.util.Date;
import java.util.LinkedList;

public class Order {
    private static int nextOrderId;
    private final int id;
    private final String supplierName;
    private final String supplierCn;
    private final String fromAddress;
    private String toAddress = "branch1";
    private final Date date;
    private final String supplierContactPhoneNum;
    private final int transportID;
    private final LinkedList<ProductOrder> products;

    //dal
    private final ProductOrderDAO productOrderDAO = new ProductOrderDAO();

    public Order(String supplierName, String supplierCn, String fromAddress,
                 String supplierContactPhoneNum, LinkedList<ProductOrder> products,int transportID){
        this.id = nextOrderId;
        nextOrderId++;
        this.fromAddress = fromAddress;
        this.supplierCn = supplierCn;
        this.supplierName = supplierName;
        this.supplierContactPhoneNum = supplierContactPhoneNum;
        this.date =  new Date(); //current time
        this.products = products;
        this.transportID = transportID;
    }

    public Order(String supplierName, String supplierCn, String fromAddress, String toAddress,
                 String supplierContactPhoneNum, LinkedList<ProductOrder> products, int transportID){
        this(supplierName, supplierCn, fromAddress, supplierContactPhoneNum, products, transportID);
        this.toAddress = toAddress;
    }

    public Order(SupplierOrdersDTO dto, LinkedList<ProductOrder> products){
        this.id = (int) dto.getOrderId();
        this.fromAddress = dto.getFromAddress();
        this.toAddress = dto.getToAddress();
        this.supplierCn = dto.getCn();
        this.date = Parser.getDate(dto.getDate());
        this.products = products;
        this.supplierName = dto.getSupplierName();
        this.supplierContactPhoneNum = dto.getContactPhoneNumber();
        this.transportID = (int) dto.getTransportID();

    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public static int getNextOrderId() {
        return nextOrderId;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {return toAddress;}

    public String getSupplierCn() {
        return supplierCn;
    }

    public String getSupplierContactPhoneNum() {
        return supplierContactPhoneNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public LinkedList<ProductOrder> getProducts() {
        return products;
    }

    //dal
    public static void setNextOrderId(int newNextOrderId){
        nextOrderId = newNextOrderId;
    }

    public void insertOrderProducts() {
        for(ProductOrder p : products){
            insertProductOrder(p);
        }
    }

    private void insertProductOrder(ProductOrder p) {
        productOrderDAO.insert(p);
    }

    public void deleteData(){
        for(ProductOrder p : products){
            deleteProduct(p);
        }
    }

    private void deleteProduct(ProductOrder p){
        productOrderDAO.deleteRow(p);
    }

    /////////////////////


    public int getTransportID() {
        return transportID;
    }
}
