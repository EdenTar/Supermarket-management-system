package Backend.Logic.Starters;

import Backend.Logic.Controllers.Stock.CategoryController;
import Backend.Logic.Controllers.Stock.DiscountHolder;
import Backend.Logic.Controllers.Stock.ProductController;
import Backend.Logic.Controllers.Stock.ReportBuilder;
import Backend.Logic.Controllers.Supplier.Product2SuppliersLinker;
import Backend.Logic.Controllers.Supplier.SupplierController;
import Backend.Logic.Controllers.TransportEmployee.*;
import Backend.Logic.Points.TransportMap;
import Backend.Logic.LogicObjects.Transport.TransportBoard;
import Backend.Logic.Vehicles.VehicleController;
import Backend.Logic.LogicLambdas.IsStoreKeeperInShift;
import Backend.ServiceLayer.Facades.Callbacks.*;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.HRService;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.UserService;
import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StockKeeperService;
import Backend.ServiceLayer.Facades.ServicePerJob.Stock.StoreManagerService;
import Backend.ServiceLayer.Facades.ServicePerJob.Supplier.SupplierManagerService;
import Backend.ServiceLayer.Facades.ServicePerJob.Transport.DriverService;
import Backend.ServiceLayer.Facades.ServicePerJob.Transport.TransportManagerService;
import Backend.ServiceLayer.Services.TransportEmployeeService;
import PresentationLayer.EmployeTransportFrontend.CLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.BasicEmployeeCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.DriverCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.HRManagerCLI;
import PresentationLayer.EmployeTransportFrontend.JobsCLI.TransportManagerCLI;
import PresentationLayer.JobsCLI.CLIStockKeeper;
import PresentationLayer.JobsCLI.CLIStoreManager;
import PresentationLayer.JobsCLI.CLISupplierManager;

import java.util.LinkedList;
import java.util.Scanner;

public class Starter {
    //singleton
    private static Starter instance;

    public static Starter getInstance() {
        if (instance == null)
            instance = new Starter();

        return instance;
    }
    //
    private final Scanner scanner = new Scanner(System.in);

    //CLI
    private CLI cli;
    private BasicEmployeeCLI basicEmployeeCLI;
    private DriverCLI driverCLI;
    private HRManagerCLI hrManagerCLI;
    private TransportManagerCLI transportManagerCLI;
    private CLIStockKeeper stockKeeperCLI;
    private CLIStoreManager storeManagerCLI;
    private CLISupplierManager supplierMangerCLI;


    //controllers
    private TransportMap transportMap;
    private VehicleController vehicleController;
    private TransportBoard transportBoard;
    private OrderTransportController orderTransportController;
    private UserController userController;
    private TransportManagerController transportManagerController;
    private OrderManController orderManController;
    private DriverController driverController;
    private HRController hrController;
    private BasicEmployeeController basicEmployeeController;

    private SupplierController supplierController;
    private Product2SuppliersLinker product2SuppliersLinker;
    private CategoryController categoryController;
    private DiscountHolder discountHolder;
    private ProductController productController;
    private ReportBuilder reportBuilder;

    private StockKeeperController stockKeeperController;

    private StoreManagerController storeManagerController;

    private SupplierManagerController supplierManagerController;

    //services
    private HRService hrService;
    private UserService userService;
    private TransportManagerService transportManagerService;
    private DriverService driverService;

    private StockKeeperService stockKeeperService;
    private StoreManagerService storeManagerService;
    private SupplierManagerService supplierManagerService;


    //callbacks
    private IsStoreKeeperInShift isStoreKeeperInShift;
    private CallbackNotifyCLI notifyCLI;
    private CallbackGetDemandOfProduct getDemandOfProduct;
    private CallbackGetProductQuantity getProductQuantity;
    private CallbackCheckProductForShortage checkProductForShortage;
    private CallbackGetTimeIdealSupplier getTimeIdealSupplier;
    private CallbackAddOrderByDemand addOrderByDemand;


    //constructor
    private Starter() {
        createCallbacks();
        createIsStoreKeeperInShift();
        initVehicleController();
        initDriverController();
        initHRController();
        createProductController();
        createSupplierController();
        createProduct2SuppliersLinker();

    }

    public void createCallbacks(){
        if(addOrderByDemand == null) {
            addOrderByDemand = (a, n, d) ->
                    getSupplierController().addOrder_bestPrice(n, d, getProduct2SuppliersLinker().getSupplierCnOfProduct(n));
        }
        if(getTimeIdealSupplier == null){
            getTimeIdealSupplier = (n, d) -> {
                LinkedList<String> productSuppliers = getProduct2SuppliersLinker().getSupplierCnOfProduct(n);
                if (productSuppliers == null || productSuppliers.size() == 0) {
                    return Integer.MAX_VALUE;
                }

                return getSupplierController().getTimeTillNextShipmentIdealSupplierShipment(n, d, productSuppliers);
            };
        }
        if(notifyCLI == null){
            notifyCLI = m -> {System.out.println(m);};
        }
        if(getDemandOfProduct == null){
            getDemandOfProduct = getProductController()::getProductDemand;
        }
        if(getProductQuantity == null){
            getProductQuantity = getProductController()::getProductQuantity;
        }
        if(checkProductForShortage == null){
            checkProductForShortage = getProductController()::checkForShortage;
        }
    }

