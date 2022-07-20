package Backend.ServiceLayer.Result;

public class ErrorResult<T> extends AbstractResult<T>{

    public ErrorResult(String error){
        super(error);
    }


    @Override
    public boolean errorOccurred() {
        return true;
    }

}
