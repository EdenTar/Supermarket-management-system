package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.TransportDTOS.DestinationFileDTO;
import Backend.Logic.LogicObjects.Transport.DestinationFile;

import java.text.ParseException;
import java.util.List;

public class DestinationFileDAO extends DAO<PK, DestinationFileDTO, DestinationFile>{

    private final PointDAO pointDAO;

    public DestinationFileDAO() {
        super(DestinationFileDTO.class,IM.getInstance().getIdentityMap(DestinationFile.class));
        this.pointDAO = new PointDAO();
    }


    /**
     * @param dto
     * @return
     */
    @Override
    protected DestinationFile convertDtoToBusiness(DestinationFileDTO dto) {
        try {
            return new DestinationFile(dto.getId(), pointDAO.getRow(PointDTO.getPK(dto.getSource())),
                    pointDAO.getRow(PointDTO.getPK(dto.getDestination())),
                    getSimpleDateFormat().parse(dto.getArrivalDate()),
                    getSimpleDateFormat().parse(dto.getCreationBased()),(int)dto.getTransportId(), dto.getIsDone() == 1,dto.getSupplierId());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected DestinationFileDTO convertBusinessToDto(DestinationFile business) {
        return new DestinationFileDTO(business.getId(),
                business.getDestination().getAddress(),
                business.getDestination().getAddress(),
                business.isDone() ? 1 : 0, getSimpleDateFormat().format(business.getArrivalDate()),
                getSimpleDateFormat().format(business.getBasedOnCreationTime()),
                business.getTransportId(),
                business.getSupplierID());
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected DestinationFileDTO createDTO(List<Object> listFields) {
        return new DestinationFileDTO(
                (String) listFields.get(0), (String) listFields.get(1),
                (String) listFields.get(2), (Integer) listFields.get(3),
                (String) listFields.get(4),(String) listFields.get(5),
                (Integer) listFields.get(6), (String) listFields.get(7));
    }
    public List<DestinationFile> getDestinationFile(int transportId)
    {
        return getRowsFromDB("transportId = "+transportId );//TODO:add transport file id
    }
}



