package Backend.Logic.Vehicles;

import Backend.DataAccess.DAOs.TransportDAOs.TruckDAO;
import Backend.Logic.Exceptions.Transport.TransportException;
import Backend.Logic.LogicLambdas.InformObservable;
import Backend.Logic.LogicLambdas.InformObserver;

import java.util.ArrayList;
import java.util.List;

public class Truck implements InformObservable {
    private int truckId;
    private String model;
    private int basicWeight;
    private int maxWeight;
    private boolean isBusy;
    private List<InformObserver> usedBy;
    private License license;
    private TruckDAO dao;

    public Truck(int truckId, String model, int basicWeight, int maxWeight,License license)
    {
        this.truckId=truckId ;
        this.model=model;
        this.basicWeight =basicWeight;
        this.maxWeight=maxWeight;
        this.isBusy=false;
        usedBy=new ArrayList<>();
        this.license=license;
        this.dao=new TruckDAO();
    }



    @Override
    public void notifyAllObservers() {
        for(InformObserver o:usedBy)o.act(this);
    }

    public boolean checkIfCanNotify()
    {
        boolean success=true;
        for(InformObserver o:usedBy) success= o.canAct(this) & success;
        return success;
    }

    @Override
    public void addObserver(InformObserver observer) {
        if(!usedBy.contains(observer))usedBy.add(observer);
    }

    @Override
    public void removeObserver(InformObserver observer) {
        if(usedBy.contains(observer))usedBy.remove(observer);
    }

    public void removeTruck(){
        if(!checkIfCanNotify())throw new TransportException("The truck is used by other files");
        notifyAllObservers();
    }

    public int getId() {
        return truckId;
    }

    public String getModel() {
        return model;
    }

    public int getBasicWeight() {
        return basicWeight;
    }



    public int getMaxWeight() {
        return maxWeight;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {

        if(busy==this.isBusy&busy)throw new TransportException("The truck is already busy");
        isBusy = busy;
        update();
    }

    public License getLicense() {
        return license;
    }

    private void update()
    {
        dao.update(this);
    }
}
