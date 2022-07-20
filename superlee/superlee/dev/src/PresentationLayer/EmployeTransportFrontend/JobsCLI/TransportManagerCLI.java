package PresentationLayer.EmployeTransportFrontend.JobsCLI;

import Backend.Logic.Points.Zone;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.ServiceObjects.Transport.*;
import Backend.ServiceLayer.Services.TransportEmployeeService;
import Obj.Action;
import Obj.Pair;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;
import PresentationLayer.JobsCLI.functionalCLI.DeleteSupplierOrderCLI;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static Obj.Parser.*;
import static PresentationLayer.EmployeTransportFrontend.CLI.*;

public class TransportManagerCLI implements DeleteSupplierOrderCLI {

    private static TransportEmployeeService transportEmployeeService;
    private static Scanner scanner;
    private final Map<Integer, Action> actionsMap = new HashMap<Integer, Action>() {
        {
            put(1, TransportManagerCLI::showAllTransportRequests);
            put(2, TransportManagerCLI::addTruck);
            put(3, TransportManagerCLI::deleteTruck);
            put(4, TransportManagerCLI::showAllAvailableTrucks);
            put(5, TransportManagerCLI::createTransportFile);
            put(6, TransportManagerCLI::showAllAvailableDrivers);
            put(7, TransportManagerCLI::removeItems);
            put(8, TransportManagerCLI::changeTruck);
            put(9, TransportManagerCLI::cancelTransport);
            put(10, TransportManagerCLI::removeDestinationFile);
            put(11, TransportManagerCLI::getDestinationFile);
            put(12, TransportManagerCLI::showOldTransports);
            put(13, TransportManagerCLI::getRequestsByPriority);
            put(14, TransportManagerCLI::getRequestsByZone);
            put(15, TransportManagerCLI::showAllAvailableLicenses);
            put(16, TransportManagerCLI::insertBranch);
            put(17, TransportManagerCLI::insertSupplier);
            put(18, TransportManagerCLI::showInProgressTransports);
            put(19, TransportManagerCLI::getTransportFile);
            put(20, TransportManagerCLI::getOrderTransportManager);
            put(21, TransportManagerCLI::getAllPointInfoManager);
            put(22, TransportManagerCLI::changeDriver);
            put(23, TransportManagerCLI::addComment);
            put(24, TransportManagerCLI.this::deleteSupplierOrder);
            put(25, CLI::changePassword);
            put(26, CLI::addConstraints);
            put(27, CLI::deleteConstraints);
            put(28, TransportManagerCLI::showAndNotifyMatchDates);
            put(29, CLI::logoutAction);
        }
    };
    public void setScannerAndTES(TransportEmployeeService transportEmployeeService, Scanner scanner){
        this.transportEmployeeService = transportEmployeeService;
        this.scanner = scanner;
    }

    public void managerScreen() {
        System.out.println("Hello transport manager!");
        while (!logout) {
            managerActionOptions();
            int option = Parser.getIntInput("");
            Parser.handleAction(actionsMap.get(option));

        }
    }

