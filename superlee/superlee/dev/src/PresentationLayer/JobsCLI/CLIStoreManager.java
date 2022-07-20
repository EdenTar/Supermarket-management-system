package PresentationLayer.JobsCLI;

import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StockKeeperService;
import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StoreManagerService;
import Obj.Action;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;
import PresentationLayer.JobsCLI.functionalCLI.InventoryManagement;
import PresentationLayer.JobsCLI.functionalCLI.ReportManagement;
import PresentationLayer.JobsCLI.functionalCLI.SupplierCardManagement;
import PresentationLayer.JobsCLI.functionalCLI.SupplierOrders;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static PresentationLayer.EmployeTransportFrontend.CLI.logout;

public class CLIStoreManager {

    /*
available actions:
    manage supplier cards
    get reports of all kinds
 */
    private ReportManagement reportManagement;
    private SupplierCardManagement supplierCardManagement;
    private final Scanner scanner;
    private final StoreManagerService service;
    private Map<String, Action> actionsMap = new HashMap<String, Action>() {
        {
            put("1", () -> supplierCardManagement.run());
            put("2", () -> reportManagement.run());
            put("3", CLI::changePassword);
            put("4", CLI::addConstraints);
            put("5", CLI::deleteConstraints);
            put("6", CLI::logoutAction);
        }
    };

    public CLIStoreManager(StoreManagerService service, Scanner scanner) {
        this.scanner = scanner;
        this.service = service;
    }


    private String getStockKeeperMenu() {
        return "choose the wanted functionality:\n" +
                "1 - supplier card management\n" +
                "2 - report service\n" +
                "3 - change password\n" +
                "4 - add constraints\n" +
                "5 - delete constrains\n" +
                "6 - logout";
    }

    public void start() {
        System.out.println("Welcome Store Manager!");
        String userInput;
        reportManagement = new ReportManagement(service, scanner);
        supplierCardManagement = new SupplierCardManagement(service, scanner);
        while (!logout) {
            userInput = Parser.getStrInput(getStockKeeperMenu());
            Parser.handleAction(actionsMap.get(userInput));
        }

    }

}
