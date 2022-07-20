package Backend.ServiceLayer.Facades.ServicePerJob.Employees;

import Backend.Logic.Controllers.TransportEmployee.BasicEmployeeController;

public class BasicEmployeeService {

    private BasicEmployeeController basicEmployeeController;
    public BasicEmployeeService(){
        basicEmployeeController=new BasicEmployeeController();
    }

}
