package Backend.ServiceLayer.ServiceObjects.Transport;

import Backend.Logic.LogicObjects.Transport.TransportFile;
import Backend.Logic.Points.Point;
import Backend.Logic.Points.Zone;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTransportFile {
    private final List<ServiceDestinationFile> destinationFiles;
    private final String comment;
    private final int fileId;
    private final boolean started;
    private final Date startDate;
    private final ServiceDriver driver;
    private final ServiceTruck truck;
    private final Point source;
    private final int startingWeight;
    private final Zone from;
    private final Zone to;

    public ServiceTransportFile(TransportFile transportFile) {
        this.destinationFiles = transportFile.getDestinationFiles().stream().
                map(ServiceDestinationFile::new).collect(Collectors.toList());
        this.fileId = transportFile.getFileId();
        this.started = transportFile.isStarted();
        this.startDate = transportFile.getStartDate();
        this.driver = new ServiceDriver(transportFile.getDriver());
        this.truck = new ServiceTruck(transportFile.getTruck());
        this.source = transportFile.getSource();
        this.startingWeight = transportFile.getStartingWeight();
        this.from = transportFile.getFrom();
        this.to = transportFile.getTo();
        this.comment = transportFile.getComment();

    }

    @Override
    public String toString() {
        return "TransportFile:\n" +
                "destinationFiles=\n" + destinationFiles +
                ", fileId=" + fileId +
                ", started=" + started +
                ", startDate=" + startDate +
                ", driver=" + driver +
                ", truck=" + truck +
                ", source=" + source +
                ", startingWeight=" + startingWeight +
                ", from=" + from +
                ", to=" + to +
                ", comment=" + comment +
                '}';
    }

    public String toStringWithoutItems() {
        return "TransportFile:\n" +
                "destinationFiles=" + destinationFiles.stream().
                map(ServiceDestinationFile::toStringWithoutItems).collect(Collectors.joining()) +
                ", fileId=" + fileId +
                ", started=" + started +
                ", startDate=" + startDate +
                ", driver=" + driver +
                ", truck=" + truck +
                ", source=" + source +
                ", startingWeight=" + startingWeight +
                ", from=" + from +
                ", to=" + to +
                ", comment=" + comment +
                '\n';
    }

    public String getComment() {
        return comment;
    }

    public List<ServiceDestinationFile> getDestinationFiles() {
        return destinationFiles;
    }

    public int getFileId() {
        return fileId;
    }

    public boolean isStarted() {
        return started;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ServiceDriver getDriver() {
        return driver;
    }

    public ServiceTruck getTruck() {
        return truck;
    }

    public Point getSource() {
        return source;
    }

    public int getStartingWeight() {
        return startingWeight;
    }

    public Zone getFrom() {
        return from;
    }

    public Zone getTo() {
        return to;
    }


}
