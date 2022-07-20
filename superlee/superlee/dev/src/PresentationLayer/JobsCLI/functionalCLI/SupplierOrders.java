package PresentationLayer.JobsCLI.functionalCLI;

import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.StockFunctionality;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.SupplierOrdersFunctionality;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;
import Obj.Action;
import Obj.Pair;
import Obj.Parser;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SupplierOrders {



    private final SupplierOrdersFunctionality service;
    private final Scanner scanner;

    private Map<String, Action> actionsMap = new HashMap<String, Action>(){
        {
            put("1", SupplierOrders.this::addOrder);
            put("2", SupplierOrders.this::getSupplierOrders);
            put("3", SupplierOrders.this::addOrderBestByDemand);
        }
    };
    public SupplierOrders(SupplierOrdersFunctionality service, Scanner scanner){
        this.service = service;
        this.scanner = scanner;
    }

    private void addOrder(){
        String cn = Parser.getStrInput("insert supplier cn:");
        String pn = Parser.getStrInput("insert supplier phone number:");
        System.out.println("insert products quantities:");
        LinkedList<Pair<String, Integer>> p2o = (LinkedList<Pair<String, Integer>>) Parser.getListInput(() -> {
            String pr = Parser.getStrInput("insert product name:");
            int q = Parser.getIntInput("insert product quantity:");
            return new Pair<>(pr, q);
        });
        Result<ServiceOrder> result = service.addOrder(cn, pn, p2o);
        Parser.printResult(result, (res) -> "added " + res.toString());
    }

    private void getSupplierOrders(){
        String cn = Parser.getStrInput("insert supplier cn:");
        Result<LinkedList<ServiceOrder>> result = service.getSupplierOrders(cn);
        Parser.printResult(result, (res) -> Parser.printList(res));
    }

    private void addOrderBestByDemand(){
        String pr = Parser.getStrInput("insert product name:");
        int d = Parser.getIntInput("insert product demand:");
        Result<ServiceOrder> result = service.addOrder_bestByDemand(pr, d);
        Parser.printResult(result, (res) -> "added " + res.toString());
    }


    private void printMenu(){
        System.out.println("[choose action]\n0 - exit\n1 - add order\n2 - get supplier orders\n3 - add best order by demand\n");
    }







    public void run(){
        String userInput = "";
        printMenu();
        while(!(userInput = scanner.nextLine()).equals("0")){
            Parser.handleAction(actionsMap.get(userInput));
            printMenu();
        }
    }
}
