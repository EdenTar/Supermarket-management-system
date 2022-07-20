package PresentationLayer.EmployeTransportFrontend.JobsCLI;

import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.Services.TransportEmployeeService;
import Obj.Action;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;
import PresentationLayer.EmployeTransportFrontend.ServiceFunction;
import PresentationLayer.EmployeTransportFrontend.ServiceFunctionHelp;
import PresentationLayer.JobsCLI.functionalCLI.DeleteSupplierOrderCLI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static PresentationLayer.EmployeTransportFrontend.CLI.*;
import static Obj.Parser.help;
import static Obj.Parser.help1;
import static Obj.Parser.help2;
import static Obj.Parser.help3;

public class HRManagerCLI implements DeleteSupplierOrderCLI {

    private static TransportEmployeeService transportEmployeeService;
    private static Scanner scanner;

    private Map<Integer, Action> actionsMap = new HashMap<Integer, Action>(){
        {
            put(1, HRManagerCLI::addNewJob);
            put(2, HRManagerCLI::addNewEmployee);
            put(3, HRManagerCLI::updateBankBranch);
            put(4, HRManagerCLI::updateAccountNumber);
            put(5, HRManagerCLI::updateBankNumber);
            put(6, HRManagerCLI::removeEmployee);
            put(7, HRManagerCLI::addScheduling);
            put(8, HRManagerCLI::removeScheduling);
            put(9, HRManagerCLI::updateShiftManager);
            put(10, HRManagerCLI::addNewShift);
            put(11, HRManagerCLI::setPosition);
            put(12, HRManagerCLI::addShiftPosition);
            put(13, HRManagerCLI::removeShiftPosition);
            put(14, HRManagerCLI::getNumberOfShiftsStatistics);
            put(15, HRManagerCLI::getNumberOfEveningShiftsStatistics);
            put(16, HRManagerCLI::getNumberOfMorningShiftsStatistics);
            put(17, HRManagerCLI::showEmployees);
            put(18, HRManagerCLI::updateSalary);
            put(19, HRManagerCLI::showForEachEmployeeHisJob);
            put(20, HRManagerCLI::addSocialBenefits);
            put(21, HRManagerCLI::addNewSocialBenefits);
            put(22, HRManagerCLI::updateFirstName);
            put(23, HRManagerCLI::updateLastName);
            put(24, HRManagerCLI::updateIsShiftManager);
            put(25, HRManagerCLI::getHistory);
            put(26, HRManagerCLI::getEmployeeConstrains);
            put(27, HRManagerCLI::getShiftPlacements);
            put(28, HRManagerCLI.this::deleteSupplierOrder);
            put(29, CLI::changePassword);
            put(30, CLI::addConstraints);
            put(31, CLI::deleteConstraints);
            put(32,HRManagerCLI::scheduleDriver);
            put(33, CLI::logoutAction);
        }
    };

    public void setScannerAndTES(TransportEmployeeService transportEmployeeService, Scanner  scanner){
        this.transportEmployeeService = transportEmployeeService;
        this.scanner = scanner;
    }

    public void hrManagerScreen() {
        System.out.println("Hello HR manager!");
        while (!logout) {
            hrManagerActionOptions();
            int option = Parser.getIntInput("");
            Parser.handleAction(actionsMap.get(option));
        }
    }
    private static void hrManagerActionOptions() {
        System.out.println("choose one of the following actions");
        System.out.println("01. add new Job");
        System.out.println("02. add new employee");
        System.out.println("03. update bank branch");
        System.out.println("04  update account number");
        System.out.println("05. update bank number");
        System.out.println("06. remove employee");
        System.out.println("07. add scheduling");
        System.out.println("08. remove scheduling");
        System.out.println("09. update shift manager");
        System.out.println("10. add new shift");
        System.out.println("11. set position");
        System.out.println("12. add shift position ");
        System.out.println("13. remove shift position ");
        System.out.println("14. get number of shifts statistics ");
        System.out.println("15. get number of evening shifts statistics ");
        System.out.println("16. get number of morning shifts statistics ");
        System.out.println("17. show employees ");
        System.out.println("18. update salary ");
        System.out.println("19. show for each employee his job ");
        System.out.println("20. add social benefits ");
        System.out.println("21. add new social benefits");
        System.out.println("22. update first name");
        System.out.println("23. update last name");
        System.out.println("24. update is shift manager");
        System.out.println("25. get history");
        System.out.println("26. get employee constrains");
        System.out.println("27. get shift placements");
        System.out.println("28. delete supplier order");
        System.out.println("29. change password");
        System.out.println("30. add constraints");
        System.out.println("31. delete constraints");
        System.out.println("32. schedule driver");
        System.out.println("33. logout");
    }

