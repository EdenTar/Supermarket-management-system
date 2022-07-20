package Backend.Logic.LogicObjects.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.SupplierContactsDAO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierContactsDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class SupplierCard {

    private String supplierName;
    private String cn;
    private String bankAccountNum;
    private String paymentMethod;
    private String paymentFrequency;
    private String address;
    private HashMap<String,Contact> contactsNumToContact = new HashMap<>();
    private final LinkedList<String> paymentMethods = new LinkedList<String>(Arrays.asList("cash", "credit", "transferOfFunds"));
    private final LinkedList<String> paymentFrequencies = new LinkedList<String>(Arrays.asList("immediate","EOM", "EO2M"));

    //dal
    private SupplierContactsDAO contactsDAO = new SupplierContactsDAO();

    public SupplierCard(String cn,String supplierName, String bankAccountNum, String paymentMethod, String paymentFrequency,String address){
        setCn(cn);
        setAddress(address);
        setSupplierName(supplierName);
        setBankAccountNum(bankAccountNum);
        setPaymentMethod(paymentMethod);
        setPaymentFrequency(paymentFrequency);
    }



    //this function will also load the data from the database if needed
    private boolean doesContactExist(String contactNumber){
        if(contactsNumToContact.containsKey(contactNumber))
            return true;

        Contact c = contactsDAO.getRow(SupplierContactsDTO.getPK(cn, contactNumber));
        if(c != null)
            contactsNumToContact.put(contactNumber, c);

        return contactsNumToContact.containsKey(contactNumber);
    }

    //adds new contact without inserting
    public void putNewContact(String contactNumber, String contactName, String contactEmail){
        if(doesContactExist(contactNumber))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " already exists.");

        Contact c = new Contact(contactEmail, contactName, contactNumber, cn);
        contactsNumToContact.put(contactNumber, c);
    }


    public Contact getContact(String contactNumber){
        if(!doesContactExist(contactNumber))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " does not exists.");

        return contactsNumToContact.get(contactNumber);
    }

    public void addContact(String contactNumber, String contactName, String contactEmail){
        if(doesContactExist(contactNumber))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " already exists.");

        Contact c = new Contact(contactEmail, contactName, contactNumber, cn);
        insertContact(c);
        contactsNumToContact.put(contactNumber, c);
    }

    public void removeContact(String contactNumber){
        if(!doesContactExist(contactNumber))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " does not exists.");

        if(contactsDAO.selectAllUnderCondition("cn = '" + cn + "'").size() <= 1)
            throw new IllegalStateException("Can't remove the only contact left");

        removeContactData(contactsNumToContact.remove(contactNumber));
    }

    public void editContactName(String contactNumber, String newContactName){
        Contact contact = getContact(contactNumber);
        contact.setName(newContactName);
        updateContactData(contact);
    }

    public void editContactEmail(String contactNumber, String newContactEmail){
        Contact contact = getContact(contactNumber);
        contact.setEmail(newContactEmail);
        updateContactData(contact);
    }

    //i added a few getter and setter for payment details
    public void editContactNum(String contactNumber,String newContactNum){
        if(!doesContactExist(contactNumber))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " does not exists.");

        if(doesContactExist(newContactNum))
            throw new IllegalStateException("A Contact with the number: " + contactNumber + " already exists.");

        String email = contactsNumToContact.get(contactNumber).getEmail();
        String name = contactsNumToContact.get(contactNumber).getName();

        Contact contact = getContact(contactNumber);
        removeContactData(contact);

        Contact newContact = new Contact(email, name, newContactNum, cn);
        insertContact(newContact);

        contactsNumToContact.remove(contactNumber);
        contactsNumToContact.put(newContactNum, newContact);
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        if(!paymentFrequencies.contains(paymentFrequency))
            throw new IllegalArgumentException("invalid payment frequency. payment frequency can only be " +
                    "'immediate', 'EOM', or 'EO2M'.");

        this.paymentFrequency = paymentFrequency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        if(!paymentMethods.contains(paymentMethod))
            throw new IllegalArgumentException("invalid payment method. payment method can only be " +
                    "'credit', 'cash', or 'transferOfFunds'.");

        this.paymentMethod = paymentMethod;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
        for(Contact c : getContactList()){
            c.setCn(cn);
            updateContactData(c);
        }
    }

        public void setAddress(String address){
            this.address = address;
        }


    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public void setSupplierName(String newSupplierName){
        this.supplierName = newSupplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public LinkedList<Contact> getContactList(){
        loadAllContacts();
        return new LinkedList<>(contactsNumToContact.values());
    }

    
    //dal
    private void insertContact(Contact c) {
        contactsDAO.insert(c);
    }

    public void insertData() {
        insertContacts();
    }

    private void insertContacts(){
        for(Contact c : contactsNumToContact.values())
            insertContact(c);
    }

    public void removeData() {
        removeContactsData();
    }

    private void removeContactsData(){
        for(Contact c : getContactList()){
            removeContactData(c);
        }
    }

    private void removeContactData(Contact c) {
        contactsDAO.deleteRow(c);

    }

    private void updateContactData(Contact c){
        contactsDAO.update(c);
    }

    private void loadAllContacts(){
        for(Contact c : contactsDAO.selectAllUnderConditionToBusiness("cn = " + cn)){
            if(!contactsNumToContact.containsKey(c.getPhoneNumber()))
            contactsNumToContact.put(c.getPhoneNumber(), c);
        }
    }

    //////////////////////////////

    public String getAddress() {
        return address;
    }
}
