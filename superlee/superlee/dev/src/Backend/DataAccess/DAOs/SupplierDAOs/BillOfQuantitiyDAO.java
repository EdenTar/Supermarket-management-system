package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.BillOfQuantities;
import Backend.DataAccess.DTOs.SupplierDTOS.BillOfQuantitiyDTO;

import java.util.List;

public class BillOfQuantitiyDAO extends DAO<PK, BillOfQuantitiyDTO, BillOfQuantities>{
    public BillOfQuantitiyDAO() {
        super(BillOfQuantitiyDTO.class,IM.getInstance().getIdentityMap(BillOfQuantities.class));
    }



    //get the schedule for a supplier
    public BillOfQuantities getFullBillOfQuantities(String supplierCn,String productName){
        BillOfQuantities c = new BillOfQuantities(supplierCn,productName);
        List<BillOfQuantitiyDTO> dtos = selectAllUnderCondition("cn = " + supplierCn);
        for(BillOfQuantitiyDTO dto : dtos){
            c.addRange((int)dto.getStartRange(),(double)dto.getDiscount());
        }
        return c;
    }


    @Deprecated
    @Override
    protected BillOfQuantities convertDtoToBusiness(BillOfQuantitiyDTO dto) {
        BillOfQuantities billOfQuantities = new BillOfQuantities(dto.getCn(), dto.getProductName());
        billOfQuantities.addRange((int)dto.getStartRange(),dto.getDiscount());
        return billOfQuantities;
    }

    @Override
    protected BillOfQuantitiyDTO convertBusinessToDto(BillOfQuantities business) {
        return new BillOfQuantitiyDTO(business.getCn(),business.getProductName(),(double) business.getRanges().get(0).getValue(),(long) business.getRanges().get(0).getKey());
    }

    @Override
    protected BillOfQuantitiyDTO createDTO(List<Object> listFields) {
        return new BillOfQuantitiyDTO((String)listFields.get(0),
                (String) listFields.get(1),
                (double)listFields.get(2),
                (int)listFields.get(3));
    }

    public void insertBillOfQuantity(BillOfQuantities billOfQuantities){
        for(int i = 1; i < billOfQuantities.getRanges().size() ; i++){
            int range = billOfQuantities.getRanges().get(i).getKey();
            double discount = billOfQuantities.getRanges().get(i).getValue();
            BillOfQuantities billOfQuantities1 = new BillOfQuantities(billOfQuantities.getCn(),billOfQuantities.getProductName(),range,discount);
            insert(billOfQuantities1);
        }
    }
    
}



