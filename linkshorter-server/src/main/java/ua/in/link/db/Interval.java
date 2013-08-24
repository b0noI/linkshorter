package ua.in.link.db;

public enum
        Interval {

    SECOND(3, -1),
    MINUTE(10, -1 * 60),
    HOUR(30, -1 * 60 * 60),
    DAY(100, -1 * 60 * 60 * 24);

    private final long PERMITTED_NUMBER;
    private final int INTERVAL_SECONDS;

    private Interval(long PERMITTED_NUMBER, int INTERVAL_SECONDS){
        this.PERMITTED_NUMBER = PERMITTED_NUMBER;
        this.INTERVAL_SECONDS = INTERVAL_SECONDS;
    }

    public long getPermittedNumber() {
        return PERMITTED_NUMBER;
    }

    public int getInterval_Seconds() {
        return INTERVAL_SECONDS;
    }

}

