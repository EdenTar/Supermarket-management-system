package Backend.Logic.LogicLambdas;

import Backend.Logic.Points.Branch;

import java.util.Date;

public interface IsStoreKeeperInShift {
    public boolean isStoreKeeperInShift(Branch branch, Date date);
}