    private static void managerActionOptions() {
        System.out.println("choose one of the following actions");
        System.out.println("01. show all transport requests");
        System.out.println("02. add truck");
        System.out.println("03. delete truck");
        System.out.println("04. show all available trucks");
        System.out.println("05. create transport file");
        System.out.println("06. show all available drivers");
        System.out.println("07. remove items");
        System.out.println("08. change truck");
        System.out.println("09. cancel transport");
        System.out.println("10. remove destination");
        System.out.println("11. get destination file");
        System.out.println("12. show old transports ");
        System.out.println("13. get requests by priority ");
        System.out.println("14. get requests by zone ");
        System.out.println("15. show all available licenses ");
        System.out.println("16. create new branch ");
        System.out.println("17. create new supplier ");
        System.out.println("18. show InProgress transports ");
        System.out.println("19. get transport file ");
        System.out.println("20. get order transport ");
        System.out.println("21. get all branches and suppliers ");
        System.out.println("22. change driver");
        System.out.println("23. add comment to transport's file");
        System.out.println("24. delete supplier order");
        System.out.println("25. change password");
        System.out.println("26. add constraints");
        System.out.println("27. delete constraints");
        System.out.println("28. show and notify match dates");

        System.out.println("29. logout");
    }
    private static void showAllTransportRequests() {
        AtomicInteger i = new AtomicInteger(1);
        Parser.handleServiceTListRequest("",
                args -> transportEmployeeService.showAllTransportRequests(),
                f -> f.apply(help2()),
                (OrderTransportService o) -> "" + i.getAndIncrement() + ". " + o.toStringWithoutItems(),
                (List<OrderTransportService> l) -> {
                    System.out.println("transport requests:");
                    return "";
                });

    }
    private static void addTruck() {

        Parser.handleServiceRequest("",
                args -> {
                    String licenseType = showOptionsForLicenseType();
                    Response response = transportEmployeeService.addTruck(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), LicenseService.valueOf(licenseType));
                    return response;
                },
                f -> f.apply(help3("truck's id", "truck's model", "base weight", "max weight")));
    }
    private static String showOptionsForLicenseType() {
        Response<LicenseService[]> serviceResponse = showAllAvailableLicenses();
        int num = Parser.getIntInput("enter license type by entering the number");
        try {
            return serviceResponse.getValue()[num - 1].name();
        }
        catch (Exception e){
            throw e;
        }
    }
    private static Response<LicenseService[]> showAllAvailableLicenses() {
        Response<LicenseService[]> serviceResponse = transportEmployeeService.showAvailableLicenses();
        if (serviceResponse.isGotError()) {
            System.out.println(serviceResponse.getError());
            return null;
        }
        AtomicInteger i = new AtomicInteger(1);
        System.out.println("Available licenses: ");
        Arrays.stream(serviceResponse.getValue()).
                forEach(licenseService ->
                        System.out.println("" + i.getAndIncrement() + ": " + licenseService));
        return serviceResponse;
    }
    private static void deleteTruck() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.deleteTruck(Integer.parseInt(args[0])),
                f -> f.apply(help3("truck's id")));
    }
    private static void showAllAvailableTrucks() {
        AtomicInteger i = new AtomicInteger(1);
        Parser.handleServiceTListRequest("",
                args -> transportEmployeeService.showAllAvailableTrucks(Parser.getDate(args[0]), Parser.getDate(args[1])),
                f -> f.apply(help3("Start Date", "End Date")),
                (ServiceTruck t) -> "" + i.getAndIncrement() + ": " + t.toString(),
                (List<ServiceTruck> l) -> {
                    l.sort(Comparator.comparing(ServiceTruck::getLicense));
                    return "";
                });

    }

    private static void createTransportFile() {
        Parser.handleServiceRequest("",
                args -> {
                    Date startDate = Parser.getComplexDateInput("please enter transports file start date (dd/MM/yyyy hh:mm:ss):");
                    Date endDate = Parser.getComplexDateInput("please enter transports file end date (dd/MM/yyyy hh:mm:ss):");
                    int truckId = Parser.getIntInput("please enter truck id:");
                    String source = Parser.getStrInput("please enter source:");
                    String[] options = new String[Zone.values().length];
                    AtomicInteger i = new AtomicInteger(0);
                    Arrays.stream(Zone.values()).forEach(v -> options[i.getAndIncrement()] = v.toString());
                    System.out.println("please enter from zone:");
                    String from = Parser.chooseFromList(options);
                    System.out.println("please enter to zone:");
                    String to = Parser.chooseFromList(options);
                    System.out.println("please choose ids:");
                    List<Obj.Pair<Integer, Date>> orderIdDate = chooseIds();
                    List<Integer> ids = orderIdDate.stream().map((Pair::getKey)).collect(Collectors.toList());
                    List<Date> dates = orderIdDate.stream().map((Pair::getValue)).collect(Collectors.toList());
            return transportEmployeeService.createTransportFile(startDate, endDate, truckId, source, from, to, ids, dates);
                },
                f -> f.apply(help2()));

    }

    private static List<Obj.Pair<Integer, Date>> chooseIds() {
        return Parser.getListInput(() -> {
            int id = Parser.getIntInput("please enter id ");
            Date date = Parser.getComplexDateInput("please enter date (dd/MM/yyyy hh:mm:ss)");
            return new Pair<>(id, date);
        });
    }





    private static boolean showAllTransportRequestsIds() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Parser.handleServiceTListRequest("",
                args -> transportEmployeeService.showAllTransportRequests(),
                f -> f.apply(help2()),
                (OrderTransportService o) -> "" + o.getOrderTransportId(),
                (l) -> {
                    System.out.println("transport requests id's:");
                    atomicBoolean.set(true);
                    return "";
                });

        return atomicBoolean.get();


    }
    private static void showAllAvailableDrivers() {
        AtomicInteger i = new AtomicInteger(1);
        Parser.handleServiceTListRequest("",
                args -> transportEmployeeService.showAvailableDrivers(Parser.getDate(args[0]), Parser.getDate(args[1])),
                f -> f.apply(help3("Start Date (dd-MM-yyyy)", "End Date(dd-MM-yyyy)")),
                (ServiceDriver d) -> "" + i.getAndIncrement() + " " + d.toString(),
                (List<ServiceDriver> l) -> {
                    System.out.println("Available drivers: ");
                    l.sort(Comparator.comparing(ServiceDriver::getEmployeeId));
                    return "";
                });

    }
    private static void addComment() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addComment(args[0], Integer.parseInt(args[1])),
                f -> f.apply(help3("a comment", "transport file id")));
    }

    private static void changeDriver() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.changeDriver(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                f -> f.apply(help3("driver Id", "transport File Id")));
    }

    private static void getAllPointInfoManager() {
        Parser.handleServiceStringListRequest(
                args -> transportEmployeeService.getAllPointInfo(),
                f -> f.apply(help2()));



    }

    private static void getOrderTransportManager() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getOrderTransport(Integer.parseInt(args[0])),
                f -> f.apply(help3("order's id which you would like to see")));

    }

    private static void getTransportFile() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getTransportFile(Integer.parseInt(args[0])),
                f -> f.apply(help3("transport's file id which you would like to see")));

    }
    private static void showAndNotifyMatchDates() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.showAndNotifyMatchDates(Integer.parseInt(args[0])),
                f -> f.apply(help3("enter order-Id")));

    }
    private static void showInProgressTransports() {
        Parser.handleServiceTListRequest("InProgress transports:",
                args -> transportEmployeeService.showInProgressTransports(),
                f -> f.apply(help2()),
                (ServiceTransportFile t) -> t.toStringWithoutItems(),
                (l) -> "");
    }

    private static void insertSupplier() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.insertSupplier(args[0], args[1], args[2],args[3]),
                f -> f.apply(help3("supplier's address", "supplier's phone", "supplier's contactName", "supplier's zone")));

    }

    private static void insertBranch() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.insertBranch(args[0], args[1], args[2],args[3]),
                f -> f.apply(help3("branch's address", "branch's phone", "branch's contactName", "branch's zone")));
    }



    private static void getRequestsByZone() {
        Parser.handleServiceTListRequest("",
                args -> transportEmployeeService.getRequestsByZone(args[0], args[1]),
                f -> f.apply(help3("request's from-zone", "request's to-zone")));
    }

    private static void getRequestsByPriority() {
        Parser.handleServiceValueRequest("The request by priority:",
                args -> transportEmployeeService.getRequestsByPriority(),
                f -> f.apply(help2()));

    }

    private static void showOldTransports() {
        Parser.handleServiceTListRequest("Old Transports:",
                args -> transportEmployeeService.showOldTransports(),
                f -> f.apply(help2()));
    }

    private static void getDestinationFile() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getDestinationFile(args[0]),
                f -> f.apply(help3("destination File Id")));

    }

    private static void removeDestinationFile() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.removeDestination(Integer.parseInt(args[0]), args[1]),
                f -> f.apply(help3("transport file id", "destination")));

    }

    private static void cancelTransport() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.cancelTransport(Integer.parseInt(args[0])),
                f -> f.apply(help3("transport file id")));

    }

    private static void changeTruck() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.changeTruck(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                f -> f.apply(help3("truck Id", "transport File Id")));
    }

    private static void removeItems() {
        String destinationId = Parser.getStrInput("please enter destinationId in the format x-y");
        List<Backend.Logic.Utilities.Pair<String,Integer>> itemQuantity = new ArrayList<>();
        List<Integer> i = Parser.getListInput(() -> {
            System.out.println("please enter item name and quantity seperated by ,");
            String[] split = help();
            itemQuantity.add(new Backend.Logic.Utilities.Pair<>(split[0],Integer.parseInt(split[1])));
            return 0;
        });
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.removeItems(destinationId, itemQuantity),
                f -> f.apply(help2()));
    }

    @Override
    public DeleteSupplierOrderFunctionality getService() {
        return transportEmployeeService;
    }
}
