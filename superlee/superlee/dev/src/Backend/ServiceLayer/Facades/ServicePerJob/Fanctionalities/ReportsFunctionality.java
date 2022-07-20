package Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities;

import Backend.Logic.Controllers.Stock.ReportBuilder;
import Backend.Logic.Starters.Starter;
import Backend.ServiceLayer.ServiceObjects.Report.SReport;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.Result.ValueResult;

import java.util.List;

public interface ReportsFunctionality {
    ReportBuilder reportBuilder = Starter.getInstance().getReportBuilder();

    //get reports functions:
    default Result<SReport> getStockReport(List<List<String>> categories) {
        try {
            return new ValueResult<>(new SReport(this.reportBuilder.buildStockReport(categories)));
        }catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }

    default Result<SReport> getDamageReport() {
        try {
            return new ValueResult<>(new SReport(this.reportBuilder.buildDamagedReport()));
        }catch (Exception e){
            return new ErrorResult<>(e.getMessage());
        }
    }
}
