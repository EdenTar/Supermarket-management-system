package Callbacks;

import Backend.ServiceLayer.Facades.Callbacks.*;

public class CallbackWrapper {

    private static CallbackWrapper instance = null;
    public static CallbackWrapper getInstance() {
        if(instance == null){
            instance = new CallbackWrapper();
        }
        return instance;
    }

    private CallbackWrapper(){}

    private CallbackNotifyCLI callbackNotifyCLI;

    private CallbackGetTimeIdealSupplier callbackGetTimeIdealSupplier;

    private CallbackGetProductQuantity callbackGetProductQuantity;

    private CallbackGetDemandOfProduct callbackGetDemandOfProduct;

    private CallbackCheckProductForShortage callbackCheckProductForShortage;

    private CallbackAddOrderByDemand callbackAddOrderByDemand;

    public CallbackNotifyCLI getCallbackNotifyCLI() {
        return callbackNotifyCLI;
    }

    public void setCallbackNotifyCLI(CallbackNotifyCLI callbackNotifyCLI) {
        this.callbackNotifyCLI = callbackNotifyCLI;
    }

    public CallbackGetTimeIdealSupplier getCallbackGetTimeIdealSupplier() {
        return callbackGetTimeIdealSupplier;
    }

    public void setCallbackGetTimeIdealSupplier(CallbackGetTimeIdealSupplier callbackGetTimeIdealSupplier) {
        this.callbackGetTimeIdealSupplier = callbackGetTimeIdealSupplier;
    }

    public CallbackGetProductQuantity getCallbackGetProductQuantity() {
        return callbackGetProductQuantity;
    }

    public void setCallbackGetProductQuantity(CallbackGetProductQuantity callbackGetProductQuantity) {
        this.callbackGetProductQuantity = callbackGetProductQuantity;
    }

    public CallbackGetDemandOfProduct getCallbackGetDemandOfProduct() {
        return callbackGetDemandOfProduct;
    }

    public void setCallbackGetDemandOfProduct(CallbackGetDemandOfProduct callbackGetDemandOfProduct) {
        this.callbackGetDemandOfProduct = callbackGetDemandOfProduct;
    }

    public CallbackCheckProductForShortage getCallbackCheckProductForShortage() {
        return callbackCheckProductForShortage;
    }

    public void setCallbackCheckProductForShortage(CallbackCheckProductForShortage callbackCheckProductForShortage) {
        this.callbackCheckProductForShortage = callbackCheckProductForShortage;
    }

    public CallbackAddOrderByDemand getCallbackAddOrderByDemand() {
        return callbackAddOrderByDemand;
    }

    public void setCallbackAddOrderByDemand(CallbackAddOrderByDemand callbackAddOrderByDemand) {
        this.callbackAddOrderByDemand = callbackAddOrderByDemand;
    }
}
