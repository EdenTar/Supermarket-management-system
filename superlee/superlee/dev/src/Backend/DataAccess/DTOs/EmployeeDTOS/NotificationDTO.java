package Backend.DataAccess.DTOs.EmployeeDTOS;

import Backend.DataAccess.DTOs.DTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Obj.Parser;

import java.lang.reflect.Field;

public class NotificationDTO extends DTO<PK> {
    String dateSet;

    public NotificationDTO(String dataSet) {
        super(new PK(getFields()));
        this.dateSet=dataSet;
    }

    @Override
    public Object[] getValues() {
        return new Object[0];
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{}, EmployeeDTO.class);
    }
    public static PK getPK(long serialNumber) {
        return new PK(getFields(), serialNumber);
    }

    public String getDateSet() {
        return dateSet;
    }

    public void setDateSet(String dateSet) {
        this.dateSet = dateSet;
    }
}
