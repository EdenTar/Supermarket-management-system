package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.SupplierProduct;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierProductsDTO;

import java.util.List;

public class SupplierProductsDAO extends DAO<PK, SupplierProductsDTO, SupplierProduct>{

    public SupplierProductsDAO() {
        super(SupplierProductsDTO.class,IM.getInstance().getIdentityMap(SupplierProduct.class));
    }

    @Override
    protected SupplierProduct convertDtoToBusiness(SupplierProductsDTO dto) {
        return new SupplierProduct(dto.getProductName(),dto.getCatalogNum(),dto.getPrice(),dto.getCn());
    }

    @Override
    protected SupplierProductsDTO convertBusinessToDto(SupplierProduct business) {
        return new SupplierProductsDTO(business.getCn(),business.getName(),business.getPrice(),business.getCatalogNum());
    }

    @Override
    protected SupplierProductsDTO createDTO(List<Object> listFields) {
        return new SupplierProductsDTO((String) listFields.get(0),(String) listFields.get(1),(Double) listFields.get(2),(String) listFields.get(3));
    }
}



