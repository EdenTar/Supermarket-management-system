package Backend.Logic.Points;


import Backend.DataAccess.DAOs.TransportDAOs.PointDAO;

import Backend.DataAccess.DTOs.TransportDTOS.PointDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TransportMap {

    private PointDAO pointDAO;


    public TransportMap() {
        this.pointDAO=new PointDAO();

    }
    public List<String> getAllPoints()
    {
        return pointDAO.selectAll().stream().map(Object::toString).collect(Collectors.toList());
    }

    public Point getPoint(String origin) {

        return pointDAO.getRow(PointDTO.getPK(origin));
    }
    public void insertPoint(Point point)
    {
        pointDAO.insert(point);
    }


}
