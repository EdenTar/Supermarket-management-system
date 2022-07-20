package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class SupplierOrdersDTO extends DTO<PK> {

    private final long orderId;
    private final String cn;
    private final String supplierName;
    private final String fromAddress;
    private final String toAddress;
    private final String date;
    private final String contactPhoneNumber;
    private final long transportID;

    public SupplierOrdersDTO(long orderId, String cn, String supplierName, String fromAddress, String toAddress, String date, String contactPhoneNumber, long transportID) {
        super(new PK(getFields(), orderId));
        this.orderId = orderId;
        this.cn = cn;
        this.supplierName = supplierName;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.date = date;
        this.contactPhoneNumber = contactPhoneNumber;
        this.transportID = transportID;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"orderId"}, SupplierOrdersDTO.class);
    }

    public static PK getPK(long orderId) {
        return new PK(getFields(), orderId);
    }

    public long getOrderId() {
        return orderId;
    }

    public String getCn() {
        return cn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getDate() {
        return date;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{orderId, cn, supplierName, fromAddress, toAddress, date, contactPhoneNumber, transportID};
    }

    ////////////////////////////////////


    public long getTransportID() {
        return transportID;
    }
}



