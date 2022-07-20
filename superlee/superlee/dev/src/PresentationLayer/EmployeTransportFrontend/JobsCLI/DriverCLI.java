package PresentationLayer.EmployeTransportFrontend.JobsCLI;

import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.ServiceObjects.Transport.LicenseService;
import Backend.ServiceLayer.Services.TransportEmployeeService;
import Obj.Action;
import Obj.Parser;
import static Obj.Parser.help;
import static Obj.Parser.help1;
import static Obj.Parser.help2;
import static Obj.Parser.help3;
import PresentationLayer.EmployeTransportFrontend.CLI;

import java.util.*;

import static PresentationLayer.EmployeTransportFrontend.CLI.*;


public class DriverCLI {

    private static TransportEmployeeService transportEmployeeService;
    private static Scanner scanner;

    private Map<Integer, Action> actionsMap = new HashMap<Integer, Action>(){
        {
            put(1, DriverCLI::weightTruck);
            put(2, DriverCLI::updateLicense);
            put(3, DriverCLI::finishDestinationFile);
            put(4, DriverCLI::showDetails);
            put(5, DriverCLI::setStarted);
            put(6, CLI::changePassword);
            put(7, CLI::addConstraints);
            put(8, CLI::deleteConstraints);
            put(9, CLI::logoutAction);
        }
    };


    public void setScannerAndTES(TransportEmployeeService transportEmployeeService, Scanner scanner){
        this.transportEmployeeService = transportEmployeeService;
        this.scanner = scanner;
    }
    public void driverScreen() {
        System.out.println("Hello driver!");
        while (!logout) {
            driverActionOptions();
            int option = Parser.getIntInput("");
            Parser.handleAction(actionsMap.get(option));
        }
    }
    private static void driverActionOptions() {
        System.out.println("choose one of the following actions");
        System.out.println("1. weight the truck");
        System.out.println("2. update license type");
        System.out.println("3. finish Destination File");
        System.out.println("4. show details");
        System.out.println("5. start drive");
        System.out.println("6. change password");
        System.out.println("7. add constraints");
        System.out.println("8. delete constraints");
        System.out.println("9. logout");
    }



    private static void setStarted() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.setStarted(Integer.parseInt(args[0])),
                f -> f.apply(help3("transport's id")));
    }

    private static void showDetails() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.showDetails(),
                f -> f.apply(help2()));
    }

    private static void finishDestinationFile() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.finishDestinationFile(args[0]),
                f -> f.apply(help3("the destination id")));
    }

    private static void updateLicense() {
        String instructions = "";
        LicenseService[] license = transportEmployeeService.showAvailableLicenses().getValue();
        for(LicenseService licenseService : license){
            instructions += licenseService.toString() + "\n";
        }
        Parser.handleServiceRequest(instructions,
                args -> transportEmployeeService.updateLicense(args[0]),
                f -> f.apply(help3("the license-type")));

    }

    private static void weightTruck() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.weightTruck(Integer.parseInt(args[0])),
                f -> f.apply(help3("the weight")));
    }
}
