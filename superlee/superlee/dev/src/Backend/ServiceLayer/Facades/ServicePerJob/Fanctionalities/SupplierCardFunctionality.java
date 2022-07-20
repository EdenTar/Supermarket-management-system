package Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities;

import Backend.Logic.Controllers.Supplier.Product2SuppliersLinker;
import Backend.Logic.Controllers.Supplier.SupplierController;
import Backend.Logic.LogicObjects.Supplier.Contact;
import Backend.Logic.LogicObjects.Supplier.SupplierProduct;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceContact;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceSupplierCard;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Result.ValueResult;

import java.util.LinkedList;

public interface SupplierCardFunctionality {
    SupplierController supplierController = Starter.getInstance().getSupplierController();
    Product2SuppliersLinker product2SuppliersLinker = Starter.getInstance().getProduct2SuppliersLinker();

    //edit
    default Result<Void> addSupplierContact(String supplierCn, String contactNumber, String contactName, String contactEmail){
        try{
            supplierController.addSupplierContact(supplierCn, contactNumber, contactName, contactEmail);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> removeSupplierContact(String supplierCn, String contactNumber){
        try{
            supplierController.removeSupplierContact(supplierCn, contactNumber);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierContactName(String supplierCn, String contactNumber, String newContactName){
        try{
            supplierController.editSupplierContactName(supplierCn, contactNumber, newContactName);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierContactEmail(String supplierCn, String contactNumber, String newContactEmail){
        try{
            supplierController.editSupplierContactEmail(supplierCn, contactNumber, newContactEmail);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierContactNumber(String supplierCn, String contactNumber, String newContactNumber){
        try{
            supplierController.editSupplierContactNumber(supplierCn, contactNumber, newContactNumber);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierCn(String supplierCn, String newCn){
        try{
            LinkedList<SupplierProduct> supplierProducts = supplierController.getSupplierProducts(supplierCn);
            LinkedList<String> productNames = new LinkedList<>();
            for (SupplierProduct sp: supplierProducts) {
                productNames.add(sp.getName());
            }
            supplierController.editSupplierCn(supplierCn, newCn);

            //should not result in an error if it gets here
            product2SuppliersLinker.removeProductsSupplierFromSystem(productNames,supplierCn);
            product2SuppliersLinker.addSupplierToListOfProducts(productNames, newCn);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierBankAccountNum(String supplierCn, String newBankAccountNum){
        try{
            supplierController.editSupplierBankAccountNum(supplierCn, newBankAccountNum);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierPaymentMethod(String supplierCn, String newPaymentMethod){
        try{
            supplierController.editSupplierPaymentMethod(supplierCn, newPaymentMethod);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierPaymentFrequency(String supplierCn, String newPaymentFrequency){
        try{
            supplierController.editSupplierPaymentFrequency(supplierCn, newPaymentFrequency);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<Void> editSupplierName(String supplierCn,String newName){
        try{
            supplierController.editSupplierName(supplierCn, newName);
            return new ValueResult<>(null);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    // getters
    default Result<String> getSupplierName(String supplierCn){
        try{
            return new ValueResult<>(supplierController.getSupplierName(supplierCn));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<String> getSupplierBankAccountNumber(String supplierCn){
        try{
            return new ValueResult<>(supplierController.getSupplierBankAccountNumber(supplierCn));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<String> getSupplierPaymentFrequency(String supplierCn){
        try{
            return new ValueResult<>(supplierController.getSupplierPaymentFrequency(supplierCn));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<String> getSupplierPaymentMethod(String supplierCn){
        try{
            return new ValueResult<>(supplierController.getSupplierPaymentMethod(supplierCn));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<ServiceContact> getSupplierContact(String supplierCn, String contactPhoneNumber){
        try{
            return new ValueResult<>(new ServiceContact(supplierController.getSupplierContact(supplierCn,contactPhoneNumber)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<LinkedList<ServiceContact>> getSupplierContactList(String supplierCn){
        try{
            LinkedList<Contact> contactsBL = supplierController.getSupplierContactList(supplierCn);
            LinkedList<ServiceContact> contactsSL = new LinkedList<>();
            for(Contact c : contactsBL){
                contactsSL.add(new ServiceContact(c));
            }
            return new ValueResult<>(contactsSL);
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<ServiceSupplierCard> getAllSupplierInfo(String supplierCn){
        try{
            return new ValueResult<>(
                    new ServiceSupplierCard(supplierController.getAllSupplierInfo(supplierCn)));
        }
        catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }


}
