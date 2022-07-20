package PresentationLayer.JobsCLI.functionalCLI;

import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;
import Obj.Parser;

public interface DeleteSupplierOrderCLI {

    DeleteSupplierOrderFunctionality getService();

    default void deleteSupplierOrder(){
        int orderId = Parser.getIntInput("insert order id:");
        Result<ServiceOrder> result = getService().deleteSupplierOrder(orderId);
        Parser.printResult(result, (res) -> "deleted " + res.toString());
    }
}
