package Backend.DataAccess.DAOs.TransportDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.DataAccess.DATA_BASE.DataBaseConnection;
import Backend.DataAccess.DTOs.TransportDTOS.TransportItemDTO;
import Backend.Logic.LogicObjects.Transport.TransportItem;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransportItemDAO extends DAO<PK, TransportItemDTO, TransportItem> {

    public TransportItemDAO() {
        super(TransportItemDTO.class, IM.getInstance().getIdentityMap(TransportItem.class));
    }

    @Override
    protected TransportItem convertDtoToBusiness(TransportItemDTO dto) {
        return new TransportItem(
                dto.getName(),
                (int) dto.getQuantity(),
                (int) dto.getOrderTransportId(),
                dto.getDestinationFileId());
    }

    @Override
    protected TransportItemDTO convertBusinessToDto(TransportItem business) {
        return new TransportItemDTO(business.getName(),
                business.getQuantity(), business.getOrderTransportId(), business.getDestinationFileId());//TODO remember to update
    }

    @Override
    protected TransportItemDTO createDTO(List<Object> listFields) {
        return new TransportItemDTO(
                (String) listFields.get(0),
                (Integer) listFields.get(1), (Integer) listFields.get(2),
                (String) listFields.get(3));
    }

    public List<TransportItem> getItemForOrderTransport(int orderTransportId) {
        return getRowsFromDB("orderTransportId =" + orderTransportId);
    }

    public List<TransportItem> getItemForOrderDestinationFile(String destinationFileID) {
        return getRowsFromDB("destinationFileId =" + destinationFileID);
    }

    public void update(TransportItem transportItem, String destinationFileId, int orderTransportId, String name) {
        TransportItemDTO t2 = convertBusinessToDto(transportItem);

        String daoName = "TransportItem";
        String updateQuery = "UPDATE " + daoName + " SET " + t2.valuesToString()
                + " WHERE " + " orderTransportId = " + orderTransportId + " AND destinationFileId = " + "'" + destinationFileId + "' AND " + " name = " + "'"+ name+"'";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



