package Backend.DataAccess.DAOs.EmployeeDAOS;
import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.ConstraintsDTO;
import Backend.Logic.LogicObjects.Employee.Constraint;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConstrainsDAO extends DAO<PK, ConstraintsDTO, Constraint>{
    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

    public ConstrainsDAO() {
        super(ConstraintsDTO.class,IM.getInstance().getIdentityMap(Constraint.class));
    }

    @Override
    protected Constraint convertDtoToBusiness(ConstraintsDTO dto)  {
        try {
            ShiftTime shiftTime;
            if(dto.getShiftTime().equals("Morning"))
                shiftTime=ShiftTime.Morning;
            else
                shiftTime=ShiftTime.Evening;
            return new Constraint((int)dto.getEmployeeId(),formatter.parse(dto.getDate()),shiftTime);
        }
        catch (Exception exception){
            return null;
        }
    }

    @Override

    protected ConstraintsDTO convertBusinessToDto(Constraint business) {
        return new ConstraintsDTO(business.getEmployeeId(),formatter.format(business.getDate()), business.getShiftTime().toString());
    }

    @Override
    protected ConstraintsDTO createDTO(List<Object> listFields) {
        return new ConstraintsDTO((Integer) listFields.get(0), (String) listFields.get(1),(String) listFields.get(2));
    }
    public List<Constraint> getRowsFromDB_(String condition1, String condition2){
        return getRowsFromDB(condition1+" AND "+condition2);
    }
}



