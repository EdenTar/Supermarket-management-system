package Backend.Logic.Vehicles;

import Backend.DataAccess.DAOs.TransportDAOs.TruckDAO;

import Backend.DataAccess.DTOs.TransportDTOS.TruckDTO;
import Backend.Logic.LogicLambdas.FreeTruck;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class VehicleController {

    private TruckDAO truckDAO;
    private FreeTruck freeTruck;




    public VehicleController() {
        this.freeTruck=null;
        this.truckDAO=new TruckDAO();
    }

    public void initialize(FreeTruck freeTruck)
    {
        this.freeTruck=freeTruck;
    }
    public List<Truck> availableTruck(Date start,Date end)
    {
        List<Truck> list=new LinkedList<>();
        for(Truck t:truckDAO.getRowsFromDB())
        {
            if(freeTruck.freeTruck(t,start,end))
                list.add(t);
        }
        return list;
    }
    public void addTruck(int truckId, String model, int basicWeight, int maxWeight,License license) {
        //if (isTruckExist(truckId)) throw new TransportException("The truck id already exists");
        truckDAO.insert(new Truck(truckId, model, basicWeight, maxWeight,license));

    }

    public void removeTruck(int truckId) {
        Truck toDelete = getTruck(truckId);
        toDelete.removeTruck();
        truckDAO.deleteRow(toDelete);
    }

    public Truck getFreeTruck() {
        return truckDAO.getFreeTrucks().stream().findFirst().orElse(null);
    }


    public Truck getTruck(int truckId) {//TODO modified by shaun
        //if (!isTruckExist(truckId)) throw new TransportException("The truck is not exists");

        return truckDAO.getRow(TruckDTO.getPK(truckId));

    }


}
