package de.hdm.bd.timekiller.model.task;

import java.util.Date;

public class Task {
    private int id;
    private DurationTracker durationTracker;
    private String name;
    private boolean active;
    private Date startTime;
    private Date endTime;

    private String As9da;


    public Task(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = false;
        this.durationTracker = new DurationTracker(id);
    }

    public String toString() {
        return name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public boolean isActive(){
        return active;
    }
    public void start(){
        if(!active){
            active = true;
            durationTracker.start();
            System.out.println("Task start DurationTracker");
        }
    }
    public void stop(){
        if(active){
            active = false;
            durationTracker.stop();
            System.out.println("Task stop DurationTracker");
        }
    }
    public long getOverallDuration(){
        return durationTracker.getDuration();
    }
    public void clearDuration(){
        durationTracker.reset();
        active = false;
    }
    public void setDurationTracker(DurationTracker durationTracker) {
        this.durationTracker = durationTracker;
    }
}
