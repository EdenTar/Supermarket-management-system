package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.TransportDTOS.TruckDTO;
import Backend.Logic.Vehicles.License;

import java.util.List;

public class TruckDAO extends DAO<PK, TruckDTO, Backend.Logic.Vehicles.Truck>{

    public TruckDAO() {
        super(TruckDTO.class,IM.getInstance().getIdentityMap(Backend.Logic.Vehicles.Truck.class));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected Backend.Logic.Vehicles.Truck convertDtoToBusiness(TruckDTO dto) {
        return new Backend.Logic.Vehicles.Truck((int) dto.getTruckId(), dto.getModel(),
                (int) dto.getBasicWeight(), (int) dto.getMaxWeight(), License.valueOf(dto.getLicense()));
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected TruckDTO convertBusinessToDto(Backend.Logic.Vehicles.Truck business) {
        return new TruckDTO(business.getId(), business.getModel(), business.getBasicWeight(), business.getMaxWeight(), business.getLicense().name());
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected TruckDTO createDTO(List<Object> listFields) {
        return new TruckDTO(
                (Integer) listFields.get(0), (String) listFields.get(1),
                (Integer) listFields.get(2), (Integer) listFields.get(3),
                (String) listFields.get(4));
    }

    public List<Backend.Logic.Vehicles.Truck> getFreeTrucks() {
        return getRowsFromDB("isBusy = 0");
    }
}



