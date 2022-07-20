package Backend.ServiceLayer.Facades.ServicePerJob.Transport;

import Backend.Logic.Controllers.TransportEmployee.TransportManagerController;
import Backend.Logic.LogicObjects.Transport.DestinationFile;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.Logic.LogicObjects.Transport.OrderTransport;
import Backend.Logic.Points.Zone;
import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Utilities.Pair;
import Backend.Logic.Vehicles.License;
import Backend.Logic.Vehicles.Truck;
import Backend.ServiceLayer.Facades.ServicePerJob.Employees.BasicEmployeeService;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.ServiceObjects.Transport.*;
import Backend.ServiceLayer.Result.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class TransportManagerService extends BasicEmployeeService implements DeleteSupplierOrderFunctionality {
    private final TransportManagerController transportManagerController;

    //TODO constructor
    public TransportManagerService(TransportManagerController transportManagerController) {
        this.transportManagerController = transportManagerController;
    }


    public Response<ServiceTransportFile> getTransportFile(int id) {
        try {
            TransportFile getTransportFile = transportManagerController.getTransportFile(id);
            ServiceTransportFile getTransportFileService = new ServiceTransportFile(getTransportFile);
            return new Response<>(getTransportFileService);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<OrderTransportService> getOrderTransport(int id) {
        try {
            OrderTransport getOrderTransport = transportManagerController.getOrderTransport(id);
            OrderTransportService getOrderTransportService = new OrderTransportService(getOrderTransport);
            return new Response<>(getOrderTransportService);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> getAllPointInfo() {
        try {
            return new Response<>(transportManagerController.getAllPointInfo());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> showAndNotifyMatchDates(Integer orderId) {
        try {
            String result = transportManagerController.showAndNotifyMatchDates(orderId);
            Response<String> response = new Response<>();
            response.setValue(result);
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<OrderTransportService> convertToOrderTransportServiceList(List<OrderTransport> orderTransportsRequests) {
        List<OrderTransportService> convertedList = new ArrayList<>();
        for (OrderTransport orderTransport : orderTransportsRequests) {
            convertedList.add(new OrderTransportService(orderTransport));
        }
        return convertedList;
    }

    //getAllPointInfo
    public Response<List<ServiceTransportFile>> showInProgressTransports() {
        try {
            List<TransportFile> showInProgressTransports = transportManagerController.showInProgressTransports();
            List<ServiceTransportFile> showInProgressServiceTransports =
                    showInProgressTransports.stream()
                            .map(ServiceTransportFile::new).
                            collect(Collectors.toList());
            return new Response<>(showInProgressServiceTransports);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<?> addTruck(int truckId, String model, int currentWeight, int maxWeight, LicenseService l) {

        try {
            transportManagerController.addTruck(truckId, model, currentWeight, maxWeight, License.valueOf(l.name()));
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response insertBranch(String address,
                                 String phone,
                                 String contactName,
                                 String zone) {

        try {
            transportManagerController.insertBranch(address, phone, contactName, Zone.valueOf(zone));
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

    }

    public Response<List<OrderTransportService>> showAllTransportRequests() {

        try {
            return new Response<>(transportManagerController.showAllTransportRequests().stream().map(OrderTransportService::new).collect(Collectors.toList()));

        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

    }

    public Response insertSupplier(String address,
                                   String phone,
                                   String contactName,
                                   String zone) {
        try {
            transportManagerController.insertSupplier(address, phone, contactName, Zone.valueOf(zone));
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

    }


    public Response deleteTruck(int truckId) {
        try {
            transportManagerController.deleteTruck(truckId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<List<ServiceTruck>> showAllAvailableTrucks(Date from, Date to) {
        try {
            List<Truck> logicTrucks = transportManagerController.showAllAvailableTrucks(from, to);
            List<ServiceTruck> serviceTrucks = convertToServiceTrucks(logicTrucks);
            return new Response<>(serviceTrucks);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<ServiceTruck> convertToServiceTrucks(List<Truck> logicTrucks) {
        List<ServiceTruck> convertedList = new ArrayList<>();
        for (Truck truck : logicTrucks) {
            convertedList.add(new ServiceTruck(truck));
        }
        return convertedList;
    }


    public Response createTransportFile(Date startingDate, Date endDate, int truckId, int driverId, String source, String from, String to, List<Integer> orderId, List<Date> endDates) {
        try {
            transportManagerController.createTransportFile(startingDate, endDate, truckId, driverId, source, from, to, orderId, endDates);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response createTransportFile(Date startingDate, Date endDate, int truckId, String source, String from, String to, List<Integer> orderId, List<Date> endDates) {
        try {
            transportManagerController.createTransportFile(startingDate, endDate, truckId, source, from, to, orderId, endDates);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<List<ServiceDriver>> showAllAvailableDrivers(Date from, Date to) {
        try {
            List<Driver> logicDrivers = transportManagerController.showAllAvailableDrivers(from, to);
            List<ServiceDriver> serviceTrucks = convertToServiceDrivers(logicDrivers);
            return new Response<>(serviceTrucks);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<ServiceDriver> convertToServiceDrivers(List<Driver> logicDrivers) {
        List<ServiceDriver> convertedList = new ArrayList<>();
        for (Driver driver : logicDrivers) {
            convertedList.add(new ServiceDriver(driver));
        }
        return convertedList;
    }

    public Response removeItems(String destinationId, List<Pair<String, Integer>> pairList) {
        try {
            transportManagerController.removeItems(destinationId, pairList);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response changeTruck(int truckId, int transportFileId) {
        try {
            transportManagerController.changeTruck(truckId, transportFileId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response changeDriver(int driverId, int transportFileId) {
        try {
            transportManagerController.changeDriver(driverId, transportFileId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response cancelTransport(int transportFileId) {
        try {
            transportManagerController.cancelTransport(transportFileId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<?> addComment(String comment, int transportId) {
        try {
            transportManagerController.addComment(comment, transportId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response removeDestination(int transportId, String destination) {
        try {
            transportManagerController.deleteDestination(transportId, destination);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<ServiceDestinationFile> getDestinationFile(String destinationFileId) {
        try {
            DestinationFile destinationFile = transportManagerController.getDestinationFile(destinationFileId);
            ServiceDestinationFile serviceDestinationFile = new ServiceDestinationFile(destinationFile);
            return new Response<>(serviceDestinationFile);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<List<ServiceTransportFile>> showOldTransports() {
        try {
            List<TransportFile> logicFiles = transportManagerController.showOldTransports();
            List<ServiceTransportFile> serviceTrucks = convertToServiceTransportFiles(logicFiles);
            return new Response<>(serviceTrucks);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<ServiceTransportFile> convertToServiceTransportFiles(List<TransportFile> logicFiles) {
        List<ServiceTransportFile> convertedList = new ArrayList<>();
        for (TransportFile file : logicFiles) {
            convertedList.add(new ServiceTransportFile(file));
        }
        return convertedList;
    }


    public Response<OrderTransportService> getRequestsByPriority() {
        try {
            OrderTransport logicOrderTransports = transportManagerController.getRequestsByPriority();
            OrderTransportService serviceOrderTransport = new OrderTransportService(logicOrderTransports);
            return new Response<>(serviceOrderTransport);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<OrderTransportService> convertToServiceOrderTransports(List<OrderTransport> logicOrderTransports) {
        List<OrderTransportService> convertedList = new ArrayList<>();
        for (OrderTransport orderTransport : logicOrderTransports) {
            convertedList.add(new OrderTransportService(orderTransport));
        }
        return convertedList;
    }


    public Response<List<OrderTransportService>> getRequestsByZone(String fromZone, String toZone) {
        try {
            List<OrderTransport> logicOrderTransports = transportManagerController.getRequestsByZone(fromZone, toZone);
            List<OrderTransportService> serviceOrderTransports = convertToServiceOrderTransportsPerZone(logicOrderTransports);
            return new Response<>(serviceOrderTransports);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private List<OrderTransportService> convertToServiceOrderTransportsPerZone(List<OrderTransport> logicOrderTransports) {
        List<OrderTransportService> list = new ArrayList<>();
        for (OrderTransport zoneListPair : logicOrderTransports) {
            list.add(new OrderTransportService(zoneListPair));
        }
        return list;
    }
/*    public void addConstraints(Date date, ShiftTime shiftTime) throws Exception {
        transportManagerController.addConstraint(date,shiftTime);
    }*/


    public Response addConstraints(Date date, ShiftTime shiftTime, int id) {
        try {
            transportManagerController.addConstraint(date, shiftTime);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public Response deleteConstraints(Date date, ShiftTime shiftTime, int id) {
        try {
            transportManagerController.deleteConstraint(date, shiftTime);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
