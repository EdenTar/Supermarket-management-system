package Backend.DataAccess.DTOs.EmployeeDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class ShiftDTO extends DTO<PK> {
    private String date;
    private String shiftTime;
    private String branch;
    private final long shiftManager;


    public ShiftDTO(String date, String shiftTime, String branch, int shiftManagerId) {
        super(new PK(getFields(), date, shiftTime, branch));
        this.date = date;
        this.shiftTime = shiftTime;
        this.branch = branch;
        this.shiftManager = shiftManagerId;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"date", "shiftTime", "branch"}, ShiftDTO.class);
    }

    public static PK getPK(String date, String shiftTime, String branch) {
        return new PK(getFields(), date, shiftTime, branch);
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{date, shiftTime, branch, shiftManager};
    }

    public long getShiftManagerId() {
        return shiftManager;
    }
}



