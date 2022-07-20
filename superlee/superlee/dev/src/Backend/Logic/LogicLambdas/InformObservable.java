package Backend.Logic.LogicLambdas;

public interface InformObservable {
    public void notifyAllObservers();
    public boolean checkIfCanNotify();

    public void addObserver(InformObserver observer);
    public void removeObserver(InformObserver observer);
}
