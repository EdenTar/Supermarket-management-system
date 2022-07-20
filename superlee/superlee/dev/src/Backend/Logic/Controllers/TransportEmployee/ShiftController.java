package Backend.Logic.Controllers.TransportEmployee;

import Backend.DataAccess.DAOs.EmployeeDAOS.*;
import Backend.DataAccess.DAOs.TransportDAOs.DriverDAO;
import Backend.DataAccess.DAOs.TransportDAOs.PointDAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.AssignmentDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.EmployeeDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.PlacementDTO;
import Backend.DataAccess.DTOs.EmployeeDTOS.ShiftDTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.TransportDTOS.DriverDTO;
import Backend.Logic.LogicObjects.Jobs.BasicEmployee;
import Backend.Logic.LogicObjects.Jobs.Driver;
import Backend.Logic.LogicObjects.Jobs.Employee;
import Backend.Logic.LogicObjects.Employee.*;
import Backend.Logic.Points.Branch;
import Backend.Logic.Points.Point;
import Backend.Logic.LogicLambdas.DriversInShift;
import Backend.Logic.LogicLambdas.IsStoreKeeperInShift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShiftController {
    private ShiftDAO shiftDAO;
    private EmployeeDAO employeeDAO;
    private PlacementDAO placementDAO;
    private ConstrainsDAO constrainsDAO;
    private AssignmentDAO assignmentDAO;
    private NotificationDAO notificationDAO;
    private final String stock_keeper = "stock keeper";
    private long notificationIndex;



    public ShiftController() {
        shiftDAO = new ShiftDAO();
        employeeDAO=new EmployeeDAO();
        placementDAO=new PlacementDAO();
        constrainsDAO= new ConstrainsDAO();
        assignmentDAO=new AssignmentDAO();
        notificationDAO=new NotificationDAO();
        notificationIndex=0;
        updateHistory();
    }

    private Date deadlineDate() {
        Date currDate = new Date();
        int day = currDate.getDay();
        int add = 7 - day + 6;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(currDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, add);
        return c.getTime();
    }

    public void addScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee, String jobName,boolean driversScheduling) throws Exception {//Done

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
      //  if (!driversScheduling && day < 6)
           // throw new Exception("It is not yet possible schedule employees. Try again on Friday.");
        if (date.compareTo(deadlineDate()) > 0)
            throw new Exception("You can only schedule employee to the next week.");
        if (!employee.getJob().getJobName().equals(jobName.toLowerCase()))
            throw new Exception("Employees job does not match.");
        if (!employee.getJob().getJobName().equalsIgnoreCase(jobName))
            throw new Exception("The job of the entered employee does not match the required job.");
        if (date.compareTo(new Date()) < 0)
            throw new Exception("This date has already been");
        if (!employee.isAvailable(getStrDate(date),shiftTime))
            throw new Exception("This employee is not available for this shift.");
/*        if(employee instanceof OrderMan && !employee.getBranchAddress().equals(branch))
            throw new Exception("This employee is not working in this branch");*/
        else if(employee instanceof BasicEmployee && !employee.getBranchAddress().equals(branch))
            throw new Exception("This employee is not working in this branch");
        getShift(date, shiftTime, branch).addPlacement(jobName.toLowerCase(), employee);
    }


    public void removeScheduling(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {//DONE

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day < 6)
            throw new Exception("It is not yet possible to remove scheduled employees. Try again on Friday.");
        if (employee == null || date == null || shiftTime == null)
            throw new Exception("The values entered are invalid.");
        if (date.compareTo(deadlineDate()) > 0)
            throw new Exception("You can only remove schedule employee to the next week.");
        if (date.compareTo(new Date()) < 0)
            throw new Exception("This date has already been");
        if (getShift(date, shiftTime, branch).getShiftManager().equals(employee))
            throw new Exception("This employee is this shift's manager. first Assign a new shift manager.");
        getShift(date, shiftTime, branch).ValidateRemoveScheduling(employee);
        getShift(date, shiftTime, branch).deletePlacementOfEmployee(employee);
    }

    public void updateShiftManager(ShiftTime shiftTime, Date date, String branch, Employee employee) throws Exception {

        if (!employee.getIsShiftManager())
            throw new Exception("This employee does not have the required authorization.");
        if (!employee.isAvailable(getStrDate(date),shiftTime) )
            throw new Exception("This shift manager is not available for this shift");
        getShift(date, shiftTime, branch).setShiftManager(employee);
    }

    public void addNewShift(ShiftTime shiftTime, Date date, String branch, Employee shiftManager) throws Exception {//DONE
        String dateStr = getStrDate2(date);
        PK spk = ShiftDTO.getPK(dateStr, shiftTime.toString(), branch);

        if (shiftManager == null || date == null || shiftTime == null)
            throw new Exception("The values entered are invalid.");
        if (!shiftManager.getIsShiftManager())
            throw new Exception("This employee can't be shift manager. He does not have the required authorization.");
        if (!shiftManager.isAvailable(getStrDate(date),shiftTime))
            throw new Exception("This shift manager is not available for this shift");
        if (shiftDAO.getRow(spk) != null)
            throw new Exception("This shift already exist.");
        shiftDAO.insert(new Shift(date, shiftTime, branch, shiftManager));
        // addScheduling(shiftTime,date,branch,shiftManager,shiftManager.getJob().getJobName());
        placementDAO.insert(new Placement(date,shiftTime,branch,shiftManager.getEmployeeId(),shiftManager.getJob().getJobName()));
        Assingment assingment=assignmentDAO.getRow(AssignmentDTO.getPK(dateStr,shiftTime.toString(),branch,shiftManager.getJob().getJobName()));
        assignmentDAO.update(new Assingment(date,shiftTime,branch,shiftManager.getJob().getJobName(),assingment.getCapacity(),assingment.getQuantity()+1));

    }



    public void addShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {
        try {
            Integer.parseInt(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            throw new Exception("Quantity is not a number.");
        }
        getShift(date, shiftTime, branch).addShiftPosition(jobName.toLowerCase(), quantity);
    }

    public void removeShiftPosition(ShiftTime shiftTime, Date date, String branch, String jobName) throws Exception {

        getShift(date, shiftTime, branch).removeShiftPosition(jobName.toLowerCase());
    }


    public void setPosition(ShiftTime shiftTime, Date date, String branch, String jobName, int quantity) throws Exception {

        if (date == null || shiftTime == null)
            throw new Exception("The values entered are invalid.");
        try {
            Integer.parseInt(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            throw new Exception("Quantity is not a number.");
        }
        getShift(date, shiftTime, branch).setPosition(jobName.toLowerCase(), quantity);
    }

    public Shift getShift(Date date, ShiftTime shiftTime, String branch) throws Exception {
        Shift shift = shiftDAO.getRow(ShiftDTO.getPK(convertBusinessDateToDB(getStrDate(date)), shiftTime.toString(), branch));
        if (shift == null)
            throw new Exception("The shift does not exist.");
        return shift;
    }

    public String getHistory() throws Exception {
        StringBuilder str= new StringBuilder();
        List<Shift> shifts=shiftDAO.getRowsFromDB();
        for (Shift s : shifts){
            str.append(s.getStrDate()).append(" ").append(s.getShiftTime().toString()).append(" :\n");
            List<Placement> placements=placementDAO.getRowsFromDB("date='"+s.getStrDate()+"'" , "shiftTime='"+s.getShiftTime().toString()+"'");
            for (Placement p : placements)
                str.append(p.getStrDate()).append(" , ").append(p.getShiftTime().toString()).append(" , ").append(p.getBranch()).append(" , ").append(p.getEmployeeId()).append(" , ");
        }
        return str.toString();
    }
    public IsStoreKeeperInShift isStoreKeeperInShiftLambda() {
        return (branch, date) -> isStoreKeeperInShiftShiftController(branch, date, date.getHours()>=14 ? "Evening": "Morning");
    }

    public boolean isStoreKeeperInShiftShiftController(Branch branch, Date date, String ShiftTime) {
        List<Placement> placements = placementDAO.getPlace(branch, date, ShiftTime);
        for (Placement placement : placements) {
            if (employeeDAO.getRow(EmployeeDTO.getPK(placement.getEmployeeId())).getJob().getJobName().equals(stock_keeper))
                return true;
        }
        return false;
    }

    public void deleteAllConstraints(int id) {
        constrainsDAO.deleteRows("employeeId = "+id);
    }


    private class Task extends TimerTask {
        public void run() {
           /* for (Date date : shifts.keySet()) {
                if (date.compareTo(new Date()) < 0) {
                    if (shifts.get(date)[0] != null)
                        history.add(shifts.get(date)[0]);
                    if (shifts.get(date)[1] != null)
                        history.add(shifts.get(date)[1]);
                    shifts.remove(date);
                }

            }*/

        }
    }

    public void updateHistory() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        TimerTask task = new Task();
        scheduler.scheduleAtFixedRate(task, 12, 12, TimeUnit.HOURS);
    }

    /*public int getEmployeeNumberOfShifts(Employee employee) throws Exception {
        int counter = 0;
        for (Shift shift : history) {
            if (ChronoUnit.DAYS.between(shift.getShiftDate().toInstant(), new Date().toInstant()) <= 30) {
                Employee[] inlays = shift.getPlacements().get(employee.getJob().toLowerCase());
                for (Employee inlay : inlays)
                    if (inlay.equals(employee))
                        counter++;
            }
        }
        return counter;
    }*/

    public String getNumberOfShiftsStatistics(String branch) throws Exception {

        StringBuilder returnedStr = new StringBuilder();
        List<Employee> employees= employeeDAO.getRowsFromDB("branch=\""+branch+"\"");
        for (Employee e:employees) {
            List<Placement> placements=placementDAO.getRowsFromDB("employeeId=\""+e.getEmployeeId()+"\"");
            returnedStr.append(e.getEmployeeId()).append(" : ").append(placements.size()).append("\n");
        }
        return returnedStr.toString();
    }

   /* public int getEmployeeNumberOfMorningShifts(Employee employee) throws Exception {
        int counter = 0;
        for (Shift shift : history) {
            if (shift.getShiftTime().equals(ShiftTime.Morning) && ChronoUnit.DAYS.between(shift.getShiftDate().toInstant(), new Date().toInstant()) <= 30) {
                Employee[] inlays = shift.getPlacements().get(employee.getJob().toLowerCase());
                for (Employee inlay : inlays)
                    if (inlay.equals(employee))
                        counter++;
            }
        }
        return counter;
    }*/

    public String getNumberOfMorningShiftsStatistics(String branch) throws Exception {
        StringBuilder returnedStr = new StringBuilder();
        List<Employee> employees= employeeDAO.getRowsFromDB("branch=\""+branch+"\"");
        for (Employee e:employees) {
            List<Placement> placements=placementDAO.getRowsFromDB("employeeId=\""+e.getEmployeeId()+"\" AND shiftTime=\"Morning\"");
            returnedStr.append(e.getEmployeeId()).append(" : ").append(placements.size()).append("\n");
        }
        return returnedStr.toString();
    }
    /*public int getEmployeeNumberOfEveningShifts(Employee employee) throws Exception {
        int counter = 0;
        for (Shift shift : history) {
            if (shift.getShiftTime().equals(ShiftTime.Evening) && ChronoUnit.DAYS.between(shift.getShiftDate().toInstant(), new Date().toInstant()) <= 30) {
                Employee[] inlays = shift.getPlacements().get(employee.getJob().toLowerCase());
                for (Employee inlay : inlays)
                    if (inlay.equals(employee))
                        counter++;
            }
        }
        return counter;

    }*/

    public String getNumberOfEveningShiftsStatistics(String branch) throws Exception {

        StringBuilder returnedStr = new StringBuilder();
        List<Employee> employees= employeeDAO.getRowsFromDB("branch=\""+branch+"\"");
        for (Employee e:employees) {
            List<Placement> placements=placementDAO.getRowsFromDB("employeeId=\""+e.getEmployeeId()+"\" AND shiftTime=\"Evening\"");
            returnedStr.append(e.getEmployeeId()).append(" : ").append(placements.size()).append("\n");
        }
        return returnedStr.toString();
    }

    public String getStrDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    public String getStrDate2(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    public DriversInShift getDriversInShift(){
        return this::getScheduledDrivers;
    }

    public LinkedList<Driver> getScheduledDrivers(Date date){
        LinkedList<Driver> scheduled=new LinkedList<>();
        DriverDAO driverDAO=new DriverDAO();
        String con1="job=\"driver\"";
        String con2="date=\""+convertBusinessDateToDB(getStrDate(date))+"\" ";
        String con3="shiftTime=\""+getTime(date.getHours())+"\"";
        List<Placement> placements=placementDAO.getRowsFromDB_( con1, con2,con3);
        for (Placement p:placements) {
            scheduled.add(driverDAO.getRow(DriverDTO.getPK(p.getEmployeeId())));
        }
        return scheduled;
    }

    public String convertBusinessDateToDB(String date){
        String[] split=date.split("/");
        return split[2]+"-"+split[1]+"-"+split[0];
    }
    private String getTime(int hours){
        return hours<14 ? "Morning" : "Evening";
    }

    public boolean isAssignedNextWeek(Employee employee) throws Exception {
        PointDAO pointDAO = new PointDAO();
        List<Point> branches = pointDAO.getRowsFromDB("tag = 'branch'");
        for (Point branch : branches) {
            if (caseByBranchEmployee(employee, branch.getAddress()))
                return true;
        }
        return false;
    }
    private boolean caseByBranchEmployee(Employee employee, String branch){

        int DAY = 1000 * 60 * 60 * 24;
        Date date = new Date();
        while (date.compareTo(deadlineDate())<0) {
            String dat121212 = convertToFormat(date);
            Placement p = placementDAO.getRow(PlacementDTO.getPK(convertToFormat(date), "Morning", branch, employee.getEmployeeId()));
            if (p != null)
                return true;
            p = placementDAO.getRow(PlacementDTO.getPK(convertToFormat(date), "Evening", branch, employee.getEmployeeId()));
            if (p != null)
                return true;
            date = new Date(date.getTime() + DAY);
        }
        return false;
    }

    public String getShiftPlacements(Date date, ShiftTime shiftTime, String branch){
        String str="";
        Shift shift=shiftDAO.getRow(ShiftDTO.getPK(convertToFormat(date), shiftTime.equals(ShiftTime.Morning) ? "Morning": "Evening",branch));
        str+=shift.getStrDate()+" "+shift.getShiftTime().toString()+" in "+branch+" :\n";
        str+="The shift manager is : "+shift.getShiftManager().getEmployeeId()+" , "+shift.getShiftManager().getEmployeeName() +" "+shift.getShiftManager().getEmployeeLastName() +" , "+shift.getShiftManager().getJob().getJobName()+"\n";
        List<Placement> placements=placementDAO.getRowsFromDB("date = \'"+shift.getStrDate()+"\' AND shiftTime = \'"+shift.getShiftTime().toString()+"\' AND branch = \'"+branch+"\'");
        for (Placement p : placements) {
            Employee e = employeeDAO.getRow(EmployeeDTO.getPK(p.getEmployeeId()));
            str += e.getEmployeeId()+" , "+e.getEmployeeName() +" "+e.getEmployeeLastName() +" , "+e.getJob().getJobName() + "\n";

        }
        return str;
    }
    private String convertToFormat(Date startingDate) {
        if(startingDate.getMonth()+1<10)
            return "" + (startingDate.getYear() + 1900) + "-0" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
        else
            return "" + (startingDate.getYear() + 1900) + "-" + (startingDate.getMonth() +1) + "-" + startingDate.getDate();
    }

    public void NotifyForDrivers(List<Date> dateList){
        String dateSet="";
        for(Date d:dateList)
        {
            dateSet+=getStrDate(d)+"*";
        }
        notificationDAO.insertRow(dateSet);
    }

   /* public LinkedList<Driver> getAvailableDrivers(Date date){
        LinkedList<Integer> ids=new LinkedList<>();
        LinkedList<Driver> available=new LinkedList<>();
        DriverDAO driverDAO=new DriverDAO();
        List<Driver> drivers=driverDAO.getRowsFromDB();
        for (Employee d:drivers){
            ids.add(d.getEmployeeId());
        }
        String con1="job=\"driver\"";
        String con2="date=\""+convertBusinessDateToDB(getStrDate(date))+"\" ";
        String con3="shiftTime=\""+getTime(date.getHours())+"\"";
        List<Placement> placements=placementDAO.getRowsFromDB_( con1, con2,con3);
        for (Placement p:placements) {
            ids.remove(p.getEmployeeId());
        }
        con1="date=\""+convertBusinessDateToDB(getStrDate(date))+"\" ";
        con2= "shiftTime=\""+getTime(date.getHours())+"\"";
        List<Constraint> constraints=constrainsDAO.getRowsFromDB_(con1,con2);
        for (Constraint c:constraints) {
            if(ids.contains(c.getEmployeeId()))
                available.add(driverDAO.getRow(DriverDTO.getPK(c.getEmployeeId())));
        }
        return available;
    }*/

    public void scheduleDriver() throws Exception {
        Notification notification=notificationDAO.getFirstRow();
        List<Date> dates= getListOfDates(notification.getDateSet());
        List<Employee> drivers= getDrivers();
        boolean scheduled=false;
        for (Date d:dates)
        {
            for(Employee driver:drivers){
                try {
                    addScheduling(ShiftTime.Morning,d,"branch1",driver,"driver",true);
                    scheduled=true;
                    notificationDAO.deleteRowNotif(notification.getDateSet());
                    break;
                }
                catch (Exception e){
                }
                if(!scheduled)
                    try {
                        addScheduling(ShiftTime.Evening,d,"branch1",driver,"driver",true);
                        scheduled=true;
                        notificationDAO.deleteRowNotif(notification.getDateSet());
                        break;
                    }
                    catch (Exception e){
                    }
                if(scheduled)
                    break;
            }

        }
        if(!scheduled) {
            notificationDAO.deleteRowNotif(notification.getDateSet());
            throw new Exception("There is no available driver to be scheduled");
        }

    }
    private List<Employee> getDrivers(){
        return employeeDAO.getRowsFromDB("job='driver'");
    }
    /*/*LinkedList<Driver> drivers= getAvailableDrivers(d);
            if (drivers.size()>0)
            {
                try {
                    addScheduling(ShiftTime.Morning, d, drivers.get(0).getBranchAddress(), drivers.get(0), "driver");
                }
                catch (Exception e){

                }
            }*/

    private List<Date> getListOfDates(String dateSet)  {
        String[] datesStr=dateSet.split("\\*");
        List<Date> dates=new LinkedList();
        for (String date:datesStr) {
            try {
                dates.add(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            }
            catch (Exception e){

            }
        }
        return dates;
    }
}
