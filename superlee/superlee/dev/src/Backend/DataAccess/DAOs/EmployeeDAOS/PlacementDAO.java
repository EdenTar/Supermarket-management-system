package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.EmployeeDTOS.PlacementDTO;
import Backend.Logic.LogicObjects.Employee.Placement;
import Backend.Logic.Points.Branch;
import Backend.Logic.LogicObjects.Employee.ShiftTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlacementDAO extends DAO<PK, PlacementDTO, Placement>{
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public PlacementDAO() {
        super(PlacementDTO.class,IM.getInstance().getIdentityMap(Placement.class));
    }

    @Override
    protected Placement convertDtoToBusiness(PlacementDTO dto) {
        try {
            ShiftTime shiftTime;
            if (dto.getShiftTime().equals("Morning"))
                shiftTime = ShiftTime.Morning;
            else
                shiftTime = ShiftTime.Evening;

            return new Placement(formatter.parse(dto.getDate()), shiftTime, dto.getBranch(), (int) dto.getEmployeeId(), dto.getJob());
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    protected PlacementDTO convertBusinessToDto(Placement business) {
        return new PlacementDTO(convertBusinessDateToDB(business.getStrDate()), business.getShiftTime().toString(), business.getBranch(), business.getEmployeeId(), business.getJob());
    }

    @Override
    protected PlacementDTO createDTO(List<Object> listFields) {
        return new PlacementDTO((String) listFields.get(0), (String) listFields.get(1), (String) listFields.get(2), (Integer) listFields.get(3), (String) listFields.get(4));
    }
    public List<Placement> getRowsFromDB(String con1, String con2) {
        return getRowsFromDB(con1 + " AND " + con2 );
    }
    public List<Placement> getRowsFromDB_(String con1, String con2, String con3) {
        return getRowsFromDB(con1 + " AND " + con2 + " AND " + con3);
    }
    public List<Placement> getPlace(Branch branch, Date date, String ShiftTime)
    {
        String s=getSimpleDateFormatNonHour().format(date);
        return getRowsFromDB(" branch = '" + branch.getAddress() + "' AND date = '" + s + "' AND shiftTime = '"+ ShiftTime + "'");

    }

}


