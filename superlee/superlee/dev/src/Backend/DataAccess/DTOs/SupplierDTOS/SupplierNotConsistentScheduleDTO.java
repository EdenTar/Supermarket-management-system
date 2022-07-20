package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class SupplierNotConsistentScheduleDTO extends DTO<PK> {

    private final String cn;
    private final long daysTillNextShipment;

    public SupplierNotConsistentScheduleDTO(String cn, long daysTillNextShipment) {
        super(new PK(getFields(), cn));
        this.cn = cn;
        this.daysTillNextShipment = daysTillNextShipment;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn"}, SupplierNotConsistentScheduleDTO.class);
    }

    public static PK getPK(String cn) {
        return new PK(getFields(), cn);
    }

    public String getCn() {
        return cn;
    }


    public long getDaysTillNextShipment() {
        return daysTillNextShipment;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, daysTillNextShipment};
    }
}



