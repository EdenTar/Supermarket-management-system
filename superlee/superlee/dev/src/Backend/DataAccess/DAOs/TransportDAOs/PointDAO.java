package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;
import Backend.Logic.Points.Branch;
import Backend.Logic.Points.Supplier;
import Backend.Logic.Points.Zone;

import java.util.List;

public class PointDAO extends DAO<PK, PointDTO, Backend.Logic.Points.Point>{

    public PointDAO() {
        super(PointDTO.class,IM.getInstance().getIdentityMap(Backend.Logic.Points.Point.class));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected Backend.Logic.Points.Point convertDtoToBusiness(PointDTO dto) {
        return dto.getTag().equals("branch") ?
                new Branch(dto.getAddress(), dto.getPhone(), dto.getContactName(), Zone.valueOf(dto.getZone())) :
                new Supplier(dto.getAddress(), dto.getPhone(), dto.getContactName(), Zone.valueOf(dto.getZone()));
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected PointDTO convertBusinessToDto(Backend.Logic.Points.Point business) {
        return business instanceof Branch ?
                new PointDTO(business.getAddress(), business.getPhone(), business.getContactName(), business.getZone().name(), "branch") :
                new PointDTO(business.getAddress(), business.getPhone(), business.getContactName(), business.getZone().name(), "supplier");

    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected PointDTO createDTO(List<Object> listFields) {
        return new PointDTO((String) listFields.get(0),
                (String) listFields.get(1), (String) listFields.get(2),
                (String) listFields.get(3), (String) listFields.get(4));
    }


}



