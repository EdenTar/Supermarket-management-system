package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.TransportDTOS.DriverDTO;
import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;
import Backend.DataAccess.DTOs.TransportDTOS.TruckDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.TransportDTOS.TransportFileDTO;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Points.Point;
import Backend.Logic.Points.Zone;
import Backend.Logic.Vehicles.Truck;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class TransportFileDAO extends DAO<PK, TransportFileDTO, TransportFile>{
    private final DriverDAO driverDAO;
    private final TruckDAO truckDAO;
    private final PointDAO pointDAO;

    public TransportFileDAO() {
        super(TransportFileDTO.class,IM.getInstance().getIdentityMap(TransportFile.class));
        this.driverDAO = new DriverDAO();
        this.truckDAO = new TruckDAO();
        this.pointDAO = new PointDAO();
    }

    /**
     * @param dto
     * @return
     */
    @Override
    protected TransportFile convertDtoToBusiness(TransportFileDTO dto) {
        Driver driver = driverDAO.getRow(DriverDTO.getPK(dto.getDriverId()));
        Truck truck = truckDAO.getRow(TruckDTO.getPK(dto.getTruckId()));
        Point point = pointDAO.getRow(PointDTO.getPK(dto.getSource()));
        try {
            return new TransportFile(
                    (int) dto.getId(),
                    getSimpleDateFormat().parse(dto.getStartingDate()),
                    getSimpleDateFormat().parse(dto.getEndDate()),
                    driver, 0, truck, point,
                    (int) dto.getStartingWeight(),
                    Zone.valueOf(dto.getFromZone()),
                    Zone.valueOf(dto.getToZone()),
                    dto.getIsFinish() == 1,
                    dto.getComment(),dto.getStarted()==1
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param business
     * @return
     */
    @Override
    protected TransportFileDTO convertBusinessToDto(TransportFile business) {
        return new TransportFileDTO(business.getFileId(), business.isStarted() ? 1 : 0,
               getSimpleDateFormat().format(business.getStartDate()), getSimpleDateFormat().format(business.getEndDate()),
                business.getDriver() == null ? business.getDriverId() : business.getDriver().getEmployeeId(), business.getTruck().getId(),
                business.getSource().getAddress(), business.getStartingWeight(),
                business.getFrom().name(), business.getTo().name(), business.getComment(), business.isFinish() ? 1 : 0);
    }

    /**
     * @param listFields
     * @return
     */
    @Override
    protected TransportFileDTO createDTO(List<Object> listFields) {
        return new TransportFileDTO(
                (Integer) listFields.get(0), (Integer) listFields.get(1),
                (String) listFields.get(2), (String) listFields.get(3),
                (Integer) listFields.get(4), (Integer) listFields.get(5),
                (String) listFields.get(6), (Integer) listFields.get(7),
                (String) listFields.get(8), (String) listFields.get(9),
                (String) listFields.get(10), (Integer) listFields.get(11));
    }

    public List<TransportFile> uploadInprogress() {
        return getRowsFromDB("isFinish = 0");
    }

    public List<TransportFile> uploadDone() {
        return getRowsFromDBOldT("isFinish = 1");
    }

    public List<TransportFile> getRowsFromDBOldT(String conditions) {

        List<TransportFileDTO> DTOList = selectAllUnderCondition(conditions);
        for (TransportFileDTO t2 : DTOList) {
            if (!identityMap.containsKey(t2.getPrimaryKey())) {
                identityMap.put(t2.getPrimaryKey(), convertDtoToBusiness(t2));
            }
        }
        return DTOList.stream().map(this::convertDtoToBusinessOldT).collect(Collectors.toList());
    }

    protected TransportFile convertDtoToBusinessOldT(TransportFileDTO dto) {
        Truck truck = truckDAO.getRow(TruckDTO.getPK(dto.getTruckId()));
        Point point = pointDAO.getRow(PointDTO.getPK(dto.getSource()));
        try {
            return new TransportFile(
                    (int) dto.getId(),
                    getSimpleDateFormat().parse(dto.getStartingDate()),
                    getSimpleDateFormat().parse(dto.getEndDate()),
                    null, (int) dto.getDriverId(), truck, point,
                    (int) dto.getStartingWeight(),
                    Zone.valueOf(dto.getFromZone()),
                    Zone.valueOf(dto.getToZone()),
                    dto.getIsFinish() == 1,
                    dto.getComment(),dto.getStarted()==1
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCurrentId() {
        List<Object> list = freeQueryOneCol("id", String.format("SELECT MAX(id) AS id FROM %s", "TransportFile"));
        return !list.isEmpty() ? (int) list.get(0) : 0;
    }
}



