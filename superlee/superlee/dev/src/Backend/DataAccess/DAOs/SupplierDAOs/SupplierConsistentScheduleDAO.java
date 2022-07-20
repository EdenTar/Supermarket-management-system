package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.ConsistentSupplierSchedule;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierConsistentScheduleDTO;

import java.util.List;

//NOTICE THIS CLASS ISNT COMPLETELY GENERIC
public class SupplierConsistentScheduleDAO extends DAO<PK, SupplierConsistentScheduleDTO, ConsistentSupplierSchedule>{

    public SupplierConsistentScheduleDAO() {
        super(SupplierConsistentScheduleDTO.class,IM.getInstance().getIdentityMap(ConsistentSupplierSchedule.class));
    }


    //get the schedule for a supplier
    public ConsistentSupplierSchedule getFullConsistentSupplierSchedule(String supplierCn){
        ConsistentSupplierSchedule c = new ConsistentSupplierSchedule(supplierCn);
        List<SupplierConsistentScheduleDTO> dtos = selectAllUnderCondition("cn = " + supplierCn);
        for(SupplierConsistentScheduleDTO dto : dtos){
            c.addSupplyingDay((int)dto.getDay());
        }
        return c;
    }

    @Deprecated
    @Override
    protected ConsistentSupplierSchedule convertDtoToBusiness(SupplierConsistentScheduleDTO dto) {
        ConsistentSupplierSchedule c = new ConsistentSupplierSchedule(dto.getCn());
        c.addSupplyingDay((int) dto.getDay());
        return c;
    }

    @Override
    protected SupplierConsistentScheduleDTO convertBusinessToDto(ConsistentSupplierSchedule business) {
        return new SupplierConsistentScheduleDTO(business.getCn(), business.getDaysList().get(0));
    }

    @Override
    protected SupplierConsistentScheduleDTO createDTO(List<Object> listFields) {
        return new SupplierConsistentScheduleDTO((String) listFields.get(0),(int) listFields.get(1));
    }

    public void insertConsistentSchedule(ConsistentSupplierSchedule schedule){
        for(int i = 0; i < schedule.getDaysList().size() ; i++){
            ConsistentSupplierSchedule schedule1 = new ConsistentSupplierSchedule(schedule.getCn());
            schedule1.addSupplyingDay(schedule.getDaysList().get(i));
            insert(schedule1);
        }
    }
}



