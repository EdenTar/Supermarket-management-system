package Backend.Logic.LogicObjects.Jobs;


import Backend.DataAccess.DAOs.EmployeeDAOS.EmployeeDAO;
import Backend.Logic.LogicObjects.Employee.BankAccount;
import Backend.Logic.LogicObjects.Employee.EmploymentConditions;
import Backend.Logic.LogicObjects.Employee.ShiftTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String employeeLastName;
    private String password;
    private Date startingDate;
    private Job job;
    private boolean isShiftManager;
    private BankAccount bankAccount;
    private EmploymentConditions employmentConditions;
    private String branchAddress;

    private final String GENERAL_PASS = "abc111";

    public Employee(int employeeId,String employeeName,String employeeLastName, Date startingDate, Job job,boolean isShiftManager,
                    BankAccount bankAccount,EmploymentConditions employmentConditions, String branchAddress){
        this.employeeName=employeeName;
        this.employeeLastName=employeeLastName;
        this.employeeId=employeeId;
        this.password=GENERAL_PASS;
        this.startingDate=startingDate;
        this.job=job;
        this.bankAccount=bankAccount;
        this.employmentConditions=employmentConditions;
        this.isShiftManager = isShiftManager;
        this.branchAddress = branchAddress;
    }
    public Employee(int employeeId,String employeeName,String employeeLastName,String password, Date startingDate, Job job,boolean isShiftManager,
                    BankAccount bankAccount,EmploymentConditions employmentConditions, String branchAddress){
        this.employeeName=employeeName;
        this.employeeLastName=employeeLastName;
        this.employeeId=employeeId;
        this.password=password;
        this.startingDate=startingDate;
        this.job=job;
        this.bankAccount=bankAccount;
        this.employmentConditions=employmentConditions;
        this.isShiftManager = isShiftManager;
        this.branchAddress = branchAddress;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public String getEmployeeLastName(){
        return this.employeeLastName;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public boolean isShiftManager() {
        return isShiftManager;
    }

    public String getGENERAL_PASS() {
        return GENERAL_PASS;
    }

    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if(!oldPassword.equals(this.password))
            throw new Exception("old password doesn't match");
        this.password = newPassword;
        new EmployeeDAO().update(this);
    }

    public String getPassword() {
        return password;
    }
    public String showDetails() {
        return  " Id:"+getEmployeeId()+" Name: "+getEmployeeName()+" "+getEmployeeLastName();
    }
    public Job getJob(){
        return job;
    }
    public boolean getIsShiftManager() { return isShiftManager; }

    public EmploymentConditions getEmploymentConditions(){return employmentConditions;}

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public String toString(){
        DateFormat dateFormat =  new SimpleDateFormat("dd MM yyyy");
        return "Employee ID: "+ this.employeeId+"  Full name: "+this.employeeName+" "+this.employeeLastName+"  Starting date: " +getStrDate(startingDate)+"  Job name: "+job.getJobName()
                +bankAccount.toString()+"  Salary: "+ employmentConditions.getSalary()+ "  Social benefits: "+employmentConditions.getSocialBenefits()+ "  Branch address: "+getBranchAddress();
    }

    public void addSocialBenefits(String socialBenefits) {
        employmentConditions.addSocialBenefits(socialBenefits);
    }

    private Date deadlineDate( )
    {
        Date currDate=new Date();
        int day=currDate.getDay();
        int add=0;
        if(day<=5)
            add=7-day+6;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(sdf.parse(sdf.format(currDate)));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, add);

        return c.getTime();
    }

    private  Date startingDate( )
    {
        Date currDate=new Date();
        int day=currDate.getDay();
        int add=7-day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(sdf.format(currDate)));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, add);
        String newDate = sdf.format(c.getTime());//TODO unused variable
        //System.out.println("Date after Addition: "+newDate);
        return c.getTime();
    }

    public void addConstraint(Date date, ShiftTime shiftTime) throws Exception {

        if(date== null || shiftTime == null)
            throw new Exception("The values entered are invalid.");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        dateConstraints(date, shiftTime, day);
        employmentConditions.addConstraints(date,shiftTime);
    }

    public void deleteConstraint(Date date, ShiftTime shiftTime) throws Exception {

        if(date== null || shiftTime == null)
            throw new Exception("The values entered are invalid.");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        dateConstraints(date, shiftTime, day);
        employmentConditions.deleteConstraints(date,shiftTime);
    }

    public String getStrDate(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    private void dateConstraints(Date date, ShiftTime shiftTime, int day) throws Exception {
       // if(day >5)
           // throw new Exception("You cant add or delete constraints today. Try again in sunday.");
        if(date.compareTo(new Date())<=0)
            throw new Exception("Date is invalid.");
        if(date.compareTo(startingDate())<0)
            throw new Exception("You can't add or delete constrain for this date anymore.");
       // if(date.compareTo(deadlineDate())>0)
           // throw new Exception("You can only apply or delete constraints to the next week.");
        if(shiftTime.equals(ShiftTime.Evening) && job.getJobName().contains("manager"))
            throw new Exception("A manager can't work at evening shifts");
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public void setNewSocialBenefits(String socialBenefits) {
        employmentConditions.setNewSocialBenefits(socialBenefits);
    }

    public void updateIsShiftManager(boolean isShiftManager) {
        this.isShiftManager = isShiftManager;
    }

   /* public String getEmployeeConstrains(){
        String str="";
                if(this instanceof HrManager || this instanceof TransportManager ||this instanceof Driver )
                   return employmentConditions.toStringConstrains("general",employeeId);
                if(this instanceof OrderMan)
                    return employmentConditions.toStringConstrains(((OrderMan)this).getBranchAddress(),employeeId);
                else
                    return employmentConditions.toStringConstrains(((OrderMan)this).getBranchAddress(),employeeId);
        }*/
    public String getEmployeeConstrains(){
        return employmentConditions.toStringConstrains(employeeId);
    }
    public boolean isAvailable( String date, ShiftTime shiftTime){
        return employmentConditions.isAvailable(employeeId,date,shiftTime);
    }

    public String getBranchAddress() {//TODO ooooooooooooooooooooo
        return branchAddress;
    }
}
