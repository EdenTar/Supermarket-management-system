package Backend.Logic.LogicObjects.Supplier;

import java.util.regex.Pattern;

public class Contact {
    // used for dal transformation
    private String cn;
    //
    private String name;
    private String email;
    private String phoneNumber;
    private final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private final String phoneRegex = "^[\\+*]?\\d+(?:[-\\/\\s.]|\\d)+";

    public Contact(String email,String name,String phoneNumber,String cn){
        setCn(cn);
        setEmail(email);
        setName(name);
        setPhoneNumber(phoneNumber);
    }

    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private boolean checkEmail(String email){
        return patternMatches(email, emailRegex);
    }

    private boolean checkPhone(String phone){
        return patternMatches(phone, phoneRegex);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        if(!checkEmail(email))
            throw new IllegalArgumentException("The given email is not valid");
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(!checkPhone(phoneNumber))
            throw new IllegalArgumentException("The given phone number is not valid");
        this.phoneNumber = phoneNumber;
    }

    public void setCn(String newCn){
        this.cn = newCn;
    }

    public String getCn() {
        return cn;
    }
}
