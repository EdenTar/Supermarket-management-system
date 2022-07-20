package Backend.DataAccess.DTOs.EmployeeDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class BankAccountDTO extends DTO<PK> {

    private long employeeId;
    private long bankNumber;
    private long bankBranch;
    private long accountNumber;

    /**
     * @return
     */
    @Override
    public Object[] getValues() {
        return new Object[]{employeeId, bankNumber, bankBranch, accountNumber};
    }

    public BankAccountDTO(long employeeId, long bankNumber, long bankBranch, long accountNumber) {
        super(new PK(getFields(), employeeId));
        this.employeeId = employeeId;
        this.bankNumber = bankNumber;
        this.bankBranch = bankBranch;
        this.accountNumber = accountNumber;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"employeeId"}, BankAccountDTO.class);
    }

    public static PK getPK(long employeeId) {
        return new PK(getFields(), employeeId);
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }


    public long getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(long bankNumber) {
        this.bankNumber = bankNumber;
    }


    public long getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(long bankBranch) {
        this.bankBranch = bankBranch;
    }


    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

}



