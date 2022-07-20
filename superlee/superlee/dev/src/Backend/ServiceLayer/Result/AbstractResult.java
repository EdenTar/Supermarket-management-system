package Backend.ServiceLayer.Result;

public abstract class AbstractResult<T> implements Result<T> {

    private T value;
    private String error;

    public AbstractResult(String error){
        this.error = error;
    }

    public AbstractResult(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    public boolean errorOccurred(){return error != null;}
}
