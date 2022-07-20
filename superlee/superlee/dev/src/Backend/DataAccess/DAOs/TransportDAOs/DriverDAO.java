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
import Backend.DataAccess.DTOs.TransportDTOS.DriverDTO;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.Vehicles.License;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class DriverDAO extends DAO<PK, DriverDTO, Driver>{

    private EmployeeDAO employeeDAO;
    private BankAccountDAO bankAccountDAO;
    private EmploymentConditionsDAO employmentConditionsDAO;
    public DriverDAO(){
        super(DriverDTO.class,IM.getInstance().getIdentityMap(Driver.class));
        this.employeeDAO=new EmployeeDAO();
        this.bankAccountDAO = new BankAccountDAO();
        this.employmentConditionsDAO = new EmploymentConditionsDAO();
    }
    @Override
    public void insert(Driver businessObject) {
        super.insert(businessObject);
        employeeDAO.insert(businessObject);
    }
    @Override
    public void deleteRow(Driver businessObject)
    {
        super.deleteRow(businessObject);
        employeeDAO.deleteRow(businessObject);
    }
    @Override
    protected Driver convertDtoToBusiness(DriverDTO dto) {
        Employee driver = employeeDAO.getRow(EmployeeDTO.getPK(dto.getEmployeeId()));
        BankAccount bankAccount = bankAccountDAO.getRow(BankAccountDTO.getPK(dto.getEmployeeId()));
        EmploymentConditions employmentConditions = employmentConditionsDAO.getRow(EmploymentConditionsDTO.getPK(dto.getEmployeeId()));
        Date date = null;
        try {
            date = getSimpleDateFormatNonHour().parse(dto.getLastUpdateDistance());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LocalDate localDate = LocalDate.of(date.getYear()+1900,date.getMonth()+1,date.getDate());
        return new Driver((int) dto.getEmployeeId(),driver.getEmployeeName(),
                driver.getEmployeeLastName(),driver.getPassword(),driver.getStartingDate(),null,driver.isShiftManager(),
                bankAccount,employmentConditions, License.valueOf(dto.getLicense()), localDate,(int)dto.getDistance());
    }

    @Override
    protected DriverDTO convertBusinessToDto(Driver business) {
        return new DriverDTO(
                            business.getEmployeeId(),
                            business.getDriverLicense().name(),
                            business.getLastUpdateDistance().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            business.getDistance());
    }

    @Override
    protected DriverDTO createDTO(List<Object> listFields) {
        return new DriverDTO(
                        (Integer) listFields.get(0),
                        (String) listFields.get(1),
                        (String) listFields.get(2),
                        (Integer)listFields.get(3));
    }
    /*public Backend.Logic.Jobs.Driver selectRow(String conditions) {
        *//*Employee employee=employeeDAO.getRowFromDB(conditions);
        Backend.Logic.Jobs.Driver driver=selectRow(conditions);
        driver.setEmployee(employee);
        return driver;*//*
        return null;
    }*/


}



