package Backend.DataAccess.DTOs.EmployeeDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class EmployeeDTO extends DTO<PK> {

    private final long id;
    private String firstName;
    private String lastName;
    private String password;
    private final String startingDate;
    private final long isShiftManager;
    private final String job;
    private final String branch;


    public EmployeeDTO(long employeeId, String firstName, String lastName,
                       String password, String startingDate, long isShiftManager, String job, String branch) {
        super(new PK(getFields(), employeeId));
        this.id = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.startingDate = startingDate;
        this.isShiftManager = isShiftManager;
        this.job = job;
        this.branch = branch;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, EmployeeDTO.class);
    }

    public static PK getPK(long id) {
        return new PK(getFields(), id);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getStartingDate() {
        return startingDate;
    }


    public long getIsShiftManager() {
        return isShiftManager;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", startingDate='" + startingDate + '\'' +
                ", isShiftManager=" + isShiftManager +
                ", job='" + job + '\'' +
                '}';
    }

    /**
     * @return
     */
    @Override
    public Object[] getValues() {
        return new Object[]{id, firstName, lastName, password, startingDate, isShiftManager, job, branch};
    }
}



