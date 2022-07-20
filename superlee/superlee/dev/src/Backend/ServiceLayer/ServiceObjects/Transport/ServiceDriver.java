package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.LogicObjects.Jobs.Driver;

public class ServiceDriver extends ServiceEmployee {

    private final LicenseService license;

    @Override
    public String toString() {
        return "Driver:\n" +
                "license=" + license +
                " " + super.toString() + '\n';
    }

    public ServiceDriver(Driver driver) {
        super(driver.getEmployeeId(), driver.getEmployeeName(), driver.getEmployeeLastName(), driver.getPassword());
        this.license = LicenseService.valueOf(driver.getDriverLicense().name());

    }

    public LicenseService getLicense() {
        return license;
    }

}
