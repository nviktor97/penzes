package hu.nye.penzugyi;

public class TetelItem {
    private String mNev;
    private String mDatum;
    private String mErtek;
    private String mTipus;

    public TetelItem(String nev, String datum, String ertek, String tipus){
        mNev = nev;
        mDatum = datum;
        mErtek = ertek;
        mTipus = tipus;
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
}
