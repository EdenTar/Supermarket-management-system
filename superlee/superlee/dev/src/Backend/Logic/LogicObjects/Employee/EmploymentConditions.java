package Backend.Logic.LogicObjects.Employee;


import Backend.DataAccess.DAOs.EmployeeDAOS.ConstrainsDAO;
import Backend.DataAccess.DTOs.EmployeeDTOS.ConstraintsDTO;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmploymentConditions {
    private int employeeId;
    private double salary;
    private String socialBenefits;
    private ConstrainsDAO constrainsDAO;


    public EmploymentConditions(int employeeId, double salary, String socialBenefits){
        this.salary=salary;
        this.socialBenefits=socialBenefits;
        this.employeeId = employeeId;
        constrainsDAO=new ConstrainsDAO();
        clearConstraints();
        this.employeeId=employeeId;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setNewSocialBenefits(String socialBenefits) {
        this.socialBenefits = socialBenefits;
    }
    public void addSocialBenefits(String socialBenefits) {
        this.socialBenefits = this.socialBenefits + " " + socialBenefits;
    }
    public void addConstraints(Date date, ShiftTime shiftTime) throws Exception {
        if(constrainsDAO.getRow(ConstraintsDTO.getPK(employeeId,getStrDate(date),shiftTime.toString()))!=null)
            throw new Exception("This constraint is already exists");
        constrainsDAO.insert(new Constraint(employeeId,date,shiftTime));
    }
    public void deleteConstraints(Date date, ShiftTime shiftTime) throws Exception {
        Constraint toDelete=constrainsDAO.getRow(ConstraintsDTO.getPK(employeeId,getStrDate(date),shiftTime.toString()));
        if(toDelete==null)
            throw new Exception("This constraint does not exists");
        constrainsDAO.deleteRow(toDelete);
    }
    public String getStrDate(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
        String[] split=formatter.format(date).split("/");
        return split[2]+"-"+split[1]+"-"+split[0];
    }
    private class Task extends TimerTask {
        public void run() {
            if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)== Calendar.THURSDAY)
                constrainsDAO.deleteAllRecords();
        }
    }

    public void clearConstraints(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        TimerTask task = new EmploymentConditions.Task();
        scheduler.scheduleAtFixedRate(task,24,24, TimeUnit.HOURS);
    }

    public double getSalary() {
        return salary;
    }

    public String getSocialBenefits() {
        return socialBenefits;
    }

    /*public String toStringConstrains(String branch){
        String str="";
     List<Constraints> constraints=constrainsDAO.getRowsFromDB("branch=\""+branch+"\"");
        for (Constraints c:constraints)
            str+="#Date: "+ getStrDate(c.getDate())+" "+c.getShiftTime().toString() ;
        return str;
    }

    public int getEmployeeId() {
        return employeeId;
    }*/
    public String toStringConstrains(int id){
        String str="";
        List<Constraint> constraints=constrainsDAO.getRowsFromDB("employeeId=\""+id+"\"");
        for (Constraint c:constraints)
            str+="#Date: "+ getStrDate(c.getDate())+" "+c.getShiftTime().toString()+"\n" ;
        return str;
    }
    /*public List<Constraints> getConstraints(int id){
       return constrainsDAO.getRowsFromDB("employeeId=\""+id+"\"");
    }*/
    public boolean isAvailable(int id, String date, ShiftTime shiftTime){
        String[] split=date.split("/");
        String newDate= split[2]+"-"+split[1]+"-"+split[0];
        Constraint constraint=constrainsDAO.getRow(ConstraintsDTO.getPK(id,newDate,shiftTime.toString()));
        return constraint!=null;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
