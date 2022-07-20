package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.ShiftDTO;
import Backend.Logic.LogicObjects.Employee.Shift;
import Backend.Logic.LogicObjects.Employee.ShiftTime;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShiftDAO extends DAO<PK, ShiftDTO, Shift>{
    private EmployeeDAO employeeDAO;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ShiftDAO() {

        super(ShiftDTO.class,IM.getInstance().getIdentityMap(Shift.class));
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected Shift convertDtoToBusiness(ShiftDTO dto) {
        try {
            ShiftTime shiftTime;
            if (dto.getShiftTime().equals("Morning"))
                shiftTime = ShiftTime.Morning;
            else
                shiftTime = ShiftTime.Evening;
            return new Shift(formatter.parse(dto.getDate()), shiftTime, dto.getBranch(),
                    employeeDAO.getRow(EmployeeDTO.getPK(dto.getShiftManagerId()))
            );
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    protected ShiftDTO convertBusinessToDto(Shift business) {
        return new ShiftDTO(business.getStrDate(), business.getShiftTime().toString(), business.getBranch(), business.getShiftManager().getEmployeeId());
    }

    @Override
    protected ShiftDTO createDTO(List<Object> listFields) {
        return new ShiftDTO((String) listFields.get(0), (String) listFields.get(1), (String) listFields.get(2), (Integer) listFields.get(3));
    }
}



