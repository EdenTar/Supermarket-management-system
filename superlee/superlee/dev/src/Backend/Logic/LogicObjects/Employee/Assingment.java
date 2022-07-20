package Backend.Logic.LogicObjects.Employee;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Assingment {

    private Date date;
    private ShiftTime shiftTime;
    private String branch;
    private String job;
    private long capacity;
    private long quantity;

    public Assingment(Date date, ShiftTime shiftTime, String branch, String job,long capacity, long quantity) {
        this.date = date;
        this.shiftTime = shiftTime;
        this.branch = branch;
        this.job = job;
        this.quantity = quantity;
        this.capacity=capacity;
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

    public void setShiftTime(ShiftTime shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    public long getCapacity(){return capacity;}

    public String getStrDate(){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
