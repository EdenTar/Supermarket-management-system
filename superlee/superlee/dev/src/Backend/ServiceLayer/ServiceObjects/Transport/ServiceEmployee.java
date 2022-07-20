package Backend.ServiceLayer.ServiceObjects.Transport;

public class ServiceEmployee {
    private final int employeeId;
    private final String employeeName;
    private final String employeeLastName;
    private final String password;

    @Override
    public String toString() {
        return "Employee:\n" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeLastName='" + employeeLastName + '\'' +
                '\n';
    }

    public ServiceEmployee(int employeeId, String employeeName, String employeeLastName, String password) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeLastName = employeeLastName;
        this.password = password;

    }


    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }


    public String getPassword() {
        return password;
    }

}
