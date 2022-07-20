package Backend.Logic.LogicObjects.Report;

import java.util.Date;
import java.util.List;

public class Report {
    private Date date;
    private List<ProductInfo> products;
    public Report(Date date, List<ProductInfo> products){
        this.date = date;
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

}
