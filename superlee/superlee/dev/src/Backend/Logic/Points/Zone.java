package Backend.Logic.Points;

public enum Zone {
    HA_NEGEV(1),HA_DAROM(2), HA_MERKAZ(3),HA_SHFELA(4),AYALON(5), DAN(6), YARKON(7) ,HA_SHARON(8),SHOMRON(9)
    ,MENASHE(10),ASHER(11), HEVRON(12),KINERT(13),  HA_ZAFON(14),  HA_GALIL(15);
    private int numVal;

    Zone(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }


}
