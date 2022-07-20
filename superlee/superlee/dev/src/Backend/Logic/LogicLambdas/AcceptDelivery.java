package Backend.Logic.LogicLambdas;

import Backend.Logic.LogicObjects.Transport.TransportItem;

import java.util.List;

public interface AcceptDelivery {
    public void acceptDelivery(List<TransportItem> arrival);
}
