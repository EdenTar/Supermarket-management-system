package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.JobsDTO;
import Backend.Logic.LogicObjects.Jobs.Job;

import java.util.List;


public class JobDAO extends DAO<PK, JobsDTO, Job>{
    public JobDAO() {
        super(JobsDTO.class,IM.getInstance().getIdentityMap(Job.class));
    }

    @Override
    protected Job convertDtoToBusiness(JobsDTO dto) {
        return new Job(dto.getJob());
    }

    @Override
    protected JobsDTO convertBusinessToDto(Job business) {
        return new JobsDTO(business.getJobName());
    }

    @Override
    protected JobsDTO createDTO(List<Object> listFields) {
        return new JobsDTO((String) listFields.get(0));
    }
}



