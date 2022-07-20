package PresentationLayer.EmployeTransportFrontend;

import Backend.DataAccess.DATA_BASE.DataBaseConnection;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.Services.TransportEmployeeService;

import java.util.Scanner;

public class Program {
    private static final String defaultPassword = "abc111";

    public static void main(String[] args) {

        String path = args.length == 1 ? args[0] : null;
        DataBaseConnection.setConnection(path);
        CLI cli = Starter.getInstance().getCLI();
        while (!CLI.SHUT_DOWN) {
            cli.loginScreen();
            if (!CLI.SHUT_DOWN & !CLI.logout) {
                System.out.println("You are in");
                cli.screenPerJob(CLI.currentJob);
            }
        }

        System.out.println("The system is shutting down...");
        CLI.shutdownWait();
        System.exit(0);
    }

    /*public static void initObjects(Service service) {
     *//*
     * add new driver
     * *//*
        service.addNewEmployee("a","a",Date.valueOf("12/05/2022")  ,
                1, "driver", false,131322,
                321321,1,123321,"alllll");
        service.login(1, defaultPassword, "driver");
        service.updateLicense("A");
        service.changePassword(defaultPassword,"1");
        service.logout(1);

        *//*
     * Add new Transport Manager
     * *//*
        service.addNewEmployee("b","b",Date.valueOf("13/05/2022")  ,
                2, "transport manager", true,
                131322,321,1,696969669,"alllll");
        service.login(2,defaultPassword,"transport manager");
        service.insertBranch("kadima herzog 27", "054-7492353", "Shaun Shuster", "HA_SHARON");
        service.insertBranch("Zoran hayilan 15", "052-4375267", "Tomer Ofek", "HA_MERKAZ");
        service.insertBranch("kadima sdf", "054-2343242434", "haim", "HA_DAROM");
        service.insertBranch("kadima asdfa", "054-343434", "shalom", "HA_GALIL");
        service.insertSupplier("kadima asdff", "054-2343241874", "izik", "HA_NEGEV");
        service.insertSupplier("kadima asdff", "054-234", "izik asd", "HA_NEGEV");
        service.insertSupplier("kadima asdff", "054-223423434", "izi ddk", "HA_NEGEV");

        service.addTruck(1, "mercedes", 1000, 2000, LicenseService.A);
        service.addTruck(2, "BMW", 1250, 3500, LicenseService.B);
        service.logout(2);
       *//* service.register("g","g","g","g","345","orderMan");
        service.login("g","g","orderMan");
        List<TransportItemService> itemList = new ArrayList<>();
        itemList.add(new TransportItemService(1, "OSEM", 3));
        service.createTransportRequest("kadima herzog 27", "Zoran hayilan 15", itemList);
        service.logout("g");
        service.login("f","f","manager");
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        //TODO shaun
        //service.createTransportFile(new Date(2022-1910,10,10), 1, "1", "kadima herzog 27", "HA_SHARON", "HA_MERKAZ", idList);
        service.logout("f");*//*

    }*/
}
