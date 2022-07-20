package Backend.ServiceLayer.Facades.ServicePerJob.Transport;

import Backend.Logic.Controllers.TransportEmployee.DriverController;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.ServiceLayer.Result.Response;

import java.util.Date;

@SuppressWarnings("rawtypes")

public class DriverService {
    private final DriverController driverController;

    public DriverService(DriverController driverController) {
        this.driverController = driverController;
    }

    public Response weightTruck(int weight) {

        try {
            driverController.weightTruck(weight);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateLicense(String license) {
        try {
            driverController.updateLicense(license);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response setStarted(int transportId) {
        try {
            driverController.setStarted(transportId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

    }

    public Response finishDestinationFile(String destinationId) {
        try {
            driverController.finishDestinationFile(destinationId);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> showDetails() {
        try {
            Response<String> response = new Response<>();
            response.setValue(driverController.showDetails());
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());

        }
    }

    public Response addConstraints(Date date, ShiftTime shiftTime) throws Exception {
        return new Response("error in driverService");
    }


    public Response addConstraints(Date date, ShiftTime shiftTime, int id) {
        try {
            driverController.addConstraint(date, shiftTime);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response deleteConstraints(Date date, ShiftTime shiftTime, int id) {
        try {
            driverController.deleteConstraint(date, shiftTime);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
