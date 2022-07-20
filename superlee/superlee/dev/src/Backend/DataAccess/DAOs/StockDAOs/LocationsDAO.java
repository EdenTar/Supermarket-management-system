package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Location;
import Backend.DataAccess.DTOs.StockDTOS.LocationsDTO;

import java.util.List;

public class LocationsDAO extends DAO<PK, LocationsDTO, Location>{
    public LocationsDAO() {
        super(LocationsDTO.class,IM.getInstance().getIdentityMap(Location.class));
    }

    @Override
    protected Location convertDtoToBusiness(LocationsDTO dto) {
        return new Location(""+ dto.getProductId(), (int)dto.getItemId(),dto.getBranch(), Location.StoreOrStorage.valueOf(dto.getPlace()), (int)dto.getShelf());
    }

    @Override
    protected LocationsDTO convertBusinessToDto(Location business) {
        // to do. change item id and product id
        return new LocationsDTO(business.getItem_id(), Integer.parseInt(business.getProduct_number()), business.getBranch(), business.getPlace().toString(), business.getShelf());
    }

    @Override
    protected LocationsDTO createDTO(List<Object> listFields) {
        return new LocationsDTO((long)(int)listFields.get(0),(long) (int)listFields.get(1),(String) listFields.get(2), (String) listFields.get(3), (long)(int)listFields.get(4));
    }
}



