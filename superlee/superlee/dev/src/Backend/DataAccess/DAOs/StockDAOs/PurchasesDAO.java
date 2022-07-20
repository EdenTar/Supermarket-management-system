package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Purchase;
import Backend.DataAccess.DTOs.StockDTOS.PurchasesDTO;
import Obj.Parser;

import java.util.List;

public class PurchasesDAO extends DAO<PK, PurchasesDTO, Purchase>{
    public PurchasesDAO() {
        super(PurchasesDTO.class,IM.getInstance().getIdentityMap(Purchase.class));
    }

    @Override
    protected Purchase convertDtoToBusiness(PurchasesDTO dto) {
        try {
            return new Purchase("" + dto.getProductId(), dto.getCostPrice(), dto.getSalePrice(), dto.getDiscount(), (int)dto.getQuantity(), dto.getSupplier(), Parser.getDate(dto.getPurchaseTime()));
        } catch (Exception e) {
            System.out.println("purchase dal " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PurchasesDTO convertBusinessToDto(Purchase business) {
        // to do - change product id
        return new PurchasesDTO(Long.parseLong(business.getProductId()), business.getCostPrice(), business.getSalePrice(), business.getDiscount(), business.getQuantity(), business.getSupplier(), business.getPurchaseTime().toString());
    }

    @Override
    protected PurchasesDTO createDTO(List<Object> listFields) {
        return new PurchasesDTO((long)(int)listFields.get(0),(double)listFields.get(1),(double)listFields.get(2),(double)listFields.get(3),(long)(int)listFields.get(4),(String)listFields.get(5), (String) listFields.get(6));
    }
}



