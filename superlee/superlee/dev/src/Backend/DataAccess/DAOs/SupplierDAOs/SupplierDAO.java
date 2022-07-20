package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.Supplier;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierDTO;

import java.util.List;

public class SupplierDAO extends DAO<PK, SupplierDTO, Supplier>{

    public SupplierDAO() {
        super(SupplierDTO.class,IM.getInstance().getIdentityMap(Supplier.class));
    }

    @Override
    protected Supplier convertDtoToBusiness(SupplierDTO dto) {
        return new Supplier(dto.getCn());
    }

    @Override
    protected SupplierDTO convertBusinessToDto(Supplier business) {
        return new SupplierDTO(business.getCn());
    }

    @Override
    protected SupplierDTO createDTO(List<Object> listFields) {
        return new SupplierDTO((String) listFields.get(0));
    }
}



