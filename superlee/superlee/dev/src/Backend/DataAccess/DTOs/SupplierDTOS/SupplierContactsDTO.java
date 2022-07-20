package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class SupplierContactsDTO extends DTO<PK> {

    private final String cn;
    private final String contactPhoneNumber;
    private final String contactName;
    private final String contactEmail;

    public SupplierContactsDTO(String cn, String contactPhoneNumber, String contactName, String contactEmail) {
        super(new PK(getFields(), cn, contactPhoneNumber));
        this.cn = cn;
        this.contactPhoneNumber = contactPhoneNumber;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn", "contactPhoneNumber"}, SupplierContactsDTO.class);
    }

    public static PK getPK(String cn, String contactPhoneNumber) {
        return new PK(getFields(), cn, contactPhoneNumber);
    }

    public String getCn() {
        return cn;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, contactPhoneNumber, contactName, contactEmail};
    }
}



