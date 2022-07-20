package Backend.ServiceLayer.Facades.ServicePerJob.Employees;

import Backend.Logic.Controllers.TransportEmployee.HRController;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.DeleteSupplierOrderFunctionality;
import Backend.ServiceLayer.Result.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
@SuppressWarnings("rawtypes")

public class HRService implements DeleteSupplierOrderFunctionality {
    private final HRController hrController;

    public HRService(HRController hrController) {
        this.hrController = hrController;
    }

    public Response addNewJob(String jobName) {
        try {
            hrController.addNewJob(jobName);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

    }

    public Response addNewEmployee(String firstName, String lastName, Date date, int id,
                                   String jobName, boolean isShiftManager,
                                   int bankNumber, int accountNumber,
                                   int bankBranch, double salary, String socialBenefits,String branchAddress) {
        try {
            hrController.addNewEmployee(firstName, lastName, date, id, jobName, isShiftManager, bankNumber, accountNumber, bankBranch, salary, socialBenefits,branchAddress);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateBankBranch(int id, int newBankBranch) {
        try {
            hrController.updateBankBranch(id, newBankBranch);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateAccountNumber(int id, int newAccountNumber) {
        try {
            hrController.updateAccountNumber(id, newAccountNumber);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateBankNumber(int id, int newBankNumber) {
        try {
            hrController.updateBankNumber(id, newBankNumber);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response removeEmployee(int id) {
        try {
            hrController.removeEmployee(id);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response addScheduling(ShiftTime shiftTime, Date date,String branch, int employeeID, String jobName) {//DONE
        try {
            hrController.addScheduling(shiftTime, date, branch ,hrController.getEmployee(employeeID), jobName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeScheduling(ShiftTime shiftTime, Date date,String branch, int employeeID) {//DONE
        try {
            hrController.removeScheduling(shiftTime, date,branch, hrController.getEmployee(employeeID));
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateShiftManager(ShiftTime shiftTime, Date date,String branch, int employeeID) {//DONE
        try {
            hrController.updateShiftManager(shiftTime, date,branch, hrController.getEmployee(employeeID));
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addNewShift(ShiftTime shiftTime, Date date,String branch, int employeeID) {//DONE
        try {
            hrController.addNewShift(shiftTime, date,branch, hrController.getEmployee(employeeID));
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response setPosition(ShiftTime shiftTime, Date date,String branch, String jobName, int quantity) {//DONE

        try {
            hrController.isJobExists(jobName);
            hrController.setPosition(shiftTime, date,branch, jobName, quantity);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }

    }

    public Response addShiftPosition(ShiftTime shiftTime, Date date,String branch, String jobName, int quantity) {//DONE
        try {
            hrController.isJobExists(jobName);
            hrController.addShiftPosition(shiftTime, date,branch, jobName, quantity);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeShiftPosition(ShiftTime shiftTime, Date date,String branch, String jobName) {//DONE
        try {
            hrController.removeShiftPosition(shiftTime, date,branch, jobName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response<String> getNumberOfShiftsStatistics(String branch) {//DONE
        try {
            String str = hrController.getNumberOfShiftsStatistics(branch);
            Response<String> response = new Response();
            response.setValue(str);
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> getNumberOfEveningShiftsStatistics(String branch) {
        try {
            String str = hrController.getNumberOfEveningShiftsStatistics(branch);
            Response<String> response = new Response();
            response.setValue(str);
            return response;

        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> getNumberOfMorningShiftsStatistics(String branch) {
        try {
            String str = hrController.getNumberOfMorningShiftsStatistics(branch);
            Response<String> response = new Response();
            response.setValue(str);
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<LinkedList<String>> showEmployees() {
        try {
            return new Response<>(hrController.showEmployees());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateSalary(int id, double newSalary) {
        try {
            hrController.updateSalary(id, newSalary);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<HashMap<Integer, String>> showForEachEmployeeHisJob() {
        try {
            return new Response<>(hrController.showForEachEmployeeHisJob());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response addSocialBenefits(int id, String socialBenefits) {
        try {
            hrController.addSocialBenefits(id, socialBenefits);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response addNewSocialBenefits(int id, String socialBenefits) {
        try {
            hrController.addNewSocialBenefits(id, socialBenefits);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateFirstName(int id, String newFirstName) {
        try {
            hrController.updateFirstName(id, newFirstName);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response updateLastName(int id, String newLastName) {
        try {
            hrController.updateLastName(id, newLastName);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response updateIsShiftManager(int id, boolean isShiftManager) {
        try {
            hrController.updateIsShiftManager(id, isShiftManager);
            return new Response<>();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> getHistory() {
        try {
            String str = hrController.getHistory();
            Response<String> response = new Response();
            response.setValue(str);
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
    public Response<String> getShiftPlacements(Date date, ShiftTime shiftTime, String branch) {
        try {
            String str = hrController.getShiftPlacements(date,shiftTime,branch);
            Response<String> response = new Response();
            response.setValue(str);
            return response;

        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
    public Response<String> getEmployeeConstrains(int employeeID) {
        try {
            String str = hrController.getEmployeeConstrains(employeeID);
            Response<String> response = new Response();
            response.setValue(str);
            return response;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
    public Response scheduleDriver() {
        try {
            hrController.scheduleDriver();
            return new Response();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


}
