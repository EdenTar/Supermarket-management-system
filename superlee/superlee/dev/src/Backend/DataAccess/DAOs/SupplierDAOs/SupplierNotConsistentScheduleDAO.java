package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.NotConsistentSupplierSchedule;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierNotConsistentScheduleDTO;

import java.util.List;

public class SupplierNotConsistentScheduleDAO extends DAO<PK, SupplierNotConsistentScheduleDTO, NotConsistentSupplierSchedule>{

    public SupplierNotConsistentScheduleDAO() {
        super(SupplierNotConsistentScheduleDTO.class,IM.getInstance().getIdentityMap(NotConsistentSupplierSchedule.class));
    }

    @Override
    protected NotConsistentSupplierSchedule convertDtoToBusiness(SupplierNotConsistentScheduleDTO dto) {
        return new NotConsistentSupplierSchedule(dto.getCn(),(int) dto.getDaysTillNextShipment());
    }

    @Override
    protected SupplierNotConsistentScheduleDTO convertBusinessToDto(NotConsistentSupplierSchedule business) {
        return new SupplierNotConsistentScheduleDTO(business.getCn(),business.getShipmentTime());
    }

    @Override
    protected SupplierNotConsistentScheduleDTO createDTO(List<Object> listFields) {
        return new SupplierNotConsistentScheduleDTO((String) listFields.get(0),(int) listFields.get(1));
    }
}



