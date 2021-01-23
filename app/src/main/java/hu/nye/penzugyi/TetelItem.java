package hu.nye.penzugyi;

public class TetelItem {
    private String mNev;
    private String mDatum;
    private String mErtek;
    private String mTipus;
    private Integer mId;

    public TetelItem(String nev, String datum, String ertek, String tipus, Integer id){
        mNev = nev;
        mDatum = datum;
        mErtek = ertek;
        mTipus = tipus;
        mId = id;
    }

    public String getmNev() {
        return mNev;
    }
    public String getmDatum() {
        return mDatum;
    }
    public String getmErtek () {
        return mErtek;
    }
    public String getmTipus() {
        return mTipus;
    }
    public Integer getmId() {
        return mId;
    }
}
