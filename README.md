# timeKiller_GUI



## How to use TimeKiller Application

In the `TimeKillerApplication` class, modify the `databaseName` variable to either `"timekiller.db"` or `"test_timekiller.db"` based on your requirements.

- For integration and unit tests, use `"test_timekiller.db"`. This database is suitable for testing purposes and ensures that your tests do not interfere with production data.

```java
private String databaseName = "test_timekiller.db";