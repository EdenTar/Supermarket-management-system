package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.SupplierCard;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierCardDTO;

import java.util.List;

public class SupplierCardDAO extends DAO<PK, SupplierCardDTO, SupplierCard>{

    public SupplierCardDAO() {
        super(SupplierCardDTO.class,IM.getInstance().getIdentityMap(SupplierCard.class));
    }

    @Override
    protected SupplierCard convertDtoToBusiness(SupplierCardDTO dto) {
        return new SupplierCard(dto.getCn(), dto.getSupplierName(), dto.getBankAccountNum(), dto.getPaymentMethod(), dto.getPaymentFrequency(),dto.getAddress());
    }

    @Override
    protected SupplierCardDTO convertBusinessToDto(SupplierCard business) {
        return new SupplierCardDTO(business.getCn(), business.getSupplierName(), business.getBankAccountNum(), business.getPaymentMethod(), business.getPaymentFrequency(),business.getAddress());
    }

    @Override
    protected SupplierCardDTO createDTO(List<Object> listFields) {

        return new SupplierCardDTO((String) listFields.get(0),
                (String) listFields.get(1),
                (String) listFields.get(2),
                (String) listFields.get(3),
                (String) listFields.get(4),
                (String) listFields.get(5));
    }
}



