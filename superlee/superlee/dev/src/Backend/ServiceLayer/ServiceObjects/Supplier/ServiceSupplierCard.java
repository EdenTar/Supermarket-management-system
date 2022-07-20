package Backend.ServiceLayer.ServiceObjects.Supplier;

import Backend.Logic.LogicObjects.Supplier.Contact;
import Backend.Logic.LogicObjects.Supplier.SupplierCard;

import java.util.LinkedList;

public class ServiceSupplierCard {
    private final String cn;
    private final String supplierName;
    private final String backAccountNum;
    private final String paymentMethod;
    private final String paymentFrequency;
    private final LinkedList<ServiceContact> contacts;

    public ServiceSupplierCard(String cn, String supplierName, String bankAccountNum, String paymentMethod, String paymentFrequency,
                               LinkedList<ServiceContact> contacts){
        this.backAccountNum = bankAccountNum;
        this.supplierName = supplierName;
        this.cn = cn;
        this.contacts = contacts;
        this.paymentFrequency = paymentFrequency;
        this.paymentMethod = paymentMethod;
    }

    public ServiceSupplierCard(SupplierCard supplierCard){
        this.cn = supplierCard.getCn();
        this.supplierName = supplierCard.getSupplierName();
        this.backAccountNum = supplierCard.getBankAccountNum();
        this.contacts = new LinkedList<>();
        this.paymentMethod = supplierCard.getPaymentMethod();
        this.paymentFrequency = supplierCard.getPaymentFrequency();
        for (Contact c : supplierCard.getContactList()) {
            this.contacts.add(new ServiceContact(c));
        }
    }

    public String getCn() {
        return cn;
    }
    public String getBackAccountNum() {
        return backAccountNum;
    }
    public String getPaymentFrequency() {
        return paymentFrequency;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public LinkedList<ServiceContact> getContacts() {
        return contacts;
    }
    public String getSupplierName(){
        return supplierName;
    }

    @Override
    public String toString() {
        return "{ " +
                "cn = '" + cn + '\'' + "\n" +
                "backAccountNum = '" + backAccountNum + '\'' + "\n" +
                "paymentMethod = '" + paymentMethod + '\'' + "\n" +
                "paymentFrequency = '" + paymentFrequency + '\'' + "\n" +
                "contacts = " + contacts +
                " }";
    }
}
