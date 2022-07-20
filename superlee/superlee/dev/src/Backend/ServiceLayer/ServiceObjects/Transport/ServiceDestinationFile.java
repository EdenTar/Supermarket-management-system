package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.LogicObjects.Transport.DestinationFile;
import Backend.Logic.Points.Point;


import java.util.List;
import java.util.stream.Collectors;

public class ServiceDestinationFile {
    private final String id;
    private final Point destination;
    private final Point source;
    private final boolean isDone;
    private final List<TransportItemService> transportItems;

    public ServiceDestinationFile(DestinationFile destinationFile) {
        this.id = destinationFile.getId();
        this.destination = destinationFile.getDestination();
        this.source = destinationFile.getSource();
        this.isDone = destinationFile.isDone();
        this.transportItems = destinationFile.
                getTransportItems().stream().
                map(TransportItemService::new).
                collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public Point getDestination() {
        return destination;
    }

    public Point getSource() {
        return source;
    }

    public boolean isDone() {
        return isDone;
    }

    public List<TransportItemService> getTransportItems() {
        return transportItems;
    }

    @Override
    public String toString() {
        return "DestinationFile:\n" +
                "id='" + id + '\'' +
                ", destination=" + destination +
                ", source=" + source +
                ", isDone=" + isDone +
                ", transportItems=" + transportItems +
                '\n';
    }

    public String toStringWithoutItems() {
        return "DestinationFile{" +
                "id='" + id + '\'' +
                ", destination=" + destination +
                ", source=" + source +
                ", isDone=" + isDone +
                '}';
    }
}
