package Backend.DataAccess.DTOs.EmployeeDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class AssignmentDTO extends DTO<PK> {
    private final String date;
    private final String shiftTime;
    private final String branch;
    private final String job;
    private final long capacity;
    private final long quantity;

    @Override
    public Object[] getValues() {
        return new Object[]{date, shiftTime, branch, job, capacity, quantity};
    }

    public AssignmentDTO(String date, String shiftTime, String branch, String job, long capacity, long quantity) {
        super(new PK(getFields(), date, shiftTime, branch, job));
        this.date = date;
        this.shiftTime = shiftTime;
        this.branch = branch;
        this.job = job;
        this.capacity = capacity;
        this.quantity = quantity;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"date", "shiftTime", "branch", "job"}, AssignmentDTO.class);
    }

    public static PK getPK(String date, String shiftTime, String branch, String job) {
        return new PK(getFields(), date, shiftTime, branch, job);
    }

    public long getCapacity() {
        return capacity;
    }

    public String getDate() {
        return date;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public String getBranch() {
        return branch;
    }

    public String getJob() {
        return job;
    }

    public long getQuantity() {
        return quantity;
    }

}



