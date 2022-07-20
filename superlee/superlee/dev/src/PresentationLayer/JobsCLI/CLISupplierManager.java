package PresentationLayer.JobsCLI;
import Backend.ServiceLayer.ServiceObjects.Supplier.*;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Facades.ServicePerJob.Supplier.SupplierManagerService;
import Obj.Action;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static Obj.Parser.*;

import static PresentationLayer.EmployeTransportFrontend.CLI.logout;

public class CLISupplierManager {
    /*
available actions:
   manage supplier cards
   manage supplier agreements
   add & remove suppliers
*/
    private final Scanner scanner;
    private final SupplierManagerService service;
    private Map<String, Action> actionsMap = new HashMap<String, Action>();

    public CLISupplierManager(SupplierManagerService service, Scanner scanner){
        this.scanner = scanner;
        this.service = service;
    }
    private String getSupplierManagerMenu(){
        return  "please enter the command index:\n" +
                "1 : add supplier\n" +
                "2 : remove supplier\n" +
                "3 : manage supplier cards\n" +
                "4 : manage supplier agreements\n" +
                "5 : get all the suppliers that supply a given product\n" +
                "6 : change password\n" +
                "7 : add constraints\n" +
                "8 : delete constrains\n" +
                "9 : logout";
    }
    public void start() {
        System.out.println("Welcome Supplier Manager!");
        while(!logout){
            actionsMap = new HashMap<String, Action>(){
                {
                    put("1", () -> addSupplier());
                    put("2", () -> removeSupplier());
                    put("3", () -> manageSupplierCards());
                    put("4", () -> manageSupplierAgreements());
                    put("5", () -> getSuppliersCnOfProduct());
                    put("6", CLI::changePassword);
                    put("7", CLI::addConstraints);
                    put("8", CLI::deleteConstraints);
                    put("9", CLI::logoutAction);
                }
            };
            String input = Parser.getStrInput(getSupplierManagerMenu());
            Parser.handleAction(actionsMap.get(input));
        }
    }
    private void manageSupplierCards(){
        String supplierCn = Parser.getStrInput("enter the supplier cn that you want to manage");
        String input = Parser.getStrInput("[choose action]\n1 : get supplier card info\n2 : edit supplier card info");
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> getSupplierCardInfo(supplierCn));
                put("2", () -> updateSupplierCard(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));
    }
    private void manageSupplierAgreements(){
        String supplierCn = Parser.getStrInput("enter the supplier cn that you want to manage");
        String input = Parser.getStrInput("[choose action]\n1 : manage supplier schedule\n2 : manage supplier products");
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> manageSupplierScheduleInfo(supplierCn));
                put("2", () -> manageSupplierProducts(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));
    }
    private void manageSupplierProducts(String supplierCn) {
        System.out.println("[choose action]");
        System.out.println("1 : add a product to supplier");
        System.out.println("2 : remove a product from supplier");
        System.out.println("--------------------");
        System.out.println("3 : get all supplier products");
        System.out.println("4 : get supplier product info");
        System.out.println("5 : get supplier product catalog number");
        System.out.println("6 : get supplier product price");
        System.out.println("7 : get supplier product price for quantity");
        System.out.println("8 : get supplier product bill of quantities info");
        System.out.println("--------------------");
        System.out.println("9 : edit supplier product catalog num");
        System.out.println("10 : edit supplier product name");
        System.out.println("11 : edit supplier product price");
        System.out.println("12 : add supplier product bill of quantities range");
        String input = Parser.getStrInput("13 : remove supplier product bill of quantities range");
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> addSupplierProduct(supplierCn));
                put("2", () -> removeSupplierProduct(supplierCn));
                put("3", () -> getAllSupplierProducts(supplierCn));
                put("4", () -> getAllProductInfo(supplierCn));
                put("5", () -> getSupplierProductCatalogNum(supplierCn));
                put("6", () -> getSupplierProductPrice(supplierCn));
                put("7", () -> getSupplierProductPriceForQuantity(supplierCn));
                put("8", () -> getBillOfQuantityForAProduct(supplierCn));
                put("9", () -> editSupplierProductCatalogNum(supplierCn));
                put("10", () -> editSupplierProductName(supplierCn));
                put("11", () -> editSupplierProductPrice(supplierCn));
                put("12", () -> addSupplierProductBillOfQuantitiesRange(supplierCn));
                put("13", () -> removeSupplierProductBillOfQuantitiesRange(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));

    }
    private void manageSupplierScheduleInfo(String supplierCn){
        System.out.println("[choose action]");
        System.out.println("1 : get supplying days");
        System.out.println("--------------------");
        System.out.println("2 : add supplying supplying day");
        System.out.println("3 : remove a consistent supplying day");
        System.out.println("--------------------");
        System.out.println("4 : define supplier not consistent");
        System.out.println("5 : define consistent supplying days");
        String input = Parser.getStrInput();
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> getSupplyingDays(supplierCn));
                put("2", () -> addSupplyingDay(supplierCn));
                put("3", () -> removeSupplyingDay(supplierCn));
                put("4", () -> defineSupplyingDaysNotConsistent(supplierCn));
                put("5", () -> defineSupplyingDays(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));


    }
    private void getSupplierCardInfo(String supplierCn) {
        System.out.println("[choose action]");
        System.out.println("0: get all supplier card info");
        System.out.println("1 : get name");
        System.out.println("2 : get bank account number");
        System.out.println("3 : get payment method");
        System.out.println("4 : get payment frequency");
        System.out.println("5 : get contact");
        String input = Parser.getStrInput("6 : get supplier contact list");
        actionsMap = new HashMap<String, Action>(){
            {
                put("0", () -> getAllSupplierCardInfo(supplierCn));
                put("1", () -> getSupplierName(supplierCn));
                put("2", () -> getSupplierBankAccountNum(supplierCn));
                put("3", () -> getSupplierPaymentMethod(supplierCn));
                put("4", () -> getSupplierPaymentFrequency(supplierCn));
                put("5", () -> getSupplierContact(supplierCn));
                put("6", () -> getSupplierContactList(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));

    }
    private void getAllSupplierCardInfo(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getAllSupplierInfo(supplierCn),
                f -> f.apply(help2()));
    }
    private void getAllProductInfo(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getAllSupplierProductInfo(supplierCn, args[0]),
                f -> f.apply(help3("product name")));
    }
    private void updateSupplierCard(String supplierCn) {
        System.out.println("[choose action]");
        System.out.println("1 : update cn");
        System.out.println("2 : update bankAccountNum");
        System.out.println("3 : update paymentMethod");
        System.out.println("4 : update paymentFrequency");
        System.out.println("5 : update contacts");
        System.out.println("6 : update supplier name");
        String updateInput = Parser.getStrInput();
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> editSupplierCn(supplierCn));
                put("2", () -> editSupplierBankAccountNum(supplierCn));
                put("3", () -> editSupplierPaymentMethod(supplierCn));
                put("4", () -> editSupplierPaymentFrequency(supplierCn));
                put("5", () -> editSupplierContacts(supplierCn));
                put("6", () -> editSupplierName(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(updateInput));

    }
    private void editSupplierContacts(String supplierCn) {
        System.out.println("[choose action]");
        System.out.println("1 : add contact");
        System.out.println("2 : remove contact");
        System.out.println("3 : update contact name");
        System.out.println("4 : update contact phone number");
        System.out.println("5 : update contact email");
        String input = Parser.getStrInput();
        actionsMap = new HashMap<String, Action>(){
            {
                put("1", () -> addSupplierContact(supplierCn));
                put("2", () -> removeSupplierContact(supplierCn));
                put("3", () -> editSupplierContactName(supplierCn));
                put("4", () -> editSupplierContactPhoneNum(supplierCn));
                put("5", () -> editSupplierContactEmail(supplierCn));
            }
        };
        Parser.handleAction(actionsMap.get(input));
    }
    private void addSupplierContact(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.addSupplierContact(supplierCn, args[0], args[1], args[2]),
                f -> f.apply(help3("contact phone number", "contact's name", "contact's email")));
    }
    private void removeSupplierContact(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.removeSupplierContact(supplierCn, args[0]),
                f -> f.apply(help3("contact phone number")));
    }
    private void editSupplierContactPhoneNum(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierContactNumber(supplierCn, args[0], args[1]),
                f -> f.apply(help3("contact current phone number", "new contact phone number")));
    }
    private void editSupplierContactName(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierContactName(supplierCn, args[0], args[1]),
                f -> f.apply(help3("contact current phone number", "contact new name")));
    }
    private void editSupplierContactEmail(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierContactEmail(supplierCn, args[0], args[1]),
                f -> f.apply(help3("contact current phone number", "new contact email")));
    }
    private void removeSupplier() {
        try {
            String input = Parser.getStrInput("enter cn");
            Result<LinkedList<ServiceOrder>> result = service.removeSupplier(input);
            Parser.printResult(result, (res) -> "manage to delete those orders: " + Parser.printList(res));
        }catch (Exception e){
            printInvalidInput();
        }
    }
    private void addSupplier() {
        AtomicReference<Result<Void>> result = new AtomicReference<>(null);
        Parser.handleServiceValueRequest("",
                args -> {
                    result.set(service.addSupplier(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]));
                    return result.get();
                },
                f -> f.apply(help3("cn", "supplierName", "bankAccountNum"
                        , "paymentFrequency", "paymentMethod", "contactEmail",
                        "contactName", "contactPhoneNumber", "address")));
        if (!result.get().errorOccurred()) {
            System.out.println("the supplier card is automatically added to the system, as well as" + "\n" +
                    " a default agreement which we can add details to");
        }
    }
    private void editSupplierCn(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierCn(supplierCn, args[0]),
                f -> f.apply(help3("new supplier cn")));
    }
    private void editSupplierName(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierName(supplierCn, args[0]),
                f -> f.apply(help3("new supplier name")));
    }
    private void editSupplierBankAccountNum(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierBankAccountNum(supplierCn, args[0]),
                f -> f.apply(help3("new bank account number")));
    }
    private void editSupplierPaymentMethod(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierPaymentMethod(supplierCn, args[0]),
                f -> f.apply(help3("new payment method")));
    }
    private void editSupplierPaymentFrequency(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierPaymentFrequency(supplierCn, args[0]),
                f -> f.apply(help3("new payment frequency")));
    }
    private void defineSupplyingDaysNotConsistent(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.defineSupplyingNotConsistent(supplierCn, Integer.parseInt(args[0])),
                f -> f.apply(help3("shipment time")));
    }
    private void defineSupplyingDays(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> {
                    List<String> daysListAsString = Parser.getListInput(() -> Parser.getStrInput("the day you want to add to the supplying days"));
                    LinkedList<Integer> daysList = new LinkedList<>();
                    daysListAsString.forEach(s -> daysList.add(Integer.parseInt(s)));
                    return service.defineSupplyingDays(supplierCn, daysList);
                },
                f -> f.apply(help2()));
    }
    private void addSupplyingDay(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.addSupplyingDay(supplierCn, Integer.parseInt(args[0])),
                f -> f.apply(help3("the day you want to add")));
    }
    private void removeSupplyingDay(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.removeSupplyingDay(supplierCn, Integer.parseInt(args[0])),
                f -> f.apply(help3("the day you want to remove")));
    }
    private void addSupplierProduct(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.addSupplierProduct(supplierCn, args[0], args[1], Double.parseDouble(args[2])),
                f -> f.apply(help3("product name", "product catalog number", "product price")));
    }
    private void removeSupplierProduct(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.removeSupplierProduct(supplierCn, args[0]),
                f -> f.apply(help3("product name")));
    }
    private void editSupplierProductCatalogNum(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierProductCatalogNum(supplierCn, args[0], args[1]),
                f -> f.apply(help3("product name", "new catalog number")));
    }
    private void editSupplierProductName(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierProductName(supplierCn, args[0], args[1]),
                f -> f.apply(help3("old product name", "new product name")));
    }
    private void editSupplierProductPrice(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.editSupplierProductPrice(supplierCn, args[0], Double.parseDouble(args[1])),
                f -> f.apply(help3("product name", "new product price")));
    }
    private void addSupplierProductBillOfQuantitiesRange(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.addSupplierProductBillOfQuantitiesRange(supplierCn, args[0],
                        Integer.parseInt(args[1]), Double.parseDouble(args[2])),
                f -> f.apply(help3("product name", "amount of items needed for the discount (start of range)", "discount percentage as double")));
    }
    private void removeSupplierProductBillOfQuantitiesRange(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.removeSupplierProductBillOfQuantitiesRange(supplierCn, args[0],
                        Integer.parseInt(args[1])),
                f -> f.apply(help3("product name", "amount of items needed for the discount (start of range) that you are willing to remove")));
    }
    private void getAllSupplierProducts(String supplierCn) {
        try {
            Result<LinkedList<ServiceSupplierProduct>> result = service.getSupplierProducts(supplierCn);
            Parser.printResult(result, res -> printList(res));
        }catch (Exception e){
            printInvalidInput();
        }
    }
    private void getSupplierProductCatalogNum(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierProductCatalogNum(supplierCn, args[0]),
                f -> f.apply(help3("product name")));
    }
    private void getSupplierProductPrice(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierProductPrice(supplierCn, args[0]),
                f -> f.apply(help3("product name")));
    }
    private void getSupplierProductPriceForQuantity(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierProductPriceForQuantity(supplierCn, args[0], Integer.parseInt(args[1])),
                f -> f.apply(help3("product name", "quantity")));
    }
    private void getSuppliersCnOfProduct() {
        Parser.handleServiceValueRequest("",
                args -> service.getSuppliersCnOfProduct(args[0]),
                f -> f.apply(help3("product name")));
    }
    private void getBillOfQuantityForAProduct(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getBillOfQuantityForAProduct(supplierCn, args[0]),
                f -> f.apply(help3("product name")));
    }
    private void getSupplyingDays(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierSupplyingDays(supplierCn),
                f -> f.apply(help2()));
    }
    private void getSupplierName(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierName(supplierCn),
                f -> f.apply(help2()));
    }
    private void getSupplierBankAccountNum(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierBankAccountNumber(supplierCn),
                f -> f.apply(help2()));
    }
    private void getSupplierPaymentMethod(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierPaymentMethod(supplierCn),
                f -> f.apply(help2()));
    }
    private void getSupplierPaymentFrequency(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierPaymentFrequency(supplierCn),
                f -> f.apply(help2()));
    }
    private void getSupplierContact(String supplierCn) {
        Parser.handleServiceValueRequest("",
                args -> service.getSupplierContact(supplierCn, args[0]),
                f -> f.apply(help3("Contact phone number")));
    }
    private void getSupplierContactList(String supplierCn) {
        try {
            Result<LinkedList<ServiceContact>> result = service.getSupplierContactList(supplierCn);
            Parser.printResult(result, res -> printList(res));
        } catch (Exception e) {
            Parser.printInvalidInput();
        }
    }
}
