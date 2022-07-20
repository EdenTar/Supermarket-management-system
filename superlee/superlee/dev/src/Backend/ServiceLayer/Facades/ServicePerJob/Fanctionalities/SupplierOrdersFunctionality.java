package Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities;

import Backend.Logic.Controllers.Supplier.Product2SuppliersLinker;
import Backend.Logic.Controllers.Supplier.SupplierController;
import Backend.Logic.LogicObjects.Supplier.Order;
import Backend.Logic.Starters.Starter;
import Obj.Pair;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceOrder;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Result.ValueResult;

import java.util.LinkedList;

public interface SupplierOrdersFunctionality {
    SupplierController supplierController = Starter.getInstance().getSupplierController();
    Product2SuppliersLinker product2SuppliersLinker = Starter.getInstance().getProduct2SuppliersLinker();

    default Result<ServiceOrder> addOrder(String supplierCn, String supplierContactPhoneNum, LinkedList<Pair<String, Integer>> products2order){
        try{
            return new ValueResult<>(new ServiceOrder(
                    supplierController.addOrder(supplierCn, supplierContactPhoneNum, products2order)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<LinkedList<ServiceOrder>> getSupplierOrders(String supplierCn){
        try{
            LinkedList<Order> orders = supplierController.getSupplierOrders(supplierCn);
            LinkedList<ServiceOrder> ordersSL = new LinkedList<>();
            for(Order o : orders){
                ordersSL.add(new ServiceOrder(o));
            }
            return new ValueResult<>(ordersSL);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<ServiceOrder> addOrder_bestByDemand(String productName, int demand){
        // might have a null value - which means nothing needs to be ordered - not an exception just need to print nothing
        try{
            return new ValueResult<>(new ServiceOrder(
                    supplierController.addOrder_bestPrice(productName, demand, product2SuppliersLinker.getSupplierCnOfProduct(productName))
            ));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }




}
