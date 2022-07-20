package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.ProductOrder;
import Backend.DataAccess.DTOs.SupplierDTOS.ProductOrderDTO;

import java.util.List;

public class ProductOrderDAO extends DAO<PK, ProductOrderDTO, ProductOrder>{
    public ProductOrderDAO() {
        super(ProductOrderDTO.class,IM.getInstance().getIdentityMap(ProductOrder.class));
    }

    @Override
    protected ProductOrder convertDtoToBusiness(ProductOrderDTO dto) {
        return new ProductOrder(
                (int)dto.getOrderId(),
                dto.getCatalogNum(),
                dto.getProductName(),
                (int)dto.getQuantity(),
                dto.getTotalPriceWithoutDiscount(),
                dto.getDiscount(),
                dto.getFinalPrice());
    }

    @Override
    protected ProductOrderDTO convertBusinessToDto(ProductOrder business) {

        return new ProductOrderDTO(
                business.getId(),
                business.getCatalogNum(),
                business.getName(),
                business.getQuantity(),
                business.getTotalPriceWithoutDiscount(),
                business.getFinalPrice(),
                business.getDiscount());
    }

    @Override
    protected ProductOrderDTO createDTO(List<Object> listFields) {
        return new ProductOrderDTO((int)listFields.get(0),
                (String)listFields.get(1),
                (String)listFields.get(2),
                (int)listFields.get(3),
                (double)listFields.get(4),
                (double)listFields.get(5),
                (double)listFields.get(6));
    }
}



