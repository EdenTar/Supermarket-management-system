package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.EmployeeDAOS.EmployeeDAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Employee.ShiftTime;

import java.util.Date;
import java.util.List;

public abstract class Controller {
    private EmployeeDAO employeeDAO= new EmployeeDAO();
    protected void containsEmployee(List<? extends Employee> list, int id){
        if(list.stream().anyMatch((employee -> employee.getEmployeeId()==id ))){
            throw new IllegalArgumentException("Can not register an already existing employee");
        }
    }
    protected void containsEmployee(int id){
        if(employeeDAO.getRow(EmployeeDTO.getPK(id)) != null){
            throw new IllegalArgumentException("Can not register an already existing employee");
        }
    }


    protected void containsEmployeeByIDAndPassword(List<?extends Employee> employees, int id,String password) {
        if(employees.stream().noneMatch(((employee)-> employee.getEmployeeId() == id && employee.getPassword().equals(password)))){
            throw new IllegalArgumentException(id+" is not register in the system");
        }
    }


    protected void containsEmployeeByIDAndPassword(int id,String password) {
        Employee employee = employeeDAO.getRow(EmployeeDTO.getPK(id));
        if(employee == null || !employee.getPassword().equals(password)){
            throw new IllegalArgumentException(id+" is not register in the system");
        }
    }
    protected Employee getEmployeeByIDAndPassword(List<?extends Employee> employees, int id,String password) {
        return employees.stream().
                filter(((employee)-> employee.getEmployeeId() ==id
                        && employee.getPassword().equals(password))).findFirst().get();

    }
    protected Employee getEmployeeByIDAndPassword(int id,String password) {
        Employee employee = employeeDAO.getRow(EmployeeDTO.getPK(id));
        if(employee != null && employee.getPassword().equals(password)){
            return employee;
        }
        return null;
    }

    protected abstract void logout(int userId);

    protected abstract boolean isLoggedIn(int id) ;

    public abstract void addConstraint(Date date, ShiftTime shiftTime) throws Exception;

    public abstract void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception;

    public abstract void changePassword(String oldPassword, String newPassword) throws Exception;

    protected abstract boolean isLoggedIn() ;
}
