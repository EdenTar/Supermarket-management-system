package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;

public class DriverDTO extends DTO<PK> {

    private final long employeeId;
    private final String license;
    private final String lastUpdateDistance;
    private final long distance;
    private final EmployeeDTO employeeDTO;

    public DriverDTO(long employeeId, String license, String lastUpdateDistance, long distance) {
        super(new PK(getFields(), employeeId));
        this.employeeId = employeeId;
        this.license = license;
        this.lastUpdateDistance = lastUpdateDistance;
        this.distance = distance;
        employeeDTO = null;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"employeeId"}, DriverDTO.class);
    }

    public static PK getPK(long employeeId) {
        return new PK(getFields(), employeeId);
    }

    public DriverDTO(long employeeId, String license, EmployeeDTO employeeDTO, String lastUpdateDistance, long distance) {
        super(new PK(getFields(), employeeId));
        this.employeeId = employeeId;
        this.license = license;
        this.employeeDTO = employeeDTO;
        this.lastUpdateDistance = lastUpdateDistance;
        this.distance = distance;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public String getLastUpdateDistance() {
        return lastUpdateDistance;
    }

    public String getLicense() {
        return license;
    }

    public String getFirstName() {
        return employeeDTO.getFirstName();
    }

    public String getLastName() {
        return employeeDTO.getLastName();
    }

    public String getPassword() {
        return employeeDTO.getPassword();
    }

    public String getStartingDate() {
        return employeeDTO.getStartingDate();
    }

    public long getIsShiftManager() {
        return employeeDTO.getIsShiftManager();
    }

    @Override
    public Object[] getValues() {
        return new Object[]{employeeId, license, lastUpdateDistance, distance};
    }

    public long getDistance() {
        return distance;
    }
}



