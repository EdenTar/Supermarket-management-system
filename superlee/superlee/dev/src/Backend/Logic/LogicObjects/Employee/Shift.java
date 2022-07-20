package Backend.Logic.LogicObjects.Employee;

import Backend.DataAccess.DAOs.EmployeeDAOS.AssignmentDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.JobDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.PlacementDAO;
import Backend.DataAccess.DAOs.EmployeeDAOS.ShiftDAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.AssignmentDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.PlacementDTO;
import Backend.Logic.LogicObjects.Jobs.Employee;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shift {
    private AssignmentDAO assignmentDAO;
    private JobDAO jobDAO;
    private PlacementDAO placementDAO;
    private ShiftDAO shiftDAO;
    private Date shiftDate;
    private ShiftTime shiftTime;
    private String branch;
    private Employee shiftManager;

    public Shift(Date shiftDate,ShiftTime shiftTime,String branch ,Employee employee) {
        assignmentDAO=new AssignmentDAO();
        placementDAO=new PlacementDAO();
        jobDAO=new JobDAO();
        shiftDAO=new ShiftDAO();
        this.shiftTime = shiftTime;
        this.shiftManager = employee;
        this.shiftDate = shiftDate;
        this.branch=branch;
        addBasics();
    }
    public void addBasics(){
        Assingment ass1=new Assingment(shiftDate,shiftTime,branch,"cashier",10,0);
        Assingment ass2= new Assingment(shiftDate,shiftTime,branch,"grocery sorter",10,0);
        Assingment ass3=new Assingment(shiftDate,shiftTime,branch,"storekeeper",10,0);
        Assingment ass4=new Assingment(shiftDate,shiftTime,branch,"driver",10,0);
        if(assignmentDAO.getRow(AssignmentDTO.getPK(ass1.getStrDate(),ass1.getShiftTime().toString(),ass1.getBranch(),ass1.getJob()))==null)
            assignmentDAO.insert(ass1);
        if(assignmentDAO.getRow(AssignmentDTO.getPK(ass2.getStrDate(),ass2.getShiftTime().toString(),ass2.getBranch(),ass2.getJob()))==null)
            assignmentDAO.insert(ass2);
        if(assignmentDAO.getRow(AssignmentDTO.getPK(ass3.getStrDate(),ass3.getShiftTime().toString(),ass3.getBranch(),ass3.getJob()))==null)
            assignmentDAO.insert(ass3);
        if(assignmentDAO.getRow(AssignmentDTO.getPK(ass4.getStrDate(),ass4.getShiftTime().toString(),ass4.getBranch(),ass4.getJob()))==null)
            assignmentDAO.insert(ass4);
    }

    //add an employee to a shift
    public void addPlacement(String jobName, Employee employee) throws Exception {
        if(placementDAO.getRow(PlacementDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getEmployeeId()))!=null)
            throw new Exception("This employee is already in this shift.");
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,jobName));
        if(assingment==null)
            throw new Exception("This job does not exists in the shift. Please add it first.");
        if( assingment.getQuantity()>=assingment.getCapacity())
            throw new Exception("All the positions of this job are full.");
        placementDAO.insert(new Placement(getShiftDate(),shiftTime,branch,employee.getEmployeeId(),jobName));
        assignmentDAO.update(new Assingment(shiftDate,shiftTime,branch,jobName,assingment.getCapacity(),assingment.getQuantity()+1));
    }

    public void setShiftManager(Employee employee) throws Exception {
        Placement placement=placementDAO.getRow(PlacementDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getEmployeeId()));
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getJob().getJobName()));
        if(placement==null)
            throw new Exception("This employee is not in this shift. Add him first.");
        if( placement==null && assingment.getQuantity()>=assingment.getCapacity())
            throw new Exception("Cant set shift manager. This employee is not in this shift and there is no free position for him. ");
        this.shiftManager=employee;
        shiftDAO.update(this);
    }

    public ShiftTime getShiftTime() {
        return shiftTime;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void deletePlacementOfEmployee(Employee employee) throws Exception {
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getJob().getJobName()));
        if(assingment==null)
            throw new Exception("Employee's job does not exists in the shift");
        Placement placement=placementDAO.getRow(PlacementDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getEmployeeId()));
        if(placement==null)
            throw new Exception("This employee is not placed in this shift.");
        placementDAO.deleteRow(placement);
        assignmentDAO.update(new Assingment(shiftDate,shiftTime,branch,employee.getJob().getJobName(),assingment.getCapacity(),assingment.getQuantity()-1));
    }

    public void addShiftPosition(String jobName, int capacity) throws Exception{
        if(shiftTime.equals(ShiftTime.Evening) && isManager(jobName))
            throw new Exception("A manager can't be assigned to evening shift");
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,jobName));
        if(assingment!=null)
            throw new Exception("This position is already exists");
        if(capacity<1)
            throw new Exception("Invalid quantity number");
        assignmentDAO.insert(new Assingment(shiftDate,shiftTime,branch,jobName,capacity,0));

    }

    public void removeShiftPosition(String jobName) throws Exception{
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,jobName));
        if(assingment==null)
            throw new Exception("This position is does not exists");
        if (jobName.equals("cashier") || jobName.equals("grocery sorter") || jobName.equals("storekeeper") || jobName.equals("driver"))
            throw new Exception("This position can't be removed");
        if (jobName.equals(shiftManager.getJob().getJobName()))
            throw new Exception("Shift manager has this position. If you want to delete it, please assign new shift manager");
        assignmentDAO.deleteRow(assingment);
        placementDAO.deleteRows("job='"+jobName+"'");
    }

    public void setPosition(String jobName, int capacity) throws Exception {
        if (jobName.equals("shift manager"))
            throw new Exception("Shift manager capacity can't be set");
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(getStrDate(),shiftTime.toString(),branch,jobName));
        if(assingment==null)
            throw new Exception("This position does not exists");
        if (capacity < 1)
            throw new Exception("Invalid capacity number");

        if (capacity < assingment.getCapacity())
            reducePositions(jobName, capacity,assingment);
        else if (capacity >assingment.getCapacity())
            increasePositions(jobName, capacity,assingment);

    }

    public void increasePositions(String jobName, int capacity,Assingment assingment) throws Exception {
        if(assingment==null)
            throw new Exception("This position does not exists");
        assignmentDAO.update(new Assingment(shiftDate,shiftTime,branch,jobName,capacity,assingment.getQuantity()));
    }

    public void reducePositions(String jobName, int capacity,Assingment assingment) throws Exception {
        if(assingment==null)
            throw new Exception("This position does not exists");
        if (assingment.getQuantity()>capacity)
            throw new Exception("The quantity is smaller than the number of workers assigned to this position. You need to remove workers from this shift job first");
        assignmentDAO.update(new Assingment(shiftDate,shiftTime,branch,jobName,capacity,assingment.getQuantity()));
    }

    private boolean isManager(String jobName) {
        if (!jobName.equals("Shift manager")) {
            return jobName.contains("manager");
        }
        return false;
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    public void ValidateRemoveScheduling(Employee employee) throws Exception {
        boolean isPlaced = isPlacedEmployee(employee);
        if (employee.getJob().equals("driver") && isPlaced)
            throw new Exception("This employee is this Driver. first Assign a new Driver.");
        if (employee.getJob().equals("storekeeper") && isPlaced)
            throw new Exception("This employee is this storekeeper. first Assign a new storekeeper.");
    }
    public boolean isPlacedEmployee(Employee employee) {
       Placement placement=placementDAO.getRow(PlacementDTO.getPK(getStrDate(),shiftTime.toString(),branch,employee.getEmployeeId()));
       return placement!=null;
    }
    public String getStrDate(){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
        String[] split=formatter.format(shiftDate).split("/");
        return split[2]+"-"+split[1]+"-"+split[0];
    }

    public String getBranch(){return  branch;}

    }

