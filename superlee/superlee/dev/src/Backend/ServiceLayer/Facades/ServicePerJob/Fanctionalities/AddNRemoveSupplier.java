package Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities;
import Backend.Logic.Controllers.Supplier.Product2SuppliersLinker;
import Backend.Logic.Controllers.Supplier.SupplierController;
import Backend.Logic.LogicObjects.Supplier.Order;
import Backend.Logic.LogicObjects.Supplier.SupplierProduct;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Result.ValueResult;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;

import java.util.LinkedList;

public interface AddNRemoveSupplier {
    SupplierController supplierController = Starter.getInstance().getSupplierController();
    Product2SuppliersLinker product2SupplierLinker = Starter.getInstance().getProduct2SuppliersLinker();

    default Result<Void> addSupplier(String supplierCn, String supplierName, String bankAccountNum, String paymentFrequency, String paymentMethod
            , String contactEmail, String contactName, String contactPhoneNumber,String address){

        try{
            supplierController.addSupplier(supplierCn, supplierName, bankAccountNum, paymentFrequency,
                    paymentMethod, contactEmail, contactName, contactPhoneNumber,address, Starter.getInstance().getCheckProductForShortage());
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<LinkedList<ServiceOrder>> removeSupplier(String supplierCn){
        try{
            LinkedList<SupplierProduct> supplierProducts = supplierController.getSupplierProducts(supplierCn);
            LinkedList<String> productNames = new LinkedList<>();
            for (SupplierProduct sp: supplierProducts)
                productNames.add(sp.getName());

            LinkedList<ServiceOrder> deletedOrders = new LinkedList<>();
            for (Order order : supplierController.removeSupplier(supplierCn))
                deletedOrders.add(new ServiceOrder(order));
            
            //there should be no errors if it gets here.
            product2SupplierLinker.removeProductsSupplierFromSystem(productNames, supplierCn);
            return new ValueResult<>(deletedOrders);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }





}
