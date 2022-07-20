package Obj;

import Backend.DataAccess.DAOs.StockDAOs.*;
import Backend.DataAccess.DAOs.SupplierDAOs.*;
import Backend.DataAccess.DAOs.TransportDAOs.PointDAO;
import Backend.ServiceLayer.Result.Response;
import Backend.ServiceLayer.Result.Result;
import PresentationLayer.EmployeTransportFrontend.ServiceFunction;
import PresentationLayer.EmployeTransportFrontend.ServiceFunctionHelp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Parser {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    private static final HashMap<String, Integer> getMonthNumber = new HashMap() {
        {
            put("Jan", 1);
            put("Feb", 2);
            put("Mar", 3);
            put("Apr", 4);
            put("May", 5);
            put("Jun", 6);
            put("Jul", 7);
            put("Aug", 8);
            put("Sep", 9);
            put("Oct", 10);
            put("Nov", 11);
            put("Dec", 12);
        }
    };

    private static final Scanner scanner = new Scanner(System.in);

    public static String getStrDate(Date date){
        return simpleDateFormat.format(date);
    }

    public static Date getDate(String date) {
        try {
            return simpleDateFormat2.parse(date);
        } catch (Exception e) {
            try {
                return simpleDateFormat.parse(date);
            }
            catch (Exception ex) {
                String[] in = date.split(" ");
                if (in.length > 2) {
                    try {
                        return simpleDateFormat.parse(in[2] + "-" + getMonthNumber.get(in[1]) + "-" + in[0]);
                    } catch (ParseException exp) {
                        try {
                            return simpleDateFormat.parse(in[2] + "-" + getMonthNumber.get(in[1]) + "-" + in[5]);
                        }
                        catch (Exception exc){
                            System.out.println("error occurred");
                        }
                    }
                }

                return new Date();
            }
        }
    }
    public static Date getComplexDate(String date) {
        try {
            return simpleDateFormat1.parse(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getStrInput(String msg) {
        try {
            System.out.println(msg);
            String input = scanner.nextLine();
            while (input.equals("")) {
                input = scanner.nextLine();
            }
            return input;
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getStrInput(msg);
        }
    }

    public static String getStrInput() {
        try {
            String input = scanner.nextLine();
            while (input.equals("")) {
                input = scanner.nextLine();
            }
            return input;
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getStrInput();
        }
    }

    public static int getIntInput(String msg) {
        try {
            return Integer.parseInt(getStrInput(msg));
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getIntInput(msg);
        }
    }

    public static double getDoubleInput(String msg) {
        try {
            return Double.parseDouble(getStrInput(msg));
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getDoubleInput(msg);
        }
    }

    public static Date getDateInput(String msg) {
        try {
            String input = getStrInput(msg);
            return simpleDateFormat.parse(input);
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getDateInput(msg);
        }
    }
    public static Date getComplexDateInput(String msg) {
        try {
            String input = getStrInput(msg);
            return simpleDateFormat1.parse(input);
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getComplexDateInput(msg);
        }
    }

    public static void deleteAllData() {
        new ProductsDAO().deleteAllRecords();
        new DiscountsDAO().deleteAllRecords();
        new CategoriesDAO().deleteAllRecords();
        new ItemsDAO().deleteAllRecords();
        new PurchasesDAO().deleteAllRecords();
        new LocationsDAO().deleteAllRecords();
        new BillOfQuantitiyDAO().deleteAllRecords();
        new ProductOrderDAO().deleteAllRecords();
        new SupplierAgreementDAO().deleteAllRecords();
        new SupplierCardDAO().deleteAllRecords();
        new SupplierConsistentScheduleDAO().deleteAllRecords();
        new SupplierContactsDAO().deleteAllRecords();
        new SupplierDAO().deleteAllRecords();
        new SupplierEmptyScheduleDAO().deleteAllRecords();
        new SupplierNotConsistentScheduleDAO().deleteAllRecords();
        new SupplierOrdersDAO().deleteAllRecords();
        new SupplierProductsDAO().deleteAllRecords();
    }


    public static <T> void printResult(Result<T> result, Function<T, String> ifValue) {
        if (result.errorOccurred()) {
            System.out.println(result.getError());
        } else {
            System.out.println(ifValue.apply(result.getValue()));
        }
    }
    public static <T> void printResult(Result<T> result) {
        printResult(result, Object::toString);
    }

    public static <T> void printResponse(Response<T> result, Function<T, String> ifValue) {
        if (result.isGotError()) {
            System.out.println(result.getError());
        } else if(result.getValue() == null) {
            System.out.println("action completed successfully");
        }
        else {
            System.out.println(ifValue.apply(result.getValue()));
        }
    }


    public static <T> List<T> getListInput(Supplier<T> make) {
        System.out.println("to stop inserting elements. write 0, to continue, write c");
        List<T> lst = new LinkedList<>();
        String userInput = "";
        while (!(getStrInput().equals("0"))) {
            lst.add(make.get());
        }
        return lst;
    }

    public static <T> String printList(List<T> lst) {
        return printList(lst, Object::toString);
    }

    public static <T> String printList(List<T> lst, Function<T, String> changeVal) {
        return lst.stream().map(t -> changeVal.apply(t) + "\n").collect(Collectors.joining());
    }

    public static void handleAction(Action a){
        if(a == null){
            printInvalidInput();
        }
        else{
            a.act();
        }
    }

    public static void handleServiceRequest(String instructions, ServiceFunctionHelp serviceFunctionHelp, ServiceFunction serviceFunction){
        System.out.println(instructions);
        try {
            Result<?> response = serviceFunction.apply(serviceFunctionHelp);
            printResult(response, v -> "success");
        } catch (Exception e) {
            printInvalidInput();
            System.out.println(e.getMessage());
        }
    }



    public static void handleServiceStringListRequest(ServiceFunctionHelp serviceFunctionHelp, ServiceFunction serviceFunction){
        try {
            Result<List<String>> response = serviceFunction.apply(serviceFunctionHelp);
            printResult(response, (List<String> lst) -> printList(lst));
        } catch (Exception e) {
            printInvalidInput();
        }
    }

    public static <T> void handleServiceTListRequest(String instructions, ServiceFunctionHelp serviceFunctionHelp, ServiceFunction serviceFunction){
        System.out.println(instructions);
        try {
            Result<List<T>> response = serviceFunction.apply(serviceFunctionHelp);
            printResult(response, (List<T> lst) -> printList(lst));

        } catch (Exception e) {
            printInvalidInput();
        }
    }

    public static <T> void handleServiceTListRequest(String instructions, ServiceFunctionHelp serviceFunctionHelp, ServiceFunction serviceFunction, Function<T, String> fp, Function<List<T>, String> doOnList){
        System.out.println(instructions);
        try {
            Result<List<T>> response = serviceFunction.apply(serviceFunctionHelp);
            printResult(response, (List<T> lst) -> {
                doOnList.apply(lst);
                return printList(lst, fp);
            });

        } catch (Exception e) {
            printInvalidInput();
        }
    }

    public static <T> void handleServiceValueRequest(String instructions, ServiceFunctionHelp serviceFunctionHelp, ServiceFunction serviceFunction){
        System.out.println(instructions);
        try {
            Result<T> response = serviceFunction.apply(serviceFunctionHelp);
            printResult(response, Object::toString);

        } catch (Exception e) {
            printInvalidInput();
        }
    }
    public static String[] help() {
        return Parser.getStrInput().split(",");
    }

    public static String[] help(String split) {
        return Parser.getStrInput().split(split);
    }
    public static String[] help1() {
        return new String[]{Parser.getStrInput()};
    }

    public static String[] help2() {
        return new String[]{};
    }
    public static String[] help3(String... args){
        String[] out = new String[args.length];
        for(int i = 0; i < args.length; i++){
            out[i] = Parser.getStrInput("please insert " + args[i].replaceAll(":", "") + ":");
        }
        return out;
        //return (String[]) Arrays.stream(args).map(arg -> Parser.getStrInput("please insert " + arg.replaceAll(":", "") + ":")).toArray();
    }
    public static String[] help3Me(String... args){
        String[] out = new String[args.length];
        for(int i = 0; i < args.length; i++){
            out[i] = Parser.getStrInput("please insert " + args[i] + ":");
        }
        return out;
        //return (String[]) Arrays.stream(args).map(arg -> Parser.getStrInput("please insert " + arg.replaceAll(":", "") + ":")).toArray();
    }

    public static void printInvalidInput(){
        System.out.println("invalid input");
    }

    public static int getIntInput(String msg, int lowerBound, int largerBound){
        try{
            int input = getIntInput(msg);
            if(input < lowerBound || input > largerBound){
                if(lowerBound > largerBound){
                    return 0;
                }
                throw new Exception("number not in range");
            }
            return input;
        }
        catch (Exception e){
            printInvalidInput();
            return getIntInput(msg, lowerBound, largerBound);
        }
    }

    public static String chooseFromList(String... options){
        if(options.length == 0)
            return "";
        AtomicInteger i = new AtomicInteger(1);
        Arrays.stream(options).forEach(o -> System.out.println(i.getAndIncrement() + ": "  + o));
        int option = getIntInput("[choose option]", 1, options.length);
        return options[option-1];
    }

    public static String chooseStoreStorage(){
        String[] options = new String[]{"STORE", "STORAGE"};
        return chooseFromList(options);
    }




}
