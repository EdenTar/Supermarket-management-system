package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.EmptySchedule;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierEmptyScheduleDTO;

import java.util.List;

public class SupplierEmptyScheduleDAO extends DAO<PK, SupplierEmptyScheduleDTO, EmptySchedule>{

    public SupplierEmptyScheduleDAO() {
        super(SupplierEmptyScheduleDTO.class,IM.getInstance().getIdentityMap(EmptySchedule.class));
    }

    @Override
    protected EmptySchedule convertDtoToBusiness(SupplierEmptyScheduleDTO dto) {
        return new EmptySchedule(dto.getCn());
    }

    @Override
    protected SupplierEmptyScheduleDTO convertBusinessToDto(EmptySchedule business) {
        return new SupplierEmptyScheduleDTO(business.getCn());
    }

    @Override
    protected SupplierEmptyScheduleDTO createDTO(List<Object> listFields) {
        //for dal
        return new SupplierEmptyScheduleDTO((String) listFields.get(0));
    }
}



