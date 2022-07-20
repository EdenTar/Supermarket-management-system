package Backend.DataAccess.DTOs.StockDTOS;

import Backend.DataAccess.DTOs.DTO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;

import java.lang.reflect.Field;

public class DeliveryDTO extends DTO<PK> {
    private final int deliveryId;
    private final String itemName;
    private final String itemDestinationFileId;
    private final int itemOrderTransportId;
    public DeliveryDTO(int deliveryId, String itemName, String itemDestinationFileId, int itemOrderTransportId){
        super(new PK(getFields(), deliveryId,itemName));
        this.deliveryId = deliveryId;
        this.itemName = itemName;
        this.itemDestinationFileId = itemDestinationFileId;
        this.itemOrderTransportId = itemOrderTransportId;
    }
    public static Field[] getFields() {
        return DTO.getFields(new String[]{"deliveryId","itemName"}, DeliveryDTO.class);
    }

    public static PK getPK(long id) {
        return new PK(getFields(), id);
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDestinationFileId() {
        return itemDestinationFileId;
    }

    public int getItemOrderTransportId() {
        return itemOrderTransportId;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{deliveryId, itemName, itemDestinationFileId, itemOrderTransportId};
    }
}
