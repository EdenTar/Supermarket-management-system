package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DAOs.TransportDAOs.TransportItemDAO;
import Backend.DataAccess.DATA_BASE.DataBaseConnection;
import Backend.DataAccess.DTOs.StockDTOS.DiscountsDTO;
import Backend.DataAccess.DTOs.TransportDTOS.TransportItemDTO;
import Backend.DataAccess.Exceptions.DbException;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Discount;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Obj.Parser;
import Backend.Logic.LogicObjects.Product.Delivery;
import Backend.DataAccess.DTOs.StockDTOS.DeliveryDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DeliveriesDAO extends DAO<PK, DeliveryDTO, Delivery> {
    private TransportItemDAO transportItemDAO;
    public DeliveriesDAO()
    {
        super(DeliveryDTO.class, IM.getInstance().getIdentityMap(Delivery.class));
        transportItemDAO = new TransportItemDAO();
    }

    // todo: eyal easter eggggggggg we need to check this function again
    @Override
    protected Delivery convertDtoToBusiness(DeliveryDTO dto) {
        return null;
    }
    public Delivery getDelivery(){
        List<DeliveryDTO> dtos = getDTOsFromDB("SELECT * FROM  Deliveries WHERE deliveryId == (SELECT MAX(deliveryId) as deliveryId FROM Deliveries)");
        if(dtos.isEmpty()){
            return null;
        }
        List<TransportItem> transportItems = new LinkedList<>();
        for(DeliveryDTO dto: dtos){
            transportItems.add(transportItemDAO.getRow(TransportItemDTO.getPK(dto.getItemName(),dto.getItemOrderTransportId(),dto.getItemDestinationFileId())));
        }
        return new Delivery(dtos.get(0).getDeliveryId(),transportItems);
    }

    private List<DeliveryDTO> getDTOsFromDB(String selectQuery) throws DbException {
        List<DeliveryDTO> dtos = new LinkedList<>();
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                List<Object> daoFields = new ArrayList<>();
                for (Field field : getPrimitiveFields()) {
                    daoFields.add(resultSet.getObject(field.getName()));
                }
                dtos.add(createDTO(daoFields));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return dtos;
    }

    @Override
    protected DeliveryDTO convertBusinessToDto(Delivery business) {
        return null;
    }

    protected List<DeliveryDTO> convertBusinessToDtos(Delivery business) {
        //to do - change id's
        List<DeliveryDTO> dtos = new LinkedList<>();
        for(TransportItem item: business.getTransportItems()){
            dtos.add(new DeliveryDTO(business.getId(),item.getName(),item.getDestinationFileId(),item.getOrderTransportId()));
        }
        return dtos;
    }
    @Override
    public void insert(Delivery business){
        List<DeliveryDTO> dtos = convertBusinessToDtos(business);
        for(DeliveryDTO dto: dtos) {
            //--------
            Object[] values = dto.getValues();
            String dtoName = "Deliveries";
            int length = values.length;
            String insertQuery = "INSERT INTO " + dtoName + "(deliveryId,itemName,itemDestinationFileId,itemOrderTransportIds)"
                    + " VALUES" + "(?,?,?,?)";
            try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                for (int i = 1; i <= length; i++)
                    preparedStatement.setObject(i, values[i - 1]);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected DeliveryDTO createDTO(List<Object> listFields) {
        return new DeliveryDTO((Integer) listFields.get(0),(String)listFields.get(1),(String)listFields.get(2),(Integer) listFields.get(3));
    }

    public void delete(Delivery delivery){
        String deleteQuery = "DELETE FROM " + "Deliveries" + " WHERE " +String.format("deliveryId == %d",delivery.getId());
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
