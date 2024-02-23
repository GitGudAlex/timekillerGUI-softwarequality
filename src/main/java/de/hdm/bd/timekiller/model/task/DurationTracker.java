package de.hdm.bd.timekiller.model.task;

import java.util.Date;

public class DurationTracker {

    private int id;
    private Date start;

    private Date end;

    private long duration = 0;


    public DurationTracker() {
// all persisted classes must define a no-arg constructor with at least package visibility
    }


    public long getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public long getStartTime() {
        if (start != null) {
            return start.getTime();
        }
        return 0;
    }

    public long getEndTime() {
        if (end != null) {
            return end.getTime();
        }
        return 0;
    }

    void setStart(long time) {
        this.start = new Date(time);
    }

    void setEnd(long time) {
        this.end = new Date(time);
        if(start!= null) {
            duration = end.getTime() - start.getTime();
        }
    }

    Date getCurrentDate() {
        return new Date();
    }

    public void start() {
        setStart(getCurrentDate().getTime());
    }

    public void stop() {
        setEnd(getCurrentDate().getTime());
    }
}
