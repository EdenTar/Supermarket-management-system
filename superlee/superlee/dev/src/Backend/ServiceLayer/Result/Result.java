package Backend.ServiceLayer.Result;

public interface Result<T> {

    public boolean errorOccurred();
    public T getValue();
    public String getError();

}
