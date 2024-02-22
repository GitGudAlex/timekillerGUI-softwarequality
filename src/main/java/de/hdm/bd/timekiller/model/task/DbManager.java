package de.hdm.bd.timekiller.model.task;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DbManager {
    public static final String DATABASE_NAME = "timekiller.db";
    private Dao<Task, Integer> taskDao;
    private Dao<DurationTracker, Integer> durationTrackerDao;
    private ConnectionSource connectionSource;

    public DbManager() throws Exception {
        connectionSource = null;
        try {
            connectionSource = connect();
            setupDatabase(connectionSource);

        } finally {
            closeConnection(connectionSource);
        }
    }

    private ConnectionSource connect() throws SQLException {
        return new JdbcConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
    }

    private void closeConnection(ConnectionSource c) throws Exception {
        if (c != null) {
            c.close();
        }
    }

    private void setupDatabase(ConnectionSource c) throws SQLException {
        taskDao = DaoManager.createDao(c, Task.class);
        durationTrackerDao = DaoManager.createDao(c, DurationTracker.class);
        TableUtils.createTableIfNotExists(c, Task.class);
        TableUtils.createTableIfNotExists(c, DurationTracker.class);
    }
    public Dao<Task, Integer> getTaskDao() {
        return taskDao;
    }

    public Dao<DurationTracker, Integer> getDurationTrackerDao() {
        return durationTrackerDao;
    }

    public float getTotalDurationForTimePeriod(Date startDate, Date endDate) {
        try {
            QueryBuilder<DurationTracker, Integer> queryBuilder = durationTrackerDao.queryBuilder();
            queryBuilder.where().between("start", startDate, endDate);
            List<DurationTracker> durationTrackers = queryBuilder.query();

            float totalDuration = 0;
            for (DurationTracker tracker : durationTrackers) {
                totalDuration += tracker.getDuration();
            }
            return totalDuration;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteDurationTrackersForTask(Task task) throws SQLException {
        //Passende Task ID zur ID in Durationtracker finden und ebenfalls löschen
        Dao<DurationTracker, Integer> durationTrackerDao = getDurationTrackerDao();
        DeleteBuilder<DurationTracker, Integer> deleteBuilder = durationTrackerDao.deleteBuilder();
        deleteBuilder.where().eq(DurationTracker.TASK_FIELD_NAME, task);
        deleteBuilder.delete();
    }

    public void dropTables() throws SQLException{
        TableUtils.dropTable(connectionSource, Task.class, true);
        TableUtils.dropTable(connectionSource, DurationTracker.class, true);
    }
}
