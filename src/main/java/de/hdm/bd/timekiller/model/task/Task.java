package de.hdm.bd.timekiller.model.task;

import java.util.Date;

public class Task {
    private int id;
    private String name;
    private boolean active;
    private Date startTime;
    private Date endTime;



    public Task(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = false;
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
            startTime = new Date();
        }
    }
    public void stop(){
        if(active){
            active = false;
            endTime = new Date();
        }
    }
    public long getOverallDuration(){
        if(startTime != null && endTime != null){
            return endTime.getTime() - startTime.getTime();
        }else{
            return 0;
        }
    }
}
