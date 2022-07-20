package Backend.ServiceLayer.Facades.ServicePerJob.Employees;

import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.ServiceLayer.Result.Response;

import java.util.Date;

public class UserService {
    private final UserController userController;

    public UserService(UserController userController) {
        this.userController = userController;
    }


  /*  public Response register(int userId, String password, String firstName, String lastName, String type) {
        try {
            userController.register(userId, password, firstName, lastName, type);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }*/


    /**
     * @param userId
     * @param password
     * @return
     */

    public Response login(int userId, String password, String type) {
        try {
            userController.login(userId, password, type);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /**
     * @param userId
     * @return
     */

    public Response logout(int userId) {
        try {
            userController.logout(userId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> isLoggedIn(int userId, String type) {
        try {
            return new Response<>(userController.isLoggedIn(userId, type));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response changePassword(String oldPassword, String newPassword) {
        try {
            userController.changePassword(oldPassword, newPassword);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response addConstraint(Date date, ShiftTime shiftTime) {
        try {
            userController.addConstraint(date,shiftTime);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }    }

    public Response deleteConstraint(Date date, ShiftTime shiftTime) {
        try {
            userController.deleteConstraint(date,shiftTime);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
}
