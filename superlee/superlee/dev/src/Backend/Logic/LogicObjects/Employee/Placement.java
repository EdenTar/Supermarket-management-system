package Backend.Logic.LogicObjects.Employee;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Placement {
    private Date date;
    private ShiftTime shiftTime;
    private String branch;
    private int employeeId;
    private String job;


    public Placement(Date date, ShiftTime shiftTime, String branch, int employeeId,String job) {
        this.date = date;
        this.shiftTime = shiftTime;
        this.branch = branch;
        this.employeeId = employeeId;
        this.job=job;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ShiftTime getShiftTime() {
        return shiftTime;
    }

    public String getJob(){return job;}

    public void setShiftTime(ShiftTime shiftTime) {
        this.shiftTime = shiftTime;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStrDate(){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
       return formatter.format(getDate());
    }

}
