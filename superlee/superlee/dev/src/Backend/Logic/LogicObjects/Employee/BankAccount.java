package Backend.Logic.LogicObjects.Employee;

public class BankAccount {
    private final int employeeId;
    private int bankNumber;
    private int accountNumber;
    private int bankBranch;

    public BankAccount(int employeeId, int bankNumber,int accountNumber,int bankBranch){
        this.bankNumber=bankNumber;
        this.accountNumber=accountNumber;
        this.bankBranch=bankBranch;
        this.employeeId = employeeId;
    }

    public void setBankNumber(int bankNumber){
        this.bankNumber=bankNumber;
    }

    public void setAccountNumber(int accountNumber){
        this.accountNumber=accountNumber;
    }

    public void setBankBranch(int bankBranch){
        this.bankBranch=bankBranch;
    }
    public String toString(){
        return "  Bank number: "+ bankNumber+"  Bank branch: " +bankBranch+"  Account number: " +accountNumber;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getBankBranch() {
        return bankBranch;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
