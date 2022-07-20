package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.BankAccountDTO;
import Backend.Logic.LogicObjects.Employee.BankAccount;

import java.util.List;

public class BankAccountDAO extends DAO<PK, BankAccountDTO, BankAccount>{
    public BankAccountDAO() {
        super(BankAccountDTO.class,IM.getInstance().getIdentityMap(BankAccount.class));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected BankAccount convertDtoToBusiness(BankAccountDTO dto) {
        return new BankAccount((int) dto.getEmployeeId(),(int) dto.getBankNumber(),(int) dto.getAccountNumber(),(int) dto.getBankBranch());
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected BankAccountDTO convertBusinessToDto(BankAccount business) {
        return new BankAccountDTO(business.getEmployeeId(), business.getBankNumber(),business.getBankBranch(),business.getAccountNumber());
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected BankAccountDTO createDTO(List<Object> listFields) {
        return new BankAccountDTO((Integer) listFields.get(0), (Integer) listFields.get(1),
                (Integer) listFields.get(2), (Integer) listFields.get(3));
    }
}



