package PresentationLayer.JobsCLI;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StockKeeperService;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Product.SDiscount;
import Backend.ServiceLayer.ServiceObjects.Product.SItem;
import Backend.ServiceLayer.ServiceObjects.Product.SProduct;
import Backend.ServiceLayer.ServiceObjects.Product.SPurchase;
import Backend.ServiceLayer.ServiceObjects.Report.SCategory;
import Obj.Action;
import Obj.Parser;
import PresentationLayer.EmployeTransportFrontend.CLI;
import PresentationLayer.JobsCLI.functionalCLI.DeleteSupplierOrderCLI;
import PresentationLayer.JobsCLI.functionalCLI.InventoryManagement;

import PresentationLayer.JobsCLI.functionalCLI.SupplierCardManagement;

import PresentationLayer.JobsCLI.functionalCLI.SupplierOrders;

import java.util.*;

import static Obj.Parser.getDate;
import static PresentationLayer.EmployeTransportFrontend.CLI.logout;


public class CLIStockKeeper implements DeleteSupplierOrderCLI {
        /*
    available actions:
        manage stock
        manage supplier orders
        manage supplier cards
     */


    private final Scanner scanner;
    private final StockKeeperService service;

    private InventoryManagement inventoryManagement;
    private SupplierCardManagement supplierCardManagement;
    private SupplierOrders supplierOrders;
    private Map<String, Action> actionsMap = new HashMap<String, Action>(){
        {
            put("1", () -> inventoryManagement.run());
            put("2", () -> supplierOrders.run());
            put("3", () -> supplierCardManagement.run());
            put("4", CLIStockKeeper.this::deleteSupplierOrder);
            put("5", CLI::changePassword);
            put("6", CLI::addConstraints);
            put("7", CLI::deleteConstraints);
            put("8", CLI::logoutAction);
        }
    };
    public CLIStockKeeper(StockKeeperService service, Scanner scanner){
        this.scanner = scanner;
        this.service = service;
    }





    private String getStockKeeperMenu(){
        return "choose the wanted functionality:\n" +
                "1 - inventory management\n" +
                "2 - supplier orders\n" +
                "3 - supplier card management\n" +
                "4 - delete supplier order\n" +
                "5 - change password\n" +
                "6 - add constraints\n" +
                "7 - delete constrains\n" +
                "8 - logout";
    }

