package PresentationLayer.EmployeTransportFrontend;

import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.Result.Result;

public interface ServiceFunction {
    Result apply(ServiceFunctionHelp serviceFunctionHelp);
}
