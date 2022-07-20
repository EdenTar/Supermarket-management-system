package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DTOs.TransportDTOS.OrderTransportDTO;

import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.Points.Point;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTransportDAO extends DAO<PK, OrderTransportDTO, OrderTransport> {
    private final PointDAO pointDAO;


    public OrderTransportDAO() {
        super(OrderTransportDTO.class, IM.getInstance().getIdentityMap(OrderTransport.class));
        this.pointDAO = new PointDAO();
    }

    @Override
    protected OrderTransport convertDtoToBusiness(OrderTransportDTO dto) {
        Point origin = pointDAO.getRow(PointDTO.getPK(dto.getOrigin()));
        Point destination = pointDAO.getRow(PointDTO.getPK(dto.getDestination()));
        try {
            return new OrderTransport((int) dto.getOrderTransportId(), origin, destination,
                    getSimpleDateFormat().parse(dto.getBasedOnCreationTime()),dto.getSupplierID());//TODO shaun
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected OrderTransportDTO convertBusinessToDto(OrderTransport business) {
        return new OrderTransportDTO(
                business.getOrderTransportId(),
                business.getOrigin().getAddress(),
                business.getDestination().getAddress(),
                getSimpleDateFormat().format(business.getCreationDate()),
                        business.getSupplierId());
    }
/*    private String convertToSimpleDateFormat(Date creationDate) {

        String month = creationDate.getMonth()<10?"0"+creationDate.getMonth():""+creationDate.getMonth();
        return ""+creationDate.getDate()+"-"+month+"-"+ (creationDate.getYear()+1900) +" 00:00:00 ";
    }*/


    @Override
    protected OrderTransportDTO createDTO(List<Object> listFields) {
        return new OrderTransportDTO(
                (Integer) listFields.get(0), (String) listFields.get(1),
                (String) listFields.get(2), (String) listFields.get(3),
                (String) listFields.get(4));
    }

    public int getCurrentId() {
        List<Object> list = freeQueryOneCol("orderTransportId", "SELECT orderTransportId\n" +
                "FROM (SELECT MAX(orderTransportId) AS orderTransportId FROM OrderTransport)\n" +
                "WHERE orderTransportId IS NOT NULL");
        return !list.isEmpty() ? (int) list.get(0) : 0;
    }

    public List<OrderTransport> getOrderTransportById(List<Integer> list) {
        return list.stream().map(x -> getRow(OrderTransportDTO.getPK(x))).collect(Collectors.toList());
    }

    public OrderTransport getOldestOrder()//TODO:check if we get oldest
    {
        List<Object> list = freeQueryOneCol("id",
                "SELECT orderTransportId FROM (SELECT MIN(orderTransportId) AS orderTransportId\n" +
                        "                              FROM OrderTransport) WHERE orderTransportId is not null");
        int id = !list.isEmpty() ? (int) list.get(0) : 0;//TODO what is id?
        return getRow(OrderTransportDTO.getPK((int) list.get(0)));
    }

    public List<OrderTransport> getReqByZone(String from, String to) {
        return getRowsFromDB(String.format("origin = %s AND destination = %s", from, to));
    }
}



