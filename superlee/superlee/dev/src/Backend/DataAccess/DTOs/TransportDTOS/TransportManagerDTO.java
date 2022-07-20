package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class TransportManagerDTO extends DTO<PK> {

    private final long employeeId;

    public TransportManagerDTO(long employeeId) {
        super(new PK(getFields(), employeeId));
        this.employeeId = employeeId;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"employeeId"}, TransportManagerDTO.class);
    }

    public static PK getPK(long employeeId) {
        return new PK(getFields(), employeeId);
    }

    public long getEmployeeId() {
        return employeeId;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{employeeId};
    }
}



