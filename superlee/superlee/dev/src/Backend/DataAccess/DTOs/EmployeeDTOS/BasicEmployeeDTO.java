package Backend.DataAccess.DTOs.EmployeeDTOS;


public class BasicEmployeeDTO {

    private long employeeId;
    private String job;


    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }


    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}



