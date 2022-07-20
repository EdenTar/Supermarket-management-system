package Backend.DataAccess.DTOs.EmployeeDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class EmploymentConditionsDTO extends DTO<PK> {

    private final double salary;
    private final long employeeId;
    private final String socialBenefits;

    public EmploymentConditionsDTO(double salary, long employeeId, String socialBenefits) {
        super(new PK(getFields(), employeeId));
        this.salary = salary;
        this.employeeId = employeeId;
        this.socialBenefits = socialBenefits;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"employeeId"}, EmploymentConditionsDTO.class);
    }

    public static PK getPK(long employeeId) {
        return new PK(getFields(), employeeId);
    }

    public double getSalary() {
        return salary;
    }

    public long getEmployeeId() {
        return employeeId;
    }


    public String getSocialBenefits() {
        return socialBenefits;
    }

    /**
     * @return
     */
    @Override
    public Object[] getValues() {
        return new Object[]{salary, employeeId, socialBenefits};
    }
}



