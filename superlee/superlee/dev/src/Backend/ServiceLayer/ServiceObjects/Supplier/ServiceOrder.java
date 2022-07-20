package Backend.ServiceLayer.ServiceObjects.Supplier;

import Backend.Logic.LogicObjects.Supplier.Order;
import Backend.Logic.LogicObjects.Supplier.ProductOrder;

import java.util.Date;
import java.util.LinkedList;

public class ServiceOrder {
    private final int id;
    private final String supplierName;
    private final String supplierCn;
    private final String address;
    private final Date date;
    private final String supplierContactPhoneNum;
    private LinkedList<ServiceProductOrder> products;

    public ServiceOrder(Order order){
        LinkedList<ServiceProductOrder> productOrdersSL = new LinkedList<>();
        for(ProductOrder po : order.getProducts()){
            productOrdersSL.add(new ServiceProductOrder(
                    po.getCatalogNum(),
                    po.getName(),
                    po.getQuantity(),
                    po.getTotalPriceWithoutDiscount(),
                    po.getDiscount(),
                    po.getFinalPrice()
            ));
        }
        this.id = order.getId();
        this.supplierName = order.getSupplierName();
        this.supplierCn = order.getSupplierCn();
        this.address = order.getToAddress();
        this.supplierContactPhoneNum = order.getSupplierContactPhoneNum();
        this.products = productOrdersSL;
        this.date = order.getDate();
    }

    public ServiceOrder(int id, String supplierName, String supplierCn, String address,
                 String supplierContactPhoneNum, LinkedList<ServiceProductOrder> products, Date date){
        this.id = id;
        this.address = address;
        this.supplierCn = supplierCn;
        this.supplierName = supplierName;
        this.supplierContactPhoneNum = supplierContactPhoneNum;
        this.date =  date;
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getSupplierCn() {
        return supplierCn;
    }

    public String getSupplierContactPhoneNum() {
        return supplierContactPhoneNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public LinkedList<ServiceProductOrder> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "\n{ " +
                "id = " + id + "\n" +
                "supplierName = '" + supplierName + '\'' + "\n" +
                "supplierCn = '" + supplierCn + '\'' + "\n" +
                "address = '" + address + '\'' + "\n" +
                "date = " + date + "\n" +
                "supplierContactPhoneNum = '" + supplierContactPhoneNum + '\'' + "\n" +
                "products = " + products +
                " }";
    }
}
