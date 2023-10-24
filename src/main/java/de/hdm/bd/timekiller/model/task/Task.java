package de.hdm.bd.timekiller.model.task;

import java.util.Date;

public class Task {
    private int id;
    private DurationTracker durationTracker;
    private String name;
    private boolean active;
    private Date startTime;
    private Date endTime;


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
            //startTime = new Date();
            durationTracker.start();
            System.out.println("Task start DurationTracker");
        }
    }
    public void stop(){
        if(active){
            active = false;
            //endTime = new Date();
            durationTracker.stop();
            System.out.println("Task stop DurationTracker");
        }
    }
    public long getOverallDuration(){
        return durationTracker.getDuration();
        /*
        if(startTime != null && endTime != null){
            System.out.println((endTime.getTime() - startTime.getTime())/1000);
            return (endTime.getTime() - startTime.getTime()) / 1000;
        }else{
            return 0;
        }
        */
    }
}