    public void start() {
        System.out.println("Welcome Stock Keeper!");
        String userInput;
        inventoryManagement = new InventoryManagement(service,scanner);
        supplierCardManagement = new SupplierCardManagement(service, scanner);
        supplierOrders = new SupplierOrders(service, scanner);
        while (!logout) {
            userInput = Parser.getStrInput(getStockKeeperMenu());
            Parser.handleAction(actionsMap.get(userInput));
        }

    }
    private static String job = "Supply Manager";
    private static String purchasesStr = "0,21,35,100,2,Rosa,20-3-2018;1,49,48,100,2,Karyn,27-9-2002;2,30,39,100,2,David,11-10-2001;3,25,21,100,2,Nichole,2-11-2011;4,36,29,100,2,James,9-2-2005;5,20,33,100,2,Loreta,23-4-2016;6,33,30,100,2,Joe,1-9-2009;7,39,43,100,2,Jimmy,19-4-2006;8,43,44,100,2,Rhoda,5-4-2009;9,39,38,100,2,Stacey,21-5-2016;10,19,41,100,2,Phyllis,11-5-2021;11,48,49,100,2,Donna,9-2-2013;12,15,25,100,2,Frances,26-4-2000;13,21,8,100,2,Francisca,16-7-2010;14,15,15,100,2,Monica,11-8-2011;15,28,3,100,2,Young,4-11-2004;16,20,47,100,2,Joseph,4-2-2009;17,37,23,100,2,Betty,8-11-2010;18,14,42,100,2,Shanta,9-5-2012;19,34,48,100,2,Edward,16-1-2017;0,16,39,100,2,Jeanine,27-4-2002;1,25,48,100,2,Thomas,23-4-2008;2,4,17,100,2,Danny,9-3-2013;3,14,49,100,2,Craig,24-6-2020;4,10,14,100,2,James,7-9-2007;5,28,1,100,2,Mark,1-2-2004;6,36,46,100,2,Mildred,21-5-2020;7,38,44,100,2,Richard,22-8-2011;8,17,29,100,2,Jimmie,12-4-2015;9,35,16,100,2,Manuel,16-8-2013;10,1,28,100,2,Norman,9-11-2009;11,21,16,100,2,Paul,28-2-2014;12,43,13,100,2,Tracy,1-6-2017;13,35,49,100,2,Jessica,16-2-2016;14,39,35,100,2,Wendy,5-11-2009;15,13,11,100,2,Valerie,14-11-2014;16,25,39,100,2,Michael,3-1-2000;17,11,40,100,2,Doris,11-6-2020;18,9,20,100,2,Ronald,19-4-2004;19,47,34,100,2,Lee,26-5-2018;0,40,2,100,2,Mark,8-1-2019;1,22,24,100,2,Felecia,28-5-2010;2,29,30,100,2,Bennie,10-10-2019;3,10,11,100,2,James,12-11-2000;4,6,49,100,2,Ruth,29-4-2002;5,34,15,100,2,Frank,26-9-2018;6,21,46,100,2,Helene,26-11-2011;7,31,24,100,2,Vincent,4-10-2009;8,15,20,100,2,Amy,5-1-2009;9,47,12,100,2,Robert,24-6-2002";
    private static String itemsStr = "0,scranton,STORE,28,20-10-2010;1,NY,STORAGE,8,7-5-2019;2,Tel-Aviv,STORE,28,21-9-2011;3,Haifa,STORAGE,1,12-9-2006;4,Jeruslem,STORE,2,15-9-2021;5,Beer-Sheva,STORAGE,7,24-1-2010;6,scranton,STORE,8,9-10-2016;7,NY,STORAGE,11,17-10-2014;8,Tel-Aviv,STORE,47,29-2-2015;9,Haifa,STORAGE,13,28-9-2010;10,Jeruslem,STORE,49,9-3-2013;11,Beer-Sheva,STORAGE,5,21-10-2003;12,scranton,STORE,49,15-4-2015;13,NY,STORAGE,36,8-3-2018;14,Tel-Aviv,STORE,37,6-3-2007;15,Haifa,STORAGE,31,19-10-2014;16,Jeruslem,STORE,31,3-9-2018;17,Beer-Sheva,STORAGE,12,27-2-2020;18,scranton,STORE,27,13-7-2009;19,NY,STORAGE,22,29-8-2001;0,Tel-Aviv,STORE,42,14-10-2020;1,Haifa,STORAGE,45,25-10-2018;2,Jeruslem,STORE,45,10-8-2015;3,Beer-Sheva,STORAGE,15,1-3-2000;4,scranton,STORE,34,7-2-2013;5,NY,STORAGE,46,5-6-2006;6,Tel-Aviv,STORE,2,15-4-2008;7,Haifa,STORAGE,5,6-6-2003;8,Jeruslem,STORE,35,4-7-2001;9,Beer-Sheva,STORAGE,29,25-7-2013;10,scranton,STORE,44,11-3-2021;11,NY,STORAGE,45,25-10-2016;12,Tel-Aviv,STORE,12,4-11-2012;13,Haifa,STORAGE,35,29-6-2008;14,Jeruslem,STORE,9,13-1-2000;15,Beer-Sheva,STORAGE,3,10-8-2017;16,scranton,STORE,9,6-8-2019;17,NY,STORAGE,44,13-1-2002;18,Tel-Aviv,STORE,12,7-1-2017;19,Haifa,STORAGE,27,17-10-2006;0,Jeruslem,STORE,39,2-6-2011;1,Beer-Sheva,STORAGE,31,14-3-2013;2,scranton,STORE,27,26-6-2017;3,NY,STORAGE,7,20-7-2000;4,Tel-Aviv,STORE,30,24-9-2020;5,Haifa,STORAGE,25,14-11-2001;6,Jeruslem,STORE,4,3-1-2011;7,Beer-Sheva,STORAGE,39,12-10-2021;8,scranton,STORE,40,24-4-2018;9,NY,STORAGE,13,13-8-2017;10,Tel-Aviv,STORE,12,6-4-2004;11,Haifa,STORAGE,30,13-6-2012;12,Jeruslem,STORE,49,7-5-2000;13,Beer-Sheva,STORAGE,49,21-2-2011;14,scranton,STORE,20,13-6-2003;15,NY,STORAGE,43,13-1-2001;16,Tel-Aviv,STORE,43,29-2-2018;17,Haifa,STORAGE,40,15-5-2013;18,Jeruslem,STORE,37,3-10-2007;19,Beer-Sheva,STORAGE,42,13-3-2016;0,scranton,STORE,43,14-10-2017;1,NY,STORAGE,37,15-11-2001;2,Tel-Aviv,STORE,24,27-11-2002;3,Haifa,STORAGE,23,8-2-2016;4,Jeruslem,STORE,32,17-4-2002;5,Beer-Sheva,STORAGE,25,1-6-2008;6,scranton,STORE,28,10-1-2015;7,NY,STORAGE,10,29-2-2010;8,Tel-Aviv,STORE,49,19-5-2008;9,Haifa,STORAGE,7,14-2-2009;10,Jeruslem,STORE,6,19-6-2010;11,Beer-Sheva,STORAGE,41,29-7-2010;12,scranton,STORE,35,16-5-2000;13,NY,STORAGE,45,29-9-2018;14,Tel-Aviv,STORE,25,9-11-2000;15,Haifa,STORAGE,33,16-8-2003;16,Jeruslem,STORE,22,18-4-2020;17,Beer-Sheva,STORAGE,7,9-3-2021;18,scranton,STORE,27,23-4-2013;19,NY,STORAGE,39,22-2-2020;0,Tel-Aviv,STORE,23,2-3-2018;1,Haifa,STORAGE,29,13-9-2019;2,Jeruslem,STORE,20,3-6-2006;3,Beer-Sheva,STORAGE,3,16-11-2020;4,scranton,STORE,15,7-8-2007;5,NY,STORAGE,49,3-10-2000;6,Tel-Aviv,STORE,40,21-9-2003;7,Haifa,STORAGE,47,19-9-2017;8,Jeruslem,STORE,25,6-11-2002;9,Beer-Sheva,STORAGE,34,21-10-2003;10,scranton,STORE,48,23-4-2006;11,NY,STORAGE,38,21-5-2018;12,Tel-Aviv,STORE,46,24-3-2005;13,Haifa,STORAGE,1,5-6-2016;14,Jeruslem,STORE,37,13-9-2007;15,Beer-Sheva,STORAGE,47,8-8-2017;16,scranton,STORE,35,6-10-2015;17,NY,STORAGE,40,8-2-2014;18,Tel-Aviv,STORE,15,13-9-2001;19,Haifa,STORAGE,5,29-8-2017";
    private static String productsStr = "0,Albert,Veronica,16,5;1,James,Peggy,32,1;2,Lisa,Sharon,24,2;3,Crystal,Steve,42,6;4,Albert,Michael,34,1;5,Diane,Daniel,5,6;6,Stephen,David,32,6;7,John,Ronald,25,2;8,Henry,Nora,31,2;9,Ramon,William,47,4;10,Doug,Gerard,4,4;11,Stephen,Cory,49,3;12,Jim,Abraham,7,5;13,Paul,Lisa,49,1;14,Rocco,Ronald,34,5;15,Bernard,Lourdes,33,6;16,Adrian,Eric,8,3;17,Dan,Rudy,45,2;18,Edward,Patricia,17,3;19,Lucas,Sean,4,1";
    private static String discountsStr = "0,100,5-9-2006,23-4-2014;1,100,3-10-2003,13-4-2021;2,100,13-10-2002,8-2-2015;3,100,2-4-2008,20-11-2014;4,100,18-8-2008,24-4-2022;5,100,13-8-2003,16-7-2020;6,100,14-1-2001,8-7-2019;7,100,29-10-2003,3-6-2015;8,100,28-11-2000,16-7-2022;9,100,17-7-2010,25-6-2021;10,100,28-7-2011,1-8-2020;11,100,9-3-2003,12-7-2016;12,100,5-5-2007,24-5-2013;13,100,9-8-2010,8-2-2022;14,100,25-5-2008,25-1-2022;15,100,14-8-2010,12-10-2016;16,100,11-3-2009,1-2-2020;17,100,1-3-2005,9-10-2016;18,100,19-1-2001,22-4-2018;19,100,26-4-2011,29-11-2013;0,100,28-4-2002,15-5-2013;1,100,10-11-2000,26-4-2018;2,100,2-2-2009,19-11-2017;3,100,21-8-2001,13-6-2021;4,100,14-8-2008,6-2-2021;5,100,22-2-2006,13-3-2019;6,100,4-4-2001,1-11-2018;7,100,27-8-2009,16-9-2016;8,100,11-6-2010,12-1-2018;9,100,18-3-2004,13-7-2016;10,100,26-6-2001,9-1-2016;11,100,16-10-2000,17-9-2019;12,100,12-3-2001,2-2-2016;13,100,18-5-2007,10-3-2014;14,100,1-2-2001,2-9-2017;15,100,23-6-2000,25-6-2017;16,100,11-3-2011,14-6-2021;17,100,22-5-2002,20-4-2015;18,100,29-5-2001,13-5-2013;19,100,2-7-2010,11-1-2018;0,100,26-3-2008,5-8-2016;1,100,4-7-2002,25-2-2013;2,100,19-6-2010,15-1-2017;3,100,26-4-2005,25-6-2020;4,100,12-11-2002,1-8-2018;5,100,21-2-2000,23-2-2022;6,100,14-5-2007,22-11-2015;7,100,23-6-2010,18-4-2022;8,100,15-9-2001,2-7-2021;9,100,17-2-2004,7-4-2021";
    private static String categoriesStr = "Maria,Matthew,Mark;Gwendolyn,Dustin,Allison;Robert,Peggy,Michael;Rhonda,Alan,Margarita;Dora,Tom,Nettie;Lee,Troy,Jennifer;Christopher,Brian,Sharilyn;Barbara,Matthew,Jacob;Oscar,Daniel,Timothy;Rhea,Ian,Leeann";