    //getters - services
    public TransportManagerService getManagerService() {
        if (transportManagerService == null)
            transportManagerService = new TransportManagerService(getTransportManagerController());
        return transportManagerService;
    }

    public DriverService getDriverService() {
        if (driverService == null)
            driverService = new DriverService(getDriverController());
        return driverService;
    }

    public HRService getHrService() {
        if (hrService == null)
            hrService = new HRService(getHRController());
        return hrService;
    }

    public UserService getUserService() {
        if (userService == null)
            userService = new UserService(getUserController());
        return userService;
    }

    public StockKeeperService getStockKeeperService() {
        if (stockKeeperService == null)
            stockKeeperService = new StockKeeperService();
        return stockKeeperService;
    }

    public StoreManagerService getStoreManagerService() {
        if (storeManagerService == null)
            storeManagerService = new StoreManagerService();
        return storeManagerService;
    }

    public SupplierManagerService getSupplierManagerService() {
        if (supplierManagerService == null)
            supplierManagerService = new SupplierManagerService();
        return supplierManagerService;
    }


    //creators
    private void createCLI() {
        createJobCLIs();
        if(cli == null) {
            CLI.transportEmployeeService = new TransportEmployeeService();
            CLI.scanner = scanner;
            cli = new CLI(basicEmployeeCLI, driverCLI, hrManagerCLI, transportManagerCLI,
                    stockKeeperCLI, storeManagerCLI, supplierMangerCLI);
        }
    }

    private void createJobCLIs() {
        createBasicEmployeeCLI();
        createTransportManagerCLI();
        createDriverCLI();
        createHRManagerCLI();
        createStockKeeperCLI();
        createStoreManagerCLI();
        createSupplierMangerCLI();
    }
    private void createBasicEmployeeCLI(){
        if(basicEmployeeCLI==null)
            basicEmployeeCLI = new BasicEmployeeCLI();
    }
    private void createTransportManagerCLI(){
        if(transportManagerCLI==null)
            transportManagerCLI = new TransportManagerCLI();
    }
    private void createDriverCLI() {
        if(driverCLI==null)
            driverCLI = new DriverCLI();
    }
    private void createHRManagerCLI() {
        if(hrManagerCLI == null)
            hrManagerCLI = new HRManagerCLI();
    }

    private void createStockKeeperCLI(){
        if(stockKeeperCLI == null)
            stockKeeperCLI = new CLIStockKeeper(getStockKeeperService(), scanner);
    }

    private void createStoreManagerCLI(){
        if(storeManagerCLI == null)
            storeManagerCLI = new CLIStoreManager(getStoreManagerService(), scanner);
    }

    private void createSupplierMangerCLI(){
        if(supplierMangerCLI == null){
            supplierMangerCLI = new CLISupplierManager(getSupplierManagerService(), scanner);
        }
    }


    private void createIsStoreKeeperInShift() {
        createHRController();
        if (isStoreKeeperInShift == null)
            isStoreKeeperInShift = hrController.getShiftController().isStoreKeeperInShiftLambda();
    }

    private void createHRController() {
        if (hrController == null)
            hrController = new HRController(new ShiftController(), new EmployeeController());
    }

    private void createStockKeeperController() {
        if (stockKeeperController == null)
            stockKeeperController = new StockKeeperController();
    }

    private void createStoreManagerController() {
        if (storeManagerController == null)
            storeManagerController = new StoreManagerController();
    }


    private void createSupplierManagerController() {
        if (supplierManagerController == null)
            supplierManagerController = new SupplierManagerController();
    }

    private void createBasicEmployeeController() {
        if (basicEmployeeController == null)
            basicEmployeeController = new BasicEmployeeController();
    }

    private void createTransportMap() {
        if (transportMap == null)
            transportMap = new TransportMap();
    }

    private void createVehicleController() {
        if (vehicleController == null)
            vehicleController = new VehicleController();
    }

    private void createDriverController() {
        if (driverController == null)
            driverController = new DriverController();
    }

    private void createOrderTransportController() {
        createTransportMap();
        if (orderTransportController == null)
            orderTransportController = new OrderTransportController(transportMap);
    }

    private void createOrderManController() {
        createOrderTransportController();
        createTransportMap();
        if (orderManController == null)
            orderManController = new OrderManController(orderTransportController);
    }

