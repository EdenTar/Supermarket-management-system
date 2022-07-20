package Backend.ServiceLayer.ServiceObjects.Supplier;

import Backend.Logic.LogicObjects.Supplier.Contact;

public class ServiceContact {
    private String name;
    private String phone;
    private String email;

    public ServiceContact(String name,String phone,String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public ServiceContact(Contact contact){
        this.name = contact.getName();
        this.phone = contact.getPhoneNumber();
        this.email = contact.getEmail();
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "{ " +
                "name = '" + name + "\n" +
                ", phone = '" + phone + "\n" +
                ", email = '" + email + "\n" +
                " }";
    }
}
