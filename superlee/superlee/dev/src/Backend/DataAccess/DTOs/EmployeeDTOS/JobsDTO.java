package Backend.DataAccess.DTOs.EmployeeDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class JobsDTO extends DTO<PK> {

    private final String job;

    public JobsDTO(String job) {
        super(new PK(getFields(), job));
        this.job = job;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"job"}, JobsDTO.class);
    }

    public static PK getPK(String job) {
        return new PK(getFields(), job);
    }

    public String getJob() {
        return job;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{job};
    }
}



