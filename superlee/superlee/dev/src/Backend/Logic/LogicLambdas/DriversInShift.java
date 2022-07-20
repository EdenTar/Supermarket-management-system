package Backend.Logic.LogicLambdas;


import Backend.Logic.LogicObjects.Jobs.Driver;

import java.util.Date;
import java.util.List;

public interface DriversInShift {
    public List<Driver> inShift(Date date);

}
