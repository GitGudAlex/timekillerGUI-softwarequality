package de.hdm.bd.timekiller.model.task;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class DurationTracker {

    public static final String START_FIELD_NAME = "start";
    public static final String END_FIELD_NAME = "end";
    public static final String DURATION_FIELD_NAME = "duration";
    public static final String TASK_FIELD_NAME = "task";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = START_FIELD_NAME, canBeNull = false)
    private Date start;

    @DatabaseField(columnName = END_FIELD_NAME)
    private Date end;

    @DatabaseField(columnName = DURATION_FIELD_NAME)
    private long duration = 0;

    @DatabaseField(foreign = true, columnName = TASK_FIELD_NAME)
    private Task task;


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
            System.out.println("setEnd: " + duration);
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
