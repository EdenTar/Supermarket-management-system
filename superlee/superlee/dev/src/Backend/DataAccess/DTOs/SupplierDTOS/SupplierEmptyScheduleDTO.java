package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class SupplierEmptyScheduleDTO extends DTO<PK> {

    private final String cn;

    public SupplierEmptyScheduleDTO(String cn) {
        super(new PK(getFields(), cn));
        this.cn = cn;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn"}, SupplierEmptyScheduleDTO.class);
    }

    public static PK getPK(String cn) {
        return new PK(getFields(), cn);
    }

    public String getCn() {
        return cn;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn};
    }
}



