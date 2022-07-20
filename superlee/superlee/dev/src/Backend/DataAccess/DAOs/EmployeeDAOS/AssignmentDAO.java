package Backend.DataAccess.DAOs.EmployeeDAOS;
import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.AssignmentDTO;
import Backend.Logic.LogicObjects.Employee.Assingment;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import java.text.SimpleDateFormat;
import java.util.List;

public class AssignmentDAO extends DAO<PK, AssignmentDTO,Assingment>{
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public AssignmentDAO() {
        super(AssignmentDTO.class,IM.getInstance().getIdentityMap(Assingment.class));
    }

    @Override
    protected Assingment convertDtoToBusiness(AssignmentDTO dto) {
        try {
            ShiftTime shiftTime;
            if(dto.getShiftTime().equals("Morning"))
                shiftTime=ShiftTime.Morning;
            else
                shiftTime=ShiftTime.Evening;
            return new Assingment(formatter.parse(dto.getDate()),shiftTime,dto.getBranch(),dto.getJob(),dto.getCapacity(),dto.getQuantity());
        }
        catch (Exception exception){
            return null;
        }
    }

    @Override
    protected AssignmentDTO convertBusinessToDto(Assingment business) {
        return new AssignmentDTO(business.getStrDate(),business.getShiftTime().toString(),business.getBranch(),business.getJob(),business.getCapacity(),business.getQuantity());
    }

    @Override
    protected AssignmentDTO createDTO(List<Object> listFields) {
        return new AssignmentDTO((String)listFields.get(0),(String)listFields.get(1),(String)listFields.get(2),(String)listFields.get(3),(Integer)listFields.get(4),(Integer)listFields.get(5));

    }
}



