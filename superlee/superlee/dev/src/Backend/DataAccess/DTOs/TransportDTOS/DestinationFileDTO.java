package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class DestinationFileDTO extends DTO<PK> {

    private final String id;
    private final String source;
    private final String destination;
    private final long isDone;
    private final String creationBased;
    private final String arrivalDate;
    private final long transportId;
    private final String supplierId;

    public DestinationFileDTO(String id, String source, String destination,
                              long isDone, String arrivalDate, String creationBased,
                              long transportId,String supplierId) {
        super(new PK(getFields(), id));
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.isDone = isDone;
        this.arrivalDate = arrivalDate;
        this.creationBased = creationBased;
        this.transportId = transportId;
        this.supplierId = supplierId;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, DestinationFileDTO.class);
    }

    public static PK getPK(String id) {
        return new PK(getFields(), id);
    }


    public String getId() {
        return id;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getIsDone() {
        return isDone;
    }


    @Override
    public Object[] getValues() {
        return new Object[]{id, source, destination, isDone, creationBased, arrivalDate, transportId,supplierId};
    }

    public String getCreationBased() {
        return creationBased;
    }

    public long getTransportId() {
        return transportId;
    }

    public String getSupplierId() {
        return supplierId;
    }
}



