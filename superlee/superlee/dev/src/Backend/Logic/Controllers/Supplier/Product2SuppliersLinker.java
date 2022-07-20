package Backend.Logic.Controllers.Supplier;

import Backend.DataAccess.DAOs.SupplierDAOs.SupplierProductsDAO;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierProductsDTO;
import Backend.ServiceLayer.Facades.Callbacks.CallbackCheckProductForShortage;

import java.util.HashMap;
import java.util.LinkedList;

public class Product2SuppliersLinker {
    private HashMap<String, LinkedList<String>> productToSuppliers;
    private CallbackCheckProductForShortage checkProductForShortage;

    //dal
    private final SupplierProductsDAO productsDAO = new SupplierProductsDAO();

    public Product2SuppliersLinker(){
        productToSuppliers = new HashMap<>();
        checkProductForShortage = null;
    }

    public void setCheckProductForShortage(CallbackCheckProductForShortage checkProductForShortage){
        this.checkProductForShortage = checkProductForShortage;
    }

    private void updateProductSuppliersListFromDB(String productName){
        LinkedList<String> supplierCns = new LinkedList<>();
        for(SupplierProductsDTO spdto : productsDAO.selectAllUnderCondition("productName = '" + productName + "'")){
            supplierCns.add(spdto.getCn());
        }

        if(!supplierCns.isEmpty())
            productToSuppliers.put(productName, supplierCns);
        else if(productToSuppliers.containsKey(productName))
            removeProduct(productName);

    }

    //this function will also load the product data from the database if needed
    private boolean doesProductExists(String productName){
        if(productToSuppliers.containsKey(productName))
            return true;

        updateProductSuppliersListFromDB(productName);
        return productToSuppliers.containsKey(productName);
    }

    //returns list of cns of the suppliers for a certain product
    public LinkedList<String> getSupplierCnOfProduct(String productName){
        if(doesProductExists(productName))
            return productToSuppliers.get(productName);

        throw new IllegalStateException("the product " + productName + " has no suppliers");
    }

    //need to check if i need to throw an exception here or not
    public void addSupplierToProduct(String productName, String supplierCn){
        //the update of this product in the database will be done in Supplier class
        if(!doesProductExists(productName))
            addNewProduct(productName);

        productToSuppliers.get(productName).add(supplierCn);
    }

    private void addNewProduct(String productName){
        if(productToSuppliers.containsKey(productName)){
            throw new IllegalArgumentException("this product already in the system");
        }
        productToSuppliers.put(productName,new LinkedList<>());
    }


    //need to throw an exception if the given supplier doesnt supply this product
    public void removeProductOfSupplier(String productName, String supplierCn){
        if(!doesProductExists(productName))
            throw new IllegalArgumentException("the product " + productName + " has no suppliers");
        if(!productToSuppliers.get(productName).remove(supplierCn)){
                throw new IllegalArgumentException("the supplier didnt have this product in his list of products he is supplying!");
        }
        if(productToSuppliers.get(productName).isEmpty()){
            removeProduct(productName);
            // check for shortage for product name
            this.checkProductForShortage.check(productName);
        }
        //the update of this product in the database will be done in Supplier class
    }

    //need to throw an exception if the product doesnt exist
    private void removeProduct(String productName){
        if(productToSuppliers.remove(productName) == null){
            throw new IllegalArgumentException("this product doesnt exist!");
        }
    }

    public void removeProductsSupplierFromSystem(LinkedList<String> productsNames, String supplierCn){
        for (String productName : productsNames) {
            removeProductOfSupplier(productName,supplierCn);
        }
        //the update of this product in the database will be done in Supplier class

    }

    public void addSupplierToListOfProducts(LinkedList<String> productsNames, String supplierCn){
        for(String productName : productsNames){
            addSupplierToProduct(productName, supplierCn);
        }
        //the update of this product in the database will be done in Supplier class

    }


}
