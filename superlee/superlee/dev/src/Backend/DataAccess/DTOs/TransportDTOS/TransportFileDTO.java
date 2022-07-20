package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class TransportFileDTO extends DTO<PK> {

    private final long id;
    private final long started;
    private final String startingDate;
    private final String endDate;
    private final long driverId;
    private final long truckId;
    private final String source;
    private final long startingWeight;
    private final String fromZone;
    private final String toZone;
    private final String comment;
    private final long isFinish;

    @Override
    public Object[] getValues() {
        return new Object[]{id, started, startingDate, endDate, driverId, truckId, source, startingWeight,
                fromZone, toZone, comment, isFinish};
    }

    public TransportFileDTO(long id, long started, String startingDate, String endDate, long driverId,
                            long truckId, String source, long startingWeight,
                            String fromZone, String toZone, String comment, long isFinish) {
        super(new PK(getFields(), id));
        this.id = id;
        this.started = started;
        this.startingDate = startingDate;
        this.endDate = endDate;
        this.driverId = driverId;
        this.truckId = truckId;
        this.source = source;
        this.startingWeight = startingWeight;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.comment = comment;
        this.isFinish = isFinish;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, TransportFileDTO.class);
    }

    public static PK getPK(long id) {
        return new PK(getFields(), id);
    }

    public long getId() {
        return id;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public long getStarted() {
        return started;
    }

    public String getEndDate() {
        return endDate;
    }

    public long getDriverId() {
        return driverId;
    }

    public long getTruckId() {
        return truckId;
    }

    public String getSource() {
        return source;
    }


    public long getStartingWeight() {
        return startingWeight;
    }


    public String getFromZone() {
        return fromZone;
    }


    public String getToZone() {
        return toZone;
    }


    public String getComment() {
        return comment;
    }

    public long getIsFinish() {
        return isFinish;
    }


}



