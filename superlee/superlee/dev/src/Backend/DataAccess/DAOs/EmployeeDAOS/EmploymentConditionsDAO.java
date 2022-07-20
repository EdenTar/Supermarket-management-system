package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmploymentConditionsDTO;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;

import java.util.List;

public class EmploymentConditionsDAO extends DAO<PK, EmploymentConditionsDTO, EmploymentConditions>{
    public EmploymentConditionsDAO() {
        super(EmploymentConditionsDTO.class,IM.getInstance().getIdentityMap(EmploymentConditions.class));
    }

    /* @param dto
     * @return
             */
    @Override
    protected EmploymentConditions convertDtoToBusiness(EmploymentConditionsDTO dto) {
        return new EmploymentConditions((int) dto.getEmployeeId(),dto.getSalary(),dto.getSocialBenefits());
    }

    /* @param business
     * @return
             */
    @Override
    protected EmploymentConditionsDTO convertBusinessToDto(EmploymentConditions business) {
        return new EmploymentConditionsDTO(business.getSalary(), business.getEmployeeId(), business.getSocialBenefits());
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected EmploymentConditionsDTO createDTO(List<Object> listFields) {
        return new EmploymentConditionsDTO((Double) listFields.get(0), (Integer) listFields.get(1), (String) listFields.get(2));

    }
}


