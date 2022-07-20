package Backend.ServiceLayer.ServiceObjects.Report;

import Backend.Logic.LogicObjects.Report.DamagedProductInfo;
import Backend.Logic.LogicObjects.Report.Report;
import Backend.Logic.LogicObjects.Report.StockProductInfo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SReport {


    private Date date;
    private List<SProductInfo> products;

    public SReport(Date date, List<SProductInfo> products) {
        this.date = date;
        this.products = products;
    }

    public SReport(Report report){
        this(report.getDate(), report.getProducts().stream().map(info -> (info instanceof DamagedProductInfo) ?
                new SDamagedProductInfo((DamagedProductInfo) info) :
                new SStockProductInfo((StockProductInfo) info)).collect(Collectors.toList()));
    }

    public Date getDate() {
        return date;
    }

    public List<SProductInfo> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return String.format("[Creation date: %s\n\nProductNumber, Name, Manufacturer, Quantity, Store Quantity, Warehouse quantity\n\nProduct Info:\n%s]", getDate().toString(), printList(getProducts().stream().map(p -> p.toString() + "\n\n").collect(Collectors.toList())));
    }

    public String flawedToString(){
        return String.format("[Creation date: %s\n\nProductNumber, Name, Manufacturer, Quantity, Flawed quantity\n\nProduct Info:\n%s]", getDate().toString(), printList(getProducts().stream().map(p -> (((SDamagedProductInfo)p).flawedToString()+ "\n\n")).collect(Collectors.toList())));
    }

    public String expiredToString(){
        return String.format("[Creation date: %s\n\nProductNumber, Name, Manufacturer, Quantity, Expired quantity\n\nProduct Info:\n%s]", getDate().toString(), printList(getProducts().stream().map(p -> (((SDamagedProductInfo)p).expiredToString() + "\n\n")).collect(Collectors.toList())));
    }

    private  <T> String printList(List<T> lst){
        String res = "<";
        int i = 0;
        for(T t : lst) {
            if(!t.toString().equals("\n\n")) {
                res += t.toString() + ((i++ == lst.size() - 1) ? "" : ",");
            }
        }
        return res + ">";
    }
}
