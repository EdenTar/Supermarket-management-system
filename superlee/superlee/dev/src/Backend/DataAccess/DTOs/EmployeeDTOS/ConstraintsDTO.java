package Backend.DataAccess.DTOs.EmployeeDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class ConstraintsDTO extends DTO<PK> {

    private long employeeId;
    private String date;
    private String shiftTime;

    public ConstraintsDTO(long employeeId, String date, String shiftTime) {
        super(new PK(getFields(), employeeId, date, shiftTime));
        this.employeeId = employeeId;
        this.date = date;
        this.shiftTime = shiftTime;

    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"employeeId", "date", "shiftTime"}, ConstraintsDTO.class);
    }

    public static PK getPK(long employeeId, String date, String shiftTime) {
        return new PK(getFields(), employeeId, date, shiftTime);
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{employeeId, date, shiftTime};
    }
}



