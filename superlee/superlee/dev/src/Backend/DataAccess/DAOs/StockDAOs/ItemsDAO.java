package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.StockDTOS.LocationsDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Item;
import Backend.Logic.LogicObjects.Product.Location;
import Backend.DataAccess.DTOs.StockDTOS.ItemsDTO;
import Obj.Parser;

import java.util.List;

public class ItemsDAO extends DAO<PK, ItemsDTO, Item>{

    private LocationsDAO locationDAO;

    public ItemsDAO(){
        super(ItemsDTO.class,IM.getInstance().getIdentityMap(Item.class));
        this.locationDAO = new LocationsDAO();
    }
    /**
     * @param dto
     * @return
     */
    @Override
    protected Item convertDtoToBusiness(ItemsDTO dto) {
        Location location = locationDAO.getRow(LocationsDTO.getPK(dto.getId(), dto.getProductId()));
        return new Item(""+ dto.getProductId(),(int)dto.getId(),location, Parser.getDate(dto.getExpired()), dto.getIsFlaw().equals("false") ? false : true);
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected ItemsDTO convertBusinessToDto(Item business) {
        //to do - change product id
        return new ItemsDTO(business.getId(), Integer.parseInt(business.getProduct_number()), ""+business.isFlaw(), business.getExpired().toString());
    }


    /**
     * @param listFields
     * @return
     */
    @Override
    protected ItemsDTO createDTO(List<Object> listFields) {
        return new ItemsDTO((long)(int) listFields.get(0), (long)(int) listFields.get(1), (String) listFields.get(2), (String) listFields.get(3));
    }
}



