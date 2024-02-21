package de.hdm.bd.timekiller.model.task;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    private boolean active;
    @ForeignCollectionField
    private Collection<DurationTracker> records = new ArrayList<>();
    private DurationTracker activeRecord;

    public Task() {
    }

    public Task(String name) throws IllegalNameException {
        this();
        if(!checkName(name)) {
            throw new IllegalNameException(name);
        }
        this.name = name;
        active = false;
        activeRecord = null;
    }
    public Task(int id, String name) throws IllegalNameException {
        this(name);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) throws IllegalNameException {
        if(!checkName(n)) {
            throw new IllegalNameException(name);
        }
        this.name = n;
    }

    public boolean isActive() {
        return active;
    }

    public Collection<DurationTracker> getRecords() {
        return records;
    }



    DurationTracker createRecord() {
        return new DurationTracker();
    }

    private float durationInSeconds(long n) {
        return ((int) (Math.round(n * 0.1))) / ((float) 100.0);
    }

    public void start() {
        DurationTracker record = createRecord();
        record.start();
        activeRecord = record;
        active = true;
        addRecordToTask(activeRecord);
    }

    private void addRecordToTask(DurationTracker record) {
        getRecords().add(record);
    }

    public void stop() {
        activeRecord.stop();
        active = false;

        //support.firePropertyChange("record", this.id, activeRecord);
        activeRecord = null;
    }

    public long getOverallDuration() {
        long result = 0;
        for (DurationTracker record : records) {
            result = result + record.getDuration();
        }
        return result;
    }

    public long getOverallDuration(Date start, Date end) {
        long result = 0;
        long startTime = start.getTime();
        long endTime = end.getTime();

        for (DurationTracker record : records) {
            if ((startTime <= record.getStartTime()) && endTime >= record.getEndTime()) {
                result = result + record.getDuration();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    private boolean checkName(String input) {

        // Definieren Sie den regulären Ausdruck (Regex)
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";

        // Erstellen Sie ein Pattern-Objekt
        Pattern pattern = Pattern.compile(regex);

        // Erstellen Sie ein Matcher-Objekt
        Matcher matcher = pattern.matcher(input);

        // Überprüfen, ob der String den Bedingungen entspricht
        return matcher.matches();
    }

    // Observer pattern
    // https://www.baeldung.com/java-observer-pattern, Kap. 4

    private PropertyChangeSupport support;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
