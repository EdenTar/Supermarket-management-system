package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.BankAccountDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmploymentConditionsDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DAOs.EmployeeDAOS.BankAccountDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.EmployeeDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.EmploymentConditionsDAO;
import Backend.DataAccess.DTOs.TransportDTOS.TransportManagerDTO;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Jobs.TransportManager;

import java.util.List;

public class TransportManagerDAO extends DAO<PK, TransportManagerDTO, TransportManager>{
    private final EmployeeDAO employeeDAO;
    private final BankAccountDAO bankAccountDAO;
    private final EmploymentConditionsDAO employmentConditionsDAO;


    public TransportManagerDAO() {
        super(TransportManagerDTO.class,IM.getInstance().getIdentityMap(TransportManager.class));
        this.employeeDAO = new EmployeeDAO();
        this.bankAccountDAO = new BankAccountDAO();
        this.employmentConditionsDAO = new EmploymentConditionsDAO();

    }

    @Override
    public void deleteRow(TransportManager businessObject) {
        super.deleteRow(businessObject);
        employeeDAO.deleteRow(businessObject);
    }

    @Override
    public void insert(TransportManager businessObject) {
        super.insert(businessObject);
        employeeDAO.insert(businessObject);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected TransportManager convertDtoToBusiness(TransportManagerDTO dto) {
        Employee transportManager = employeeDAO.getRow(EmployeeDTO.getPK(dto.getEmployeeId()));
        BankAccount bankAccount = bankAccountDAO.getRow(BankAccountDTO.getPK(dto.getEmployeeId()));
        EmploymentConditions employmentConditions = this.employmentConditionsDAO.getRow(EmploymentConditionsDTO.getPK(dto.getEmployeeId()));
        return new TransportManager(
                transportManager.getEmployeeId(),
                transportManager.getEmployeeName(),
                transportManager.getEmployeeLastName(),
                transportManager.getPassword(),
                transportManager.getStartingDate(),
                null, null,
                transportManager.getIsShiftManager(), bankAccount, employmentConditions);
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected TransportManagerDTO convertBusinessToDto(TransportManager business) {
        return new TransportManagerDTO(business.getEmployeeId());
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected TransportManagerDTO createDTO(List<Object> listFields) {
        return new TransportManagerDTO((Integer) listFields.get(0));
    }

}



