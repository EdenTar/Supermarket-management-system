package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.BankAccountDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.ConstraintsDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmploymentConditionsDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.JobsDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.PlacementDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.Controllers.TransportEmployee.EmployeeController;
import Backend.Logic.Controllers.TransportEmployee.ShiftController;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Jobs.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class EmployeeDAO extends DAO<PK, EmployeeDTO, Employee>{

    private final BankAccountDAO bankAccountDAO;
    private final EmploymentConditionsDAO employmentConditionsDAO;
    private final ConstrainsDAO constrainsDAO;
    private final PlacementDAO placementDAO;

    public EmployeeDAO() {
        super(EmployeeDTO.class,IM.getInstance().getIdentityMap(Employee.class));
        this.bankAccountDAO = new BankAccountDAO();
        this.employmentConditionsDAO = new EmploymentConditionsDAO();
        placementDAO = new PlacementDAO();
        constrainsDAO = new ConstrainsDAO();
    }

    public void insert(Employee businessObject) {
        super.insert(businessObject);
        bankAccountDAO.insert(businessObject.getBankAccount());
        employmentConditionsDAO.insert(businessObject.getEmploymentConditions());
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected Employee convertDtoToBusiness(EmployeeDTO dto) {
        BankAccount bankAccount = this.bankAccountDAO.getRow(BankAccountDTO.getPK(dto.getId()));
        EmploymentConditions employmentConditions = this.employmentConditionsDAO.getRow(EmploymentConditionsDTO.getPK(dto.getId()));
        JobDAO jobDAO = new JobDAO();
        Job job = jobDAO.getRow(JobsDTO.getPK(dto.getJob()));
        try {
            switch (job.getJobName()) {
                case "hr manager":
                    return new HrManager((int) dto.getId(), getSimpleDateFormatNonHour().parse(dto.getStartingDate()), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(), dto.getIsShiftManager() == 1,
                            bankAccount, employmentConditions, new ShiftController(), new EmployeeController());
                case "stock keeper":
                    return new StockKeeper((int) dto.getId(), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(), getSimpleDateFormatNonHour().parse(dto.getStartingDate()),dto.getIsShiftManager() == 1,
                            bankAccount, employmentConditions, dto.getBranch());
                case "store manager":
                    return new StoreManager((int) dto.getId(), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(), getSimpleDateFormatNonHour().parse(dto.getStartingDate()),dto.getIsShiftManager() == 1,
                            bankAccount, employmentConditions, dto.getBranch());
                case "supplier manager":
                    return new SupplierManager((int) dto.getId(), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(), getSimpleDateFormatNonHour().parse(dto.getStartingDate()),dto.getIsShiftManager() == 1,
                            bankAccount, employmentConditions, dto.getBranch());
                case "driver":
                case "transport manager":
                    return new Employee((int) dto.getId(), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(),
                            getSimpleDateFormatNonHour().parse(dto.getStartingDate()),
                            job, dto.getIsShiftManager() == 1,
                            bankAccount, employmentConditions, dto.getBranch());

                default:
                    return new BasicEmployee((int) dto.getId(), dto.getFirstName(),
                            dto.getLastName(), dto.getPassword(), job, getSimpleDateFormatNonHour().parse(dto.getStartingDate()),
                            dto.getIsShiftManager() == 1, bankAccount, employmentConditions, dto.getBranch());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected EmployeeDTO convertBusinessToDto(Employee business) {
        /*String branch;
        if (business instanceof HrManager || business instanceof TransportManager || business instanceof Driver)
            branch = "general";
        else {
            branch = business.getBranchAddress();
        }*/
        return new EmployeeDTO(business.getEmployeeId(),
                business.getEmployeeName(), business.getEmployeeLastName(),
                business.getPassword(), convertToFormat(business.getStartingDate()), business.isShiftManager() ? 1 : 0, business.getJob().getJobName(), business.getBranchAddress());

    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected EmployeeDTO createDTO(List<Object> listFields) {
        return new EmployeeDTO((Integer) listFields.get(0), (String) listFields.get(1),
                (String) listFields.get(2), (String) listFields.get(3),
                (String) listFields.get(4), (Integer) listFields.get(5),
                (String) listFields.get(6), (String) listFields.get(7));
    }

    public void deleteRow(Employee employee) {
        super.deleteRow(employee);
        bankAccountDAO.deleteRow(employee.getBankAccount());
        employmentConditionsDAO.deleteRow(employee.getEmploymentConditions());
        List<ConstraintsDTO> c = constrainsDAO.selectAllUnderCondition("employeeId = " + employee.getEmployeeId());
        c.forEach(constrains -> constrainsDAO.deleteRow(constrainsDAO.convertDtoToBusiness(constrains)));
        List<PlacementDTO> p = placementDAO.selectAllUnderCondition("employeeId = " + employee.getEmployeeId());
        p.forEach(placementDTO -> placementDAO.deleteRow(placementDAO.convertDtoToBusiness(placementDTO)));
    }

    private String convertToFormat(Date startingDate) {
        if(startingDate.getMonth()+1<10)
            return "" + (startingDate.getYear() + 1900) + "-0" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
        else
            return "" + (startingDate.getYear() + 1900) + "-" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
    }

    public static void main(String[] args) {
        // EmployeeDAO employeeDao = new EmployeeDAO();
        //  OrderManDAO orderManDAO = new OrderManDAO(employeeDao);
        //employeeDao.insert(6,"test","test-test","1456","05/05/2022",0);
    /*    OrderMan orderMan = new OrderMan(123, "shauns", "ahua",
                , "shuster");*/
        //employeeDao.insert(orderMan);
        //orderManDAO.insert(orderMan);
        //System.out.println(orderManDAO.getRow(new OrderManPrimaryKey(Integer.parseInt(orderMan.getEmployeeId()))).showDetails());
        //employeeDao.update("firstName = 'haim',lastName = 'moshe'","id = 2");
        //employeeDao.deleteRow("id = 5");
   /*     try {
            //Field[] fields = {Employee.class.getDeclaredField("id"), Employee.class.getDeclaredField("lastName")};
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } */
        // employeeDao.deleteRow("id = 5");
        // employeeDao.insert(5,"test","test-test","1456","05/05/2022",0);
        // employeeDao.selectAll().forEach(System.out::println);
       /* List<Pair<String,String>> pairList=new LinkedList<>();
        pairList.add(new Pair<>("id","5"));
        employeeDao.selectRow(pairList).update("firstName = 'shaun',lastName = 'shuster'");
        System.out.println(employeeDao.selectRow(pairList));*/

    }


}



