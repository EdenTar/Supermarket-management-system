package PresentationLayer.EmployeTransportFrontend.JobsCLI;

import Backend.ServiceLayer.Services.TransportEmployeeService;
import Obj.Action;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static PresentationLayer.EmployeTransportFrontend.CLI.*;

public class BasicEmployeeCLI {

    private static TransportEmployeeService transportEmployeeService;
    private static Scanner scanner;
    private Map<Integer, Action> actionsMap = new HashMap<Integer, Action>(){
        {
            put(1, CLI::changePassword);
            put(2, CLI::addConstraints);
            put(3, CLI::deleteConstraints);
            put(4, CLI::logoutAction);
        }
    };
    public void setScannerAndTES(TransportEmployeeService transportEmployeeService, Scanner scanner){
        this.transportEmployeeService = transportEmployeeService;
        this.scanner = scanner;
    }
    private void basicEmployeeActionOptions() {
        System.out.println("choose one of the following actions");
        System.out.println("1. change password");
        System.out.println("2. add constraints");
        System.out.println("3. delete constraints");
        System.out.println("4. logout");
    }
    public void basicEmployeeScreen() {
        System.out.println("Hello employee!");
        while (!logout) {
            basicEmployeeActionOptions();
            int option = Parser.getIntInput("");
            Parser.handleAction(actionsMap.get(option));
        }
    }
}
