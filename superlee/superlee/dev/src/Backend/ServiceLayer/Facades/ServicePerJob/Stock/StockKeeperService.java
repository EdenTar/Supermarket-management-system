package Backend.ServiceLayer.Facades.ServicePerJob.Stock;

import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.StockFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.SupplierCardFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.SupplierOrdersFunctionality;

public class StockKeeperService implements StockFunctionality, SupplierOrdersFunctionality,
        SupplierCardFunctionality, DeleteSupplierOrderFunctionality {
    /*
    available actions:
        manage stock
        manage supplier orders
        manage supplier cards
     */

    //acts on default constructor
}
