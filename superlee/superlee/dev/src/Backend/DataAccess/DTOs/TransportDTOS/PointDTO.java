package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class PointDTO extends DTO<PK> {

    private final String address;
    private final String phone;
    private final String contactName;
    private final String zone;
    private final String tag;

    public PointDTO(String address, String phone, String contactName, String zone, String tag) {
        super(new PK(getFields(), address));
        this.address = address;
        this.phone = phone;
        this.contactName = contactName;
        this.zone = zone;
        this.tag = tag;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"address"}, PointDTO.class);
    }

    public static PK getPK(String address) {
        return new PK(getFields(), address);
    }


    public String getAddress() {
        return address;
    }


    public String getPhone() {
        return phone;
    }

    public String getContactName() {
        return contactName;
    }

    public String getZone() {
        return zone;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{address, phone, contactName, zone, tag};
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Point{" +
                "address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", contactName='" + contactName + '\'' +
                ", zone='" + zone + '\'' +
                ", tag='" + tag + '\'' +
                "}\n";
    }
}



