package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class SupplierConsistentScheduleDTO extends DTO<PK> {

    private final String cn;
    private final long day;

    public SupplierConsistentScheduleDTO(String cn, long day) {
        super(new PK(getFields(), cn, day));
        this.cn = cn;
        this.day = day;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn", "day"}, SupplierConsistentScheduleDTO.class);
    }

    public static PK getPK(String cn, long day) {
        return new PK(getFields(), cn, day);
    }

    public String getCn() {
        return cn;
    }

    public long getDay() {
        return day;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, day};
    }
}



