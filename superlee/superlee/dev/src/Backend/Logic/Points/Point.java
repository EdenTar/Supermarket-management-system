package Backend.Logic.Points;

import Backend.Logic.Controllers.TransportEmployee.ShiftController;

public abstract class Point {
    private String address;
    private String phone;
    private ShiftController shiftController;
    private String contactName;
    private Zone zone;

    public Point(String address, String phone, String contactName, Zone zone) {
        this.address = address;
        this.phone = phone;
        this.contactName = contactName;
        this.zone = zone;
        shiftController=new ShiftController();
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
    @Override
    public String toString() {
        return
                "address='" + address + '\'' +
                        ", phone='" + phone + '\'' +
                        ", contactName='" + contactName + '\'' +
                        ", zone=" + zone;
    }
}
