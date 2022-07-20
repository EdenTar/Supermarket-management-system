package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class SupplierCardDTO extends DTO<PK> {
    private final String cn;
    private final String supplierName;
    private final String bankAccountNum;
    private final String paymentMethod;
    private final String paymentFrequency;
    private final String address;


    public SupplierCardDTO(String cn, String supplierName, String bankAccountNum, String paymentMethod, String paymentFrequency,String address) {
        super(new PK(getFields(), cn));
        this.cn = cn;
        this.supplierName = supplierName;
        this.bankAccountNum = bankAccountNum;
        this.paymentMethod = paymentMethod;
        this.paymentFrequency = paymentFrequency;
        this.address = address;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn"}, SupplierCardDTO.class);
    }

    public static PK getPK(String cn) {
        return new PK(getFields(), cn);
    }

    public String getCn() {
        return cn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, supplierName, bankAccountNum, paymentMethod, paymentFrequency, address};
    }
}



