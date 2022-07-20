package PresentationLayer.EmployeTransportFrontend;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Jobs.StockKeeper;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.Services.TransportEmployeeService;
import static Obj.Parser.help;
import static Obj.Parser.help1;
import static Obj.Parser.help2;
import static Obj.Parser.help3;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.BasicEmployeeCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.DriverCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.HRManagerCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.TransportManagerCLI;
import PresentationLayer.JobsCLI.CLIStockKeeper;
import PresentationLayer.JobsCLI.CLIStoreManager;
import PresentationLayer.JobsCLI.CLISupplierManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("ALL")
public class CLI {
    public static boolean isShutDown() {
        return SHUT_DOWN;
    }
    public static TransportEmployeeService transportEmployeeService;
    public static Scanner scanner;
    private final BasicEmployeeCLI basicEmployeeCLI;
    private final DriverCLI driverCLI;
    private final HRManagerCLI hrManagerCLI;
    private final TransportManagerCLI transportManagerCLI;
    private final CLIStockKeeper stockKeeperCLI;
    private final CLIStoreManager storeManagerCLI;
    private final CLISupplierManager supplierManagerCLI;


    public CLI(BasicEmployeeCLI basicEmployeeCLI, DriverCLI driverCLI, HRManagerCLI hrManagerCLI, TransportManagerCLI transportManagerCLI,
               CLIStockKeeper stockKeeperCLI, CLIStoreManager storeManagerCLI, CLISupplierManager supplierManagerCLI) {
        this.basicEmployeeCLI = basicEmployeeCLI;
        this.driverCLI = driverCLI;
        this.hrManagerCLI = hrManagerCLI;
        this.transportManagerCLI = transportManagerCLI;
        this.stockKeeperCLI = stockKeeperCLI;
        this.storeManagerCLI = storeManagerCLI;
        this.supplierManagerCLI = supplierManagerCLI;
        basicEmployeeCLI.setScannerAndTES(transportEmployeeService, scanner);
        driverCLI.setScannerAndTES(transportEmployeeService, scanner);
        hrManagerCLI.setScannerAndTES(transportEmployeeService, scanner);
        transportManagerCLI.setScannerAndTES(transportEmployeeService, scanner);
    }

    enum Job {
        TRANSPORT_MANAGER,
        DRIVER,
        HR_MANAGER,
        BASIC_EMPLOYEE,
        STOCK_KEEPER,
        SUPPLIER_MANAGER,
        STORE_MANAGER
    }

    public static Job currentJob;
    public static boolean SHUT_DOWN = false;
    public static boolean logout = true;


    public static void shutdownWait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void screenPerJob(Job currentJob) {
        if (currentJob == null) return;
        switch (currentJob) {
            case TRANSPORT_MANAGER:
                transportManagerCLI.managerScreen();
                break;
            case DRIVER:
                driverCLI.driverScreen();
                break;
            case HR_MANAGER:
                hrManagerCLI.hrManagerScreen();
                break;
            case BASIC_EMPLOYEE:
                basicEmployeeCLI.basicEmployeeScreen();
                break;
            case STOCK_KEEPER:
                stockKeeperCLI.start();
                break;
            case STORE_MANAGER:
                storeManagerCLI.start();
                break;
            case SUPPLIER_MANAGER:
                supplierManagerCLI.start();
                break;
        }
    }

    public static void logoutAction() {
        AtomicReference<Response> response = new AtomicReference<>(null);
        Parser.handleServiceRequest("",
                args -> {response.set(transportEmployeeService.logout(Integer.parseInt(args[0])));
            return response.get();
            },
                f -> f.apply(help3("your id")));
        if (!response.get().isGotError()){
            logout = true;
        }
    }


    public void loginScreen() {
        showOptionsForLoginScreen();
        while (true) {
            int option = Parser.getIntInput("please enter option");
            switch (option) {
                case 1:
                    enterCredentials();
                    return;
                case 2:
                    SHUT_DOWN = true;
                    return;
            }
        }
    }

    private static String showOptionsForType() {
        System.out.println("please enter your rule from the following options by entering the number:");
        String[] options = new String[Job.values().length];
        for(int i = 0; i < Job.values().length; i++){
            options[i] = Job.values()[i].name();
        }
        return Parser.chooseFromList(options);
        //return Parser.chooseFromList((String[]) Arrays.stream(Job.values()).map(t -> t.name()).toArray());
    }

    private static void enterCredentials() {
        AtomicReference<String> type = new AtomicReference<>(null);
        AtomicReference<Response> response = new AtomicReference<>(null);
        Parser.handleServiceRequest("",
                args -> {
                    type.set(showOptionsForType());
                    response.set(transportEmployeeService.login(Integer.parseInt(args[0]), args[1], type.get()));
                    return response.get();
                },
                f -> f.apply(help3("your id", "password")));
        if(!response.get().isGotError()){
            currentJob = Job.valueOf(type.get());
            logout = false;
        }
    }


    private static void showOptionsForLoginScreen() {
        System.out.println("Choose the action by entering the according number, you would like to perform");
        System.out.println("1. LOGIN");
        System.out.println("2. SHUTDOWN");

    }

    public static void deleteConstraints() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.deleteConstraint(Parser.getDate(args[0]), ShiftTime.valueOf(args[1])),
                f -> f.apply(help3("date dd/mm/yyyy","shift time(Morning/Evening)")));
    }

    public static void addConstraints() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addConstraint(Parser.getDate(args[0]), ShiftTime.valueOf(args[1])),
                f -> f.apply(help3("date dd/mm/yyyy","shift time(Morning/Evening)")));

    }

    public static void changePassword() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.changePassword(args[0], args[1]),
                f -> f.apply(help3("old-password", "new password")));

    }


}
