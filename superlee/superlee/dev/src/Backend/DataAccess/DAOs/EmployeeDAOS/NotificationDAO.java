package Backend.DataAccess.DAOs.EmployeeDAOS;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DATA_BASE.DataBaseConnection;
import Backend.DataAccess.DTOs.EmployeeDTOS.ConstraintsDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.NotificationDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Employee.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class NotificationDAO extends DAO<PK, NotificationDTO, Notification> {

    public NotificationDAO() {
        super(NotificationDTO.class, IM.getInstance().getIdentityMap(Notification.class));
    }

    @Override
    protected Notification convertDtoToBusiness(NotificationDTO dto) {
        return new Notification(dto.getDateSet());
    }

    @Override
    protected NotificationDTO convertBusinessToDto(Notification business) {
        return new NotificationDTO(business.getDateSet());

    }

    @Override
    protected NotificationDTO createDTO(List<Object> listFields) {
        return new NotificationDTO((String) listFields.get(0));

    }

    public void deleteRowNotif(String dateSet) {
        String deleteQuery = "DELETE FROM Notification WHERE dateSet='" + dateSet + "'";
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertRow(String dateSet) {
        String insertQuery = "INSERT INTO Notification (dateSet) VALUES (" + "?" + ")";
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, dateSet);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


