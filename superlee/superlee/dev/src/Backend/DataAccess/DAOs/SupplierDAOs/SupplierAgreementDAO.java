package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierNotConsistentScheduleDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.Agreement;
import Backend.Logic.LogicObjects.Supplier.ConsistentSupplierSchedule;
import Backend.Logic.LogicObjects.Supplier.NotConsistentSupplierSchedule;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierAgreementDTO;
import Backend.Logic.Starters.Starter;

import java.util.List;

public class SupplierAgreementDAO extends DAO<PK, SupplierAgreementDTO, Agreement>{



    public SupplierAgreementDAO() {
        super(SupplierAgreementDTO.class,IM.getInstance().getIdentityMap(Agreement.class));
    }


    @Override
    protected Agreement convertDtoToBusiness(SupplierAgreementDTO dto) {
        Agreement agreement = new Agreement(dto.getCn());
        if(dto.getIsConsistent().equals("TRUE")) {
            List<ConsistentSupplierSchedule> c = new SupplierConsistentScheduleDAO().getRowsFromDB("cn = " + dto.getCn());
            for (ConsistentSupplierSchedule css : c){
                agreement.addSupplyingDayFromDB(css.getDaysList().get(0));
            }
        }
        else if(dto.getIsConsistent().equals("false")){
            NotConsistentSupplierSchedule c = new SupplierNotConsistentScheduleDAO().getRow(SupplierNotConsistentScheduleDTO.getPK(dto.getCn()));
            agreement.defineSupplyingNotConsistentFromDB(c.getDaysTillNextShipment());
        }
        return agreement;
    }

    @Override
    protected SupplierAgreementDTO convertBusinessToDto(Agreement business) {
        if(business.isSupplierScheduleEmpty()){
            return new SupplierAgreementDTO(business.getCn(), "NULL");
        }
            return new SupplierAgreementDTO(business.getCn(), "" + business.isScheduleConsistent());
    }

    @Override
    protected SupplierAgreementDTO createDTO(List<Object> listFields) {
        return new SupplierAgreementDTO((String) listFields.get(0), (String) listFields.get(1));
    }
}



