package Backend.ServiceLayer.Result;

public class Response<T> implements Result<T>{

    private T value;
    private String error;
    private boolean gotError;

    public Response(){
        this.gotError = false;
    }
    public Response(T value){
        this.value = value;
    }
    public Response(String error){
        this.error = error;
        gotError = true;
    }

    public boolean isGotError() {
        return gotError;
    }

    public String getError() {
        return error;
    }
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean errorOccurred() {return isGotError();}

    @Override
    public T getValue() {
        return value;
    }
}