    private static void addNewJob() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addNewJob((String) args[0]),
                f -> f.apply(help3("new Job Name")));
    }


    private static void getShiftPlacements() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getShiftPlacements(Parser.getDate(args[0]), ShiftTime.valueOf(args[1]), args[2]),
                f -> f.apply(help3("date(dd/mm/yyyy)", "shift time(Morning/Evening)", "a branch")));

    }



    private static void showForEachEmployeeHisJob() {
        Response<HashMap<Integer, String>> responseList = transportEmployeeService.showForEachEmployeeHisJob();
        Parser.printResponse(responseList, val -> "employee id | job:\n" + val.entrySet().stream().map(e -> e.getKey() + " | " + e.getValue() + "\n").collect(Collectors.joining()));
    }

    private static void getHistory() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getHistory(),
                f -> f.apply(help2()));
    }

    private static void showEmployees() {
        Parser.handleServiceStringListRequest(
                args -> transportEmployeeService.showEmployees(),
                f -> f.apply(help2()));

    }

    private static void getEmployeeConstrains() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getEmployeeConstrains(Integer.parseInt(args[0])),
                f -> f.apply(help3("employee-id")));
    }

    private static void updateIsShiftManager() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateIsShiftManager(Integer.parseInt(args[0]),
                        !args[1].equals("false")),
                f -> f.apply(help3("employee-id", "true/false")));

    }
    private static void scheduleDriver() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.scheduleDriver(),
                f -> f.apply(help2()));

    }

    private static void updateFirstName() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateFirstName(Integer.parseInt(args[0]),
                        args[1]),
                f -> f.apply(help3("employee-id", "new first-name")));
    }

    private static void updateLastName() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateLastName(Integer.parseInt(args[0]),
                        args[1]),
                f -> f.apply(help3("employee-id", "new last-name")));

    }

    private static void addNewSocialBenefits() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addNewSocialBenefits(Integer.parseInt(args[0]),
                        args[1]),
                f -> f.apply(help3("employee-id", "social-benefits")));
    }

    private static void addSocialBenefits() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addSocialBenefits(Integer.parseInt(args[0]),
                        args[1]),
                f -> f.apply(help3("employee-id", "social-benefits")));
    }

    private static void updateSalary() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateSalary(Integer.parseInt(args[0]),
                        Double.parseDouble(args[1])),
                f -> f.apply(help3("employee-id", "new-salary")));

    }


    private static void getNumberOfMorningShiftsStatistics() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getNumberOfMorningShiftsStatistics(args[0]),
                f -> f.apply(help3("branch address")));

    }

    private static void getNumberOfEveningShiftsStatistics() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getNumberOfEveningShiftsStatistics(args[0]),
                f -> f.apply(help3("branch address")));

    }

    private static void getNumberOfShiftsStatistics() {
        Parser.handleServiceValueRequest("",
                args -> transportEmployeeService.getNumberOfShiftsStatistics(args[0]),
                f -> f.apply(help3("branch address")));

    }

    private static void removeShiftPosition() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.removeShiftPosition(ShiftTime.valueOf(args[0]),
                        Parser.getDate(args[1]), args[2], args[3]),
                f -> f.apply(help3("shift time-(Morning/Evening)",
                        "date in the format dd/mm/yyyy","branch address", "job")));

    }

    private static void addShiftPosition() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addShiftPosition(ShiftTime.valueOf(args[0]), Parser.getDate(args[1]), args[2], args[3], Integer.parseInt(args[4])),
                f -> f.apply(help3("shift time-(Morning/Evening)", "date in the format dd/mm/yyyy","branch address","job name", "quantity")));

    }

    private static void setPosition() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.setPosition(ShiftTime.valueOf(args[0]),
                        Parser.getDate(args[1]), args[2], args[3], Integer.parseInt((String) args[4])),
                f -> f.apply(help3("shift time-(Morning/Evening)", "date in the format dd/mm/yyyy"
                        , "branch address", "job name(driver/ hr manager /transport manager)", "quantity")));
    }

    //  Morning,25/05/2022,branch1,1
    private static void addNewShift() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addNewShift(ShiftTime.valueOf(args[0]), Parser.getDate(args[1]), args[2], Integer.parseInt(args[3])),
                f -> f.apply(help3("shift time-(Morning/Evening)", "date in the format dd/mm/yyyy","branch address", "employee id")));
    }

    private static void updateShiftManager() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateShiftManager(ShiftTime.valueOf(args[0]), Parser.getDate(args[1]), args[2], Integer.parseInt(args[3])),
                f -> f.apply(help3("shift time-(Morning/Evening)", "date in the format dd/mm/yyyy","branch address", "employee id")));
    }

    private static void removeScheduling() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.removeScheduling(ShiftTime.valueOf(args[0]), Parser.getDate(args[1]), args[2], Integer.parseInt(args[3])),
                f -> f.apply(help3("shift time-(Morning/Evening)", "date in the format dd/mm/yyyy","branch address", "employee id")));

    }



    //Morning,25/05/2022,branch1,1,orderMan
    private static void addScheduling() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addScheduling(ShiftTime.valueOf(args[0]), Parser.getDate(args[1]), args[2], Integer.parseInt(args[3]), args[4]),
                f -> f.apply(help3("shift time-(Morning/Evening)",
                        "date in the format dd/mm/yyyy","branch address", "employee id", "job")));

    }



    private static void removeEmployee() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.removeEmployee(Integer.parseInt(args[0])),
                f -> f.apply(help3("employeeId which you would like to remove")));

    }

    private static void updateBankNumber() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateBankNumber(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                f -> f.apply(help3("employee Id", "new bank number")));

    }

    private static void updateAccountNumber() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateAccountNumber(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                f -> f.apply(help3("employee Id", "new account number")));

    }

    private static void updateBankBranch() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.updateBankBranch(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                f -> f.apply(help3("employee Id", "new Bank branch")));

    }

    private static void addNewEmployee() {
        Parser.handleServiceRequest("",
                args -> transportEmployeeService.addNewEmployee(args[0], args[1],
                        Parser.getDate(args[2]), Integer.parseInt(args[3]),
                        args[4], args[5].equals("yes"), Integer.parseInt(args[6]),
                        Integer.parseInt(args[7]), Integer.parseInt(args[8]), Double.parseDouble(args[9]),
                        args[10], args[11]
                ), f -> f.apply(help3("first name","last name","starting date in the format dd/mm/yyyy","id","job name","is a shift manager(yes/no)",
                        "bank number","account number","bank branch number", "salary", "social benefits",
                        "branch address (if there isn't enter general)")));

    }

    @Override
    public DeleteSupplierOrderFunctionality getService() {
        return transportEmployeeService;
    }
}