    private void createTransportBoard() {
        createOrderTransportController();
        createDriverController();
        createTransportMap();
        createHRController();
        createSupplierController();
        if (transportBoard == null)
            transportBoard = new TransportBoard(
                    orderTransportController,
                    driverController.getAllDriversLambda(),
                    transportMap,
                    hrController.getShiftController().getDriversInShift(),
                    isStoreKeeperInShift,
                    productController::addDelivery,
                    supplierController.getSupplierDaysLambda(),
                    hrController.getShiftController()::NotifyForDrivers);
    }

    private void createTransportManagerController() {
        createVehicleController();
        createTransportBoard();
        if (transportManagerController == null)
            transportManagerController = new TransportManagerController(vehicleController, transportBoard);
    }

    private void createUserController() {
        createOrderManController();
        createTransportManagerController();
        createDriverController();
        createHRController();
        createBasicEmployeeController();
        createStockKeeperController();
        createStoreManagerController();
        createSupplierManagerController();
        if (userController == null)
            userController = new UserController(orderManController, transportManagerController, driverController, hrController, basicEmployeeController,
                    stockKeeperController, storeManagerController, supplierManagerController);
    }

    private void createProduct2SuppliersLinker() {
        if (product2SuppliersLinker == null)
            product2SuppliersLinker = new Product2SuppliersLinker();
    }

    private void createSupplierController() {
        createOrderManController();
        if (supplierController == null){
            supplierController = new SupplierController(getProductQuantity, getDemandOfProduct, checkProductForShortage, orderManController);
            supplierController.checkAllProductsForShortage();
        }

    }

    private void createCategoryController() {
        if (categoryController == null)
            categoryController = new CategoryController();
    }

    private void createDiscountHolder() {
        if (discountHolder == null)
            discountHolder = new DiscountHolder();
    }

    private void createProductController() {
        if (productController == null)
            productController = new ProductController(getTimeIdealSupplier, addOrderByDemand, notifyCLI);
    }

    private void createReportBuilder() {
        if (reportBuilder == null)
            reportBuilder = new ReportBuilder(productController, categoryController);
    }


    //initialize controllers
    private void initVehicleController() {
        createVehicleController();
        createTransportBoard();
        vehicleController.initialize(transportBoard.getFreeTruck());
    }

    private void initDriverController() {
        createDriverController();
        createTransportBoard();
        driverController.setTransportBoard(transportBoard);
    }

    private void initHRController() {
        createHRController();
        createDriverController();
        createTransportManagerController();
        createOrderManController();
        hrController.initialize(driverController, transportManagerController);
    }

    //getters - CLI
    public CLI getCLI() {
        createCLI();
        return cli;
    }

    //getters - controllers
    public HRController getHRController() {
        createHRController();
        return hrController;
    }

    public BasicEmployeeController getBasicEmployeeController() {
        createBasicEmployeeController();
        return basicEmployeeController;
    }

    private TransportMap getTransportMap() {
        createTransportMap();
        return transportMap;
    }

    public StockKeeperController getStockKeeperController() {
        createStockKeeperController();
        return stockKeeperController;
    }

    public StoreManagerController getStoreManagerController() {
        createStoreManagerController();
        return storeManagerController;
    }

    public SupplierManagerController getSupplierManagerController() {
        createSupplierManagerController();
        return supplierManagerController;
    }

    public VehicleController getVehicleController() {
        createVehicleController();
        return vehicleController;
    }

    public DriverController getDriverController() {
        createDriverController();
        return driverController;
    }

    public OrderTransportController getOrderTransportController() {
        createOrderTransportController();
        return orderTransportController;
    }

    public OrderManController getOrderManController() {
        createOrderManController();
        return orderManController;
    }

    public TransportBoard getTransportBoard() {
        createTransportBoard();
        return transportBoard;
    }

    public TransportManagerController getTransportManagerController() {
        createTransportManagerController();
        return transportManagerController;
    }

    public UserController getUserController() {
        createUserController();
        return userController;
    }

    public Product2SuppliersLinker getProduct2SuppliersLinker() {
        createProduct2SuppliersLinker();
        return product2SuppliersLinker;
    }

    public SupplierController getSupplierController() {
        createSupplierController();
        return supplierController;
    }

    public CategoryController getCategoryController() {
        createCategoryController();
        return categoryController;
    }

    public DiscountHolder getDiscountHolder() {
        createDiscountHolder();
        return discountHolder;
    }

    public ProductController getProductController() {
        createProductController();
        return productController;
    }

    public ReportBuilder getReportBuilder() {
        createReportBuilder();
        return reportBuilder;
    }

    //getters - callbacks
    public CallbackCheckProductForShortage getCheckProductForShortage() {
        return checkProductForShortage;
    }

    // restarters
    public void restart(){
        supplierController.lior();
        instance = null;
        SupplierController.deleteCheckDate();
    }

}
