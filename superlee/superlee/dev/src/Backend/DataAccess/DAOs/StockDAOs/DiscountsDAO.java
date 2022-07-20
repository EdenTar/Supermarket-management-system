package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Discount;
import Backend.DataAccess.DTOs.StockDTOS.DiscountsDTO;
import Obj.Parser;

import java.util.List;

public class DiscountsDAO extends DAO<PK, DiscountsDTO, Discount>{



    public DiscountsDAO() {
        super(DiscountsDTO.class,IM.getInstance().getIdentityMap(Discount.class));
    }

    @Override
    protected Discount convertDtoToBusiness(DiscountsDTO dto) {
        return new Discount((String) dto.getId(), (dto.getCategoryId() == -1 ? null : (int)(long) dto.getCategoryId()),dto.getProductId() == -1 ? null : "" + dto.getProductId() ,dto.getDiscount(), Parser.getDate(dto.getDateFrom()), Parser.getDate(dto.getDateTo()));
    }

    @Override
    protected DiscountsDTO convertBusinessToDto(Discount business) {
        //to do - change id's
        if(business.getCategory_id() == null){
            return new DiscountsDTO(business.getId(), (long) -1, (long)Integer.parseInt(business.getProduct_id()), business.getDiscount(), business.getDateFrom().toString(), business.getDateTo().toString());
        }
        return new DiscountsDTO(business.getId(), (long)(int)business.getCategory_id(), (long) -1, business.getDiscount(), business.getDateFrom().toString(), business.getDateTo().toString());
    }

    @Override
    protected DiscountsDTO createDTO(List<Object> listFields) {
        return new DiscountsDTO((String)listFields.get(0),(long)(int)listFields.get(1),(long)(int)listFields.get(2),(double)listFields.get(3),(String) listFields.get(4),(String) listFields.get(5));
    }
}



