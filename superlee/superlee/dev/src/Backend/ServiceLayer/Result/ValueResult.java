package Backend.ServiceLayer.Result;

public class ValueResult<T> extends AbstractResult<T>{

    public ValueResult(T value){
        super(value);
    }

}
