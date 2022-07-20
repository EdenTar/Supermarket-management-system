package Backend.Logic.LogicObjects.Employee;
import java.util.Date;

public class Constraint  {
    private Date date;
    private ShiftTime shiftTime;
    private int employeeId;

    public Constraint(int employeeId, Date date, ShiftTime shiftTime) {
        this.date = date;
        this.shiftTime = shiftTime;
        this.employeeId=employeeId;
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
    public  int getEmployeeId(){return employeeId;}
}
