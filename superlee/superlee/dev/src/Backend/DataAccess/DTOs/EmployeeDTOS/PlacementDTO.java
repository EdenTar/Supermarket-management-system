package Backend.DataAccess.DTOs.EmployeeDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class PlacementDTO extends DTO<PK> {

    private String date;
    private String shiftTime;
    private final String branch;
    private long employeeId;
    private final String job;

    public PlacementDTO(String date, String shiftTime, String branch, long employeeId, String job) {
        super(new PK(getFields(), date, shiftTime, branch, employeeId));
        this.date = date;
        this.shiftTime = shiftTime;
        this.branch = branch;
        this.employeeId = employeeId;
        this.job = job;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"date", "shiftTime", "branch", "employeeId"}, PlacementDTO.class);
    }

    public static PK getPK(String date, String shiftTime, String branch, long employeeId) {
        return new PK(getFields(), date, shiftTime, branch, employeeId);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJob() {
        return job;
    }


    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }


    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{date, shiftTime, branch, employeeId, job};
    }

    public String getBranch() {
        return branch;
    }
}



