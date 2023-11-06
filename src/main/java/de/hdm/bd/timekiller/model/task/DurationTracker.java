package de.hdm.bd.timekiller.model.task;

import java.util.Date;

public class DurationTracker {
    private int taskId;
    private Date startTime;
    private Date endTime;
    private long duration;

    public DurationTracker(int taskId){
        this.taskId = taskId;
        this.duration = 0;
    }

    public int getTaskId(){
        return taskId;
    }
    public void start(){
        if(startTime == null){
            startTime = new Date();
        }
    }
    public void stop(){
        if(startTime != null){
            endTime = new Date();
            duration += (endTime.getTime() - startTime.getTime())/1000;
            startTime = null;
        }
    }
    public long getDuration(){
        return duration;
    }
}