    public void uploadSystemWithData(){
        String path = "C:\\Users\\achiy\\ADSS-Projects\\superlee\\";
        //path = "";
        String[] category = categoriesStr.split(";");//readFile(path + "categories.txt").split(";");
        List<List<String>> categories = new LinkedList<>();
        for(String st : category){
            String[] str = st.split(",");
            List<String> route = new LinkedList<>();
            for(int i = 0;i < str.length;i++) {
                Result<SCategory> result = service.addCategory(str[i], route);
                if(result.errorOccurred()){
                    System.out.println(result.getError());
                    return;
                }
                route.add(str[i]);
            }
            categories.add(route);
        }
        String[] products = productsStr.split(";");//readFile(path + "products.txt").split(";");
        for(int i = 0;i < products.length;i++) {
            String[] st = products[i].split(",");
            Result<SProduct> result = service.addProduct(st[0], st[1], st[2], Double.parseDouble(st[3]), categories.get(i % categories.size()), Integer.parseInt(st[4]));
            if(result.errorOccurred()){
                System.out.println(result.getError());
                return;
            }
        }
        String[] items = itemsStr.split(";");//readFile(path + "Items.txt").split(";");
        for(int i = 0;i < items.length;i++) {
            String[] st = items[i].split(",");
            Result<Void> resultV = service.addItems(st[0], st[1], st[2], Integer.parseInt(st[3]), getDate(st[4]),1);
            if(resultV.errorOccurred()){
                System.out.println(resultV.getError());
                return;
            }
            if(i % 2 == 0) {
                Result<SItem> result = service.reportItemAsFlawed(st[0], st[1], st[2], Integer.parseInt(st[3]));
                if(result.errorOccurred()){
                    System.out.println(result.getError());
                    return;
                }
            }
        }
        String[] purchases = purchasesStr.split(";");//readFile(path + "purchases.txt").split(";");
        for(int i = 0;i < purchases.length;i++) {
            String[] st = purchases[i].split(",");
            Result<SPurchase> result = service.addProductPurchase(st[0], Double.parseDouble(st[1]), Double.parseDouble(st[2]), Double.parseDouble(st[3]), Integer.parseInt(st[4]), st[5], getDate(st[6]));
            if(result.errorOccurred()){
                System.out.println(result.getError());
                return;
            }
        }
        String[] discounts = discountsStr.split(";");//readFile(path + "discounts.txt").split(";");
        for(int i = 0;i < discounts.length;i++) {
            String[] st = discounts[i].split(",");
            Result<SDiscount> result = service.addProductDiscount(st[0], Double.parseDouble(st[1]), getDate(st[2]), getDate(st[3]));
            if(result.errorOccurred()){
                System.out.println(result.getError());
                return;
            }
            result = service.addCategoryDiscount(categories.get(i%categories.size()), Double.parseDouble(st[1]), getDate(st[2]), getDate(st[3]));
            if(result.errorOccurred()){
                System.out.println(result.getError());
                return;
            }
        }
        System.out.println("upload finished");
    }

    public static void main(String[] args){
        Parser.deleteAllData();
        CLIStockKeeper cliStockKeeper = new CLIStockKeeper(Starter.getInstance().getStockKeeperService(), new Scanner(System.in));
        cliStockKeeper.uploadSystemWithData();
        System.exit(0);
    }

    @Override
    public DeleteSupplierOrderFunctionality getService() {
        return service;
    }
}
