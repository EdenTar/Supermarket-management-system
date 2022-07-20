package Backend.Logic.LogicObjects.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.BillOfQuantitiyDAO;

public class SupplierProduct {

    private String cn;
    private String name;
    private String catalogNum;
    private double price;
    private BillOfQuantities billOfQuantities = new BillOfQuantities(cn, name);
    private BillOfQuantitiyDAO billOfQuantitiyDAO;

    public SupplierProduct(String name, String catalogNum, double price, String cn) {
        this.name = name;
        this.catalogNum = catalogNum;
        this.price = price;
        this.cn = cn;
        if (price <= 0) {
            throw new IllegalArgumentException("price cannot be negative!");
        }
        this.billOfQuantitiyDAO = new BillOfQuantitiyDAO();
        BillOfQuantities billOfQuantities = billOfQuantitiyDAO.getFullBillOfQuantities(cn,name);
        if(billOfQuantities != null){
            this.billOfQuantities = billOfQuantities;
        }
    }

    public void setCatalogNum(String catalogNum) {
        this.catalogNum = catalogNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addBillOfQuantitiesRange(int startOfRange, double discountPercentage) {
        removeBillOfQuantitiesData();
        billOfQuantities.addRange(startOfRange,discountPercentage);
        insertBillOfQuantities();

    }

    public void removeBillOfQuantitiesRange(int startOfRage) {
        removeBillOfQuantitiesData();
        billOfQuantities.removeRange(startOfRage);
        insertBillOfQuantities();
    }

    public double getBillOfQuantitiesDiscount(int quantity) {
        return billOfQuantities.getDiscountWithGivenRange(quantity);
    }


    public String getName() {
        return name;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    public double getPrice() {
        return price;
    }

    public double getProductPriceForQuantity(int quantity) {
        double discountPercentage = getBillOfQuantitiesDiscount(quantity);
        double priceForUnit = getPriceForUnitWithDiscount(discountPercentage);
        return priceForUnit * quantity;
    }

    private double getPriceForUnitWithDiscount(double discountPercentage) {
        return price - (price * (discountPercentage / 100));
    }

    public void editPriceForQuantities(int startOfRange, double newDiscount) {
        billOfQuantities.editDiscountForRange(startOfRange, newDiscount);
    }


    public BillOfQuantities getBillOfQuantities() {
        return billOfQuantities;
    }

    public String getCn() {
        return cn;
    }

    public void removeData() {
        removeBillOfQuantitiesData();
    }

    private void removeBillOfQuantitiesData() {
        billOfQuantitiyDAO.deleteRows("cn = '" +  cn + "' AND productName = '" + name + "'");
    }


    public void editCn(String newCn){
        removeBillOfQuantitiesData();
        this.cn = newCn;
        this.billOfQuantities.setCn(newCn);
        insertBillOfQuantities();
    }


    private void insertBillOfQuantities(){
        billOfQuantitiyDAO.insertBillOfQuantity(billOfQuantities);
    }
}
