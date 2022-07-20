package Backend.Logic.LogicObjects.Transport;

import Backend.DataAccess.DAOs.TransportDAOs.TransportItemDAO;

public class TransportItem {
    private String name;//primary key
    private int quantity;
    private int orderTransportId;//primaryKey can be null
    private String destinationFileId;//primaryKey can be null
    private TransportItemDAO itemDAO;

    public TransportItem(String name, int quantity, int orderTransportId, String destinationFileId) {
        this.name = name;
        this.quantity = quantity;
        this.orderTransportId = orderTransportId;
        this.destinationFileId = destinationFileId;
        this.itemDAO=new TransportItemDAO();
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        update();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        update();
    }

    public int getOrderTransportId() {
        return orderTransportId;
    }

    public void setOrderTransportId(int orderTransportId) {
        int tempOrder=this.orderTransportId;
        String tempDest=this.destinationFileId;
        this.orderTransportId = orderTransportId;
        this.destinationFileId="none";
        updatePk(tempDest,tempOrder);
    }

    public String getDestinationFileId() {
        return destinationFileId;
    }

    public void setDestinationFileId(String destinationFileId) {
        int tempOrder=this.orderTransportId;
        String tempDest=this.destinationFileId;
        this.destinationFileId = destinationFileId;
        this.orderTransportId=0;
        updatePk(tempDest,tempOrder);
    }


    private void update()
    {
        itemDAO.update(this);
    }
    private void updatePk(String destinationFileId,int orderTransportId)
    {
        itemDAO.update(this,destinationFileId,orderTransportId,name);
    }
}
