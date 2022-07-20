package Backend.Logic.Controllers.Stock;

import Backend.DataAccess.DAOs.StockDAOs.DeliveriesDAO;
import Backend.DataAccess.DTOs.StockDTOS.ProductsDTO;
import Backend.Logic.LogicObjects.Product.*;
import Backend.Logic.LogicObjects.Report.Category;
import Backend.DataAccess.DAOs.StockDAOs.ProductsDAO;
import Backend.Logic.LogicObjects.Transport.TransportItem;
import Backend.ServiceLayer.Facades.Callbacks.CallbackAddOrderByDemand;
import Backend.ServiceLayer.Facades.Callbacks.CallbackGetTimeIdealSupplier;
import Backend.ServiceLayer.Facades.Callbacks.CallbackNotifyCLI;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductController {

    private final ProductsDAO productsDAO;
    private final DeliveriesDAO deliveriesDAO;
    private final CallbackGetTimeIdealSupplier getTimeIdealSupplier;
    private final CallbackAddOrderByDemand addOrderByDemand;
    private final CallbackNotifyCLI notifyCLI;

    public ProductController(CallbackGetTimeIdealSupplier getTimeIdealSupplier,
                             CallbackAddOrderByDemand addOrderByDemand,
                             CallbackNotifyCLI notify) {
        productsDAO = new ProductsDAO();
        deliveriesDAO = new DeliveriesDAO();
        this.addOrderByDemand = addOrderByDemand;
        this.getTimeIdealSupplier = getTimeIdealSupplier;
        this.notifyCLI = notify;
    }

    public List<Product> getProducts() {
        List<Product> lst = productsDAO.selectAllRowsToBusiness();
        lst.forEach(p -> {
            p.setOrderByDemand(addOrderByDemand);
            p.setGetTimeIdealSupplier(getTimeIdealSupplier);
            p.setNotifyCLI(notifyCLI);
        });
        return lst;
    }

    public Delivery getDelivery() {
        return deliveriesDAO.getDelivery();
    }

    public void addDelivery(List<TransportItem> transportItems) {
        Delivery old = getDelivery();
        Delivery delivery;
        if (old == null) {
            delivery = new Delivery(0, transportItems);
        } else {
            delivery = new Delivery(old.getId() + 1, transportItems);
        }
        deliveriesDAO.insert(delivery);
    }

    public void receiveDelivery(List<Integer> quantities, List<Date> expired_dates,
                                List<String> branches, List<String> places, List<Integer> shelfs) {
        Delivery delivery = getDelivery();
        for (int i = 0; i < quantities.size(); i++) {
            TransportItem deliveryProduct = delivery.getTransportItems().get(i);
            Product product = getProductByName(deliveryProduct.getName());
            product.addItems(branches.get(i), Location.StoreOrStorage.valueOf(places.get(i)), shelfs.get(i), expired_dates.get(i),quantities.get(i));
        }
        deliveriesDAO.delete(delivery);
    }

    private Product getProductByName(String productName) {
        return getProductsByPredicate(product -> product.getName().equals(productName)).get(0);
    }

    public Integer getProductQuantity(String productName) {
        Product p = getProductByName(productName);
        return p.getQuantity() - p.getFlawedQuantity();
    }

    public Integer getProductDemand(String productName) {
        return getProductByName(productName).getDemandPerDay();
    }

    public Product getProduct(String productNumber) throws Exception {
        Product p = productsDAO.getRow(ProductsDTO.getPK(Long.parseLong(productNumber)));
        checkValidity(p == null, "product number doesn't exist");
        p.setGetTimeIdealSupplier(getTimeIdealSupplier);
        p.setOrderByDemand(addOrderByDemand);
        p.setNotifyCLI(notifyCLI);
        return p;
    }

    public Product addProduct(String productNumber, String name, String manufacturer,
                              double price, Category category, int demand) throws Exception {
        Product p = productsDAO.getRow(ProductsDTO.getPK(Long.parseLong(productNumber)));
        checkValidity(p != null, "product number already exist");
        Product toAdd = new Product(productNumber, name, manufacturer, price, category, demand, getTimeIdealSupplier, addOrderByDemand, notifyCLI);
        productsDAO.insert(toAdd);
        return toAdd;
    }

    public void checkForShortage(String name) {
        if (getProducts().stream().noneMatch((product) -> product.getName().equals(name))) {
            throw new IllegalStateException("no such item exist");
        }
        getProducts().stream().filter((product) -> product.getName().equals(name)).collect(Collectors.toList()).get(0).checkMissing();
    }

    public List<Product> getMissingProducts() {
        return getProductsByPredicate(Product::isMissing);
    }

    public List<Product> getProductsByCategories(List<Category> categories) {
        return getProductsByPredicate(product -> product.getCategory().getCategoryPath().stream().anyMatch(categories::contains));
    }

    private List<Product> getProductsByPredicate(Predicate<Product> predicate) {
        return getProducts().stream().filter(predicate).collect(Collectors.toList());
    }

    public Product removeProduct(String productNumber) throws Exception {
        Product p = productsDAO.getRow(ProductsDTO.getPK(Long.parseLong(productNumber)));
        checkValidity(p == null, "product number doesn't exist");
        productsDAO.deleteRow(p);
        return p;
    }

    public Product updateProductDemand(String productNumber, int demand) throws Exception {
        getProduct(productNumber).setDemand(demand);
        return getProduct(productNumber);
    }

    public Product updateProductPrice(String productNumber, double price) throws Exception {
        getProduct(productNumber).setPrice(price);
        return getProduct(productNumber);
    }

    public Product updateProductCategory(String productNumber, Category category) throws Exception {
        getProduct(productNumber).setCategory(category);
        return getProduct(productNumber);
    }

    public Item reportItemAsFlawed(String productNumber, String branch, Location.StoreOrStorage place, int shelf) throws Exception {
        Product product = getProduct(productNumber);
        Item item = product.getNotFlawedItem(branch, place, shelf);
        item.setFlaw(true);
        product.checkMissing();
        return item;
    }

    public Purchase addPurchase(String productNumber, double costPrice, double salePrice, double discount, int quantity, String supplier, Date purchaseTime) throws Exception {
        return getProduct(productNumber).addPurchase(costPrice, salePrice, discount, quantity, supplier, purchaseTime);
    }

    public List<Product> getDamagedProducts() {
        return getProductsByPredicate(product -> product.getFlawedItems().size() > 0 || product.getExpiredQuantity() > 0);
    }

    private void checkValidity(boolean exp, String errorMessage) throws Exception {
        if (exp) {
            throw new Exception(errorMessage);
        }
    }


}
